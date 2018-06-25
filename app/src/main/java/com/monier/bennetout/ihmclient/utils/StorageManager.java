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

    private static void writeToFile(File file, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
    }

    private static byte[] readFromFile(File file) throws IOException {
        byte[] ret;
        FileInputStream fis = new FileInputStream(file);
        ret = new byte[fis.available()];
        fis.read(ret);
        fis.close();
        return ret;
    }

    private static void performObjectSaving(File file, Context context, Object object) {

        if (file == null)
            return;

        int retry = 100;

        while (retry > 0) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(byteArrayOutputStream, object);
                byteArrayOutputStream.flush();
                byte[] datas = byteArrayOutputStream.toByteArray();
                writeToFile(file, datas);
                retry = 0;
            } catch (IOException e) {
                /* Gestion de la saturation de la mémoire, dans ce cas on
                 * supprime le fichier de log le plus anciennement modifié. */
                if (e.getMessage().contains("ENOSPC")) {
                    removeOldestLogFile(context);
                    Log.e(TAG, "No space left on device, trying to remove old logs");
                    retry--;
                } else {
                    e.printStackTrace();
                    retry = 0;
                }
            }
        }
    }

    private static Object performObjectLoading(File file, Class<?> valueType) throws IOException {
        if (file == null)
            return null;

        Object object;
        JsonFactory jsonFactory = new MappingJsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonParser jsonParser = jsonFactory.createParser(readFromFile(file));
        object = objectMapper.readValue(jsonParser, valueType);

        return object;
    }

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

    private static double getFreeSize(Context context) {
        StatFs statFs = new StatFs(getDocumentsDir(context).getPath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return (double)availableBlocks * (double)blockSize;
    }

    //**********************************************************************************************
    //                                  PUBLIC API                                                 *
    //**********************************************************************************************

    // Renvoi l'unique planning pour la supervision
    // Créer un nouveau fichier planning sinon
    // null
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
    static File getDocumentsDir(Context context) {

        File documentsDirName = null;

        if (context == null)
            return null;

        File[] filesDirs = context.getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS);
        if (filesDirs == null)
            return null;

        // filesDirs[0] = mémoire interne
        // filesDirs[1] = Sd Card
        switch (filesDirs.length) {
            case 2:
                if (filesDirs[1] != null)
                    documentsDirName = filesDirs[1];
                else
                    documentsDirName = filesDirs[0];
                break;

            case 1:
                documentsDirName = filesDirs[0];
                break;

            default:
                documentsDirName = null;
                break;
        }

        return documentsDirName;
    }


    // Renvoi dans l'ordre si erreur:
    //  - Un fichier du nom de filename sauvegardé sur la SD card (accessible par tout le monde)
    //  - Un fichier du nom de filename sauvegardé sur la mémoire interne (accessible par tout le monde)
    //  - null
    @Nullable
    public static File getLogFileFromDocExtStorage(Context context, String filename, String extension, boolean withDate) {

        if (!isStorageReady(context))
            return null;

        File documentsDirName = getDocumentsDir(context);
        if (documentsDirName == null)
            return null;

        String completFilename = filename;
        if (withDate) {
            DateFormat dateFormat = new SimpleDateFormat("ddMMMHHmmss", Locale.FRANCE);
            completFilename += "_" + dateFormat.format(new Date()) + extension;
        } else {
            completFilename += extension;
        }

        // Get the directory for the user's public document directory.
        File logFolder = new File(documentsDirName, "logs");
        if (!logFolder.exists()) {
            if (!logFolder.mkdirs())
                return null;
        }
        return new File(documentsDirName, "logs/" + completFilename);
    }

    @NonNull
    public static String getFreeSizeString(Context context) {
        double size = getFreeSize(context);
        String suffix;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "GB";
                    size /= 1024;
                }
            }
        } else {
            suffix = "Bits";
        }

        return String.format(Locale.FRANCE, "%.2f", size) + suffix;
    }

    /* true if the oldest file was deleted
     * false otherwise */
    public static boolean removeOldestLogFile(Context context) {
        File dir = new File(getDocumentsDir(context), "logs");
        if (!dir.isDirectory())
            return false;

        String[] childrens = dir.list();

        if (childrens.length == 0)
            return false;

        Date oldestDate = new Date();
        File oldestFile = null;

        for (String file : childrens) {
            File testFile = new File(dir, file);
            Date testDate = new Date(testFile.lastModified());

            if (testDate.before(oldestDate)) {
                oldestDate = testDate;
                oldestFile = testFile;
            }
        }

        return oldestFile != null && oldestFile.delete();
    }

    public static boolean removeAllLogs(Context context) {
        File dir = new File(getDocumentsDir(context), "logs");
        if (!dir.isDirectory())
            return false;

        String[] childrens = dir.list();
        for (String file:childrens) {
            File toBeDeleted = new File(dir, file);
            if (!toBeDeleted.delete())
                return false;
        }

        return true;
    }
}
