package com.monier.bennetout.ihmclient.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StorageManager {

    private static final String TAG = StorageManager.class.getName();

    /* Checks if external storage is available for read and write */
    private static boolean isStorageReady(Context context) {

        if (getDocumentsDir(context) == null) {
            Log.e(TAG, "Error: storage is not ready");
            return false;
        }
        // API 21 needed...
//        String state = Environment.getExternalStorageState(getDocumentsDir(context));
//        if (!Environment.MEDIA_MOUNTED.equals(state)) {
//            Log.i(TAG, "Error: media not mounted -->(Environment.MEDIA_MOUNTED.equals(state) == false)");
//            return false;
//        }

        return true;
    }

    @Nullable
    public static File getConfigFileFromDocExtStorage(Context context) {

        if (!isStorageReady(context)) {
            Log.e(TAG, "Error: storage is not ready");
            return null;
        }

        File documentsDirName = getDocumentsDir(context);
        if (documentsDirName == null) {
            Log.e(TAG, "Error: documentsDirName is null");
            return null;
        }

        String dirName = "configuration";
        // Création d'un nouveau planning vierge
        String completFilename = "CONFIG.json";

        File file = new File(documentsDirName, dirName + "/" + completFilename);
        if (file.exists())
            return file;

        // Get the directory for the user's public document directory.
        File oversightFolder = new File(documentsDirName, dirName);
        if (!oversightFolder.exists()) {
            if (!oversightFolder.mkdirs())
                return null;
        }
        return new File(documentsDirName, dirName + "/" + completFilename);
    }

    /**
     * Récupère le répertoire Documents de l'application.<p/>
     * Les fichiers créés sont accessibles par tout le monde
     *
     * @param context App context pour l'acces aux ressources
     * @return Répertoire Documents de l'application sur la carte SD ou sur mémoire interne
     * du smartphone si aucune carte SD n'est détectée.<p/>
     * Retourne null si un problème est détecté
     */
    private static File getDocumentsDir(Context context) {

        File documentsDirName;

        if (context == null)
            return null;

        documentsDirName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BENNETOUT");

        if (!documentsDirName.exists()){
            if (!documentsDirName.mkdirs()) {
                Log.e(TAG, "Cannot create BENNETOUT base directory");
                return null;
            }
        }

        return documentsDirName;
    }
}
