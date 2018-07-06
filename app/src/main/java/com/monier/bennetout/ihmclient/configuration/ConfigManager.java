package com.monier.bennetout.ihmclient.configuration;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monier.bennetout.ihmclient.utils.StorageManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import static com.monier.bennetout.ihmclient.utils.StorageManager.getConfigFileFromDocExtStorage;

public class ConfigManager {

    private static final String TAG = ConfigManager.class.getCanonicalName();

    private static boolean initDone = false;

    private static final int CREATE_NEW_CONFIG  = 1;
    private static final int LOAD_CONFIG_FROM_JSON  = 2;

    public static ConfigModel model;

    public static void initConfig(Context context) {

        if (initDone)
            return;

        File configFile = getConfigFileFromDocExtStorage(context);
        if (configFile == null) {
            Log.e(TAG, "Error: file is null");
            return;
        }

        int action;

        if (configFile.length() == 0) {
            action = CREATE_NEW_CONFIG;
            Log.e(TAG, "No config file found, create a new one");
        }
        else {
            action = LOAD_CONFIG_FROM_JSON;
            Log.i(TAG, "Config file found");
        }


        switch (action) {
            case CREATE_NEW_CONFIG:
                try {
                    createDefaultConfig(configFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case LOAD_CONFIG_FROM_JSON:
                boolean resultOk;
                try {
                    resultOk = loadConfig(configFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    resultOk = configFile.delete();
                }
                if (!resultOk) {
                    try {
                        createDefaultConfig(configFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
        initDone = true;
    }

    public static boolean configFile2Model(Context context) {
        File configFile = StorageManager.getConfigFileFromDocExtStorage(context);
        if (configFile == null)
            return false;

        try {
            return loadConfig(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean model2ConfigFile(Context context) {
        File configFile = StorageManager.getConfigFileFromDocExtStorage(context);
        if (configFile == null)
            return false;

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        try {
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
            jsonGenerator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);

            ConfigModel configModel = model;
            objectMapper.writeValue(jsonGenerator, configModel);

            FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write(stringWriter.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static boolean loadConfig(File configFile) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        FileReader fileReader = new FileReader(configFile);

        JsonParser jsonParser = jsonFactory.createJsonParser(fileReader);

        ConfigModel configModel = objectMapper.readValue(jsonParser, ConfigModel.class);
        if (configModel == null)
            return false;

        model = configModel;
        return true;
    }

    private static void createDefaultConfig(File configFile) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);

        ConfigModel configModel = new ConfigModel();  // New model with all default values
        objectMapper.writeValue(jsonGenerator, configModel);
        model = configModel;

        FileWriter fileWriter = new FileWriter(configFile);
        fileWriter.write(stringWriter.toString());
        fileWriter.close();
    }
}
