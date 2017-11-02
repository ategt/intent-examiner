package com.example.ateg.intentexperiments;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by ATeg on 6/9/2017.
 */

public class LoggingUtilities {
    private static final String TAG = "Logging Utilites";
    private File logFile;
    private String directoryType = Environment.DIRECTORY_DOCUMENTS;

    public LoggingUtilities(@NonNull File logFile) {
        this.setLogFile(logFile);
    }

    public LoggingUtilities(@NonNull String logFile, String directoryType) {

        initLogFile(logFile, directoryType);
    }

    private void initLogFile(@NonNull String logFile, String directoryType) {
        File createdLogFile = establishLogFile(directoryType);
        this.setLogFile(createdLogFile);
    }

    public static LoggingUtilities defaultLogger() {
        return new LoggingUtilities("everythingLog.txt", Environment.DIRECTORY_DOCUMENTS);
    }

    public File establishLogFile(String fileName) {
        return establishLogFile(getDirectoryType());
    }

    public static File establishLogFile(String directoryType) {

        File[] files = ContextCompat.getExternalFilesDirs(directoryType);

        Log.i(TAG, "Files Matching request: " + files.length);

        File storageDir = null;

        for (File x : files) {
            storageDir = x;
        }

        generateFolderTree(storageDir);

        String fileName = PreferencesUtilites.getDefaultFileName();

        return new File(storageDir, fileName);
    }

    public static void generateFolderTree(File storageDir) {
        if (!storageDir.exists() || !storageDir.isDirectory()) {
            if (!storageDir.mkdirs()) {
                throw new LoggingUtilitiesException("Folder Generation Failed.");
            }
        }
    }

    public boolean updateTextFile(String str) {
        return updateTextFile(str, getLogFile());
    }

    public static boolean updateTextFile(String str, File inputFile) {

        StringBuilder stringBuilder = new StringBuilder();

        if (inputFile.exists()) {
            String outputString = readFile(inputFile);

            stringBuilder.append(outputString);
            stringBuilder.append(System.getProperty("line.separator"));
        }

        stringBuilder.append(str);
        File txtFile = inputFile;

        try {
            backupCurrentLog(txtFile);
        } catch (IOException e) {
            Log.e("adf", "IO problem ", e);
            throw new LoggingUtilitiesException("Something went wrong while backing up the logs.");
        }

        boolean didWriteSucceed = writeFile(stringBuilder, txtFile);

        scanFileToFileSystemIndex(txtFile);

        return didWriteSucceed;
    }

    private static boolean writeFile(StringBuilder stringBuilder, File txtFile) {
        File storageDir = null;
        FileOutputStream fileOutputStream = null;
        try {

            storageDir = txtFile.getParentFile();

            generateFolderTree(storageDir);

            File tempLog = File.createTempFile("log_", ".txt", storageDir);

            fileOutputStream = new FileOutputStream(tempLog);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(stringBuilder.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            tempLog.renameTo(txtFile);
            return true;
        } catch (IOException e) {
            Log.e("tag", "Problem " + (storageDir.isDirectory() ? "Dir" : "File") + " " + txtFile.getParentFile().toURI(), e);
            throw new LoggingUtilitiesException("Problem text.");
        } finally {
            if (fileOutputStream != null)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e("tag", "Big IO problem.", e);
                    throw new LoggingUtilitiesException("Big Io Problem.");
                }
        }
    }

    private static void backupCurrentLog( File txtFile) throws IOException {
        File oldFile = new File(txtFile.getAbsolutePath() + ".old");
        if (oldFile.exists()) {
            File junkFile = File.createTempFile("log_", ".txt.old", txtFile.getParentFile());
            if (oldFile.renameTo(junkFile)) {
                junkFile.delete();
            }
        }

        File newOldFile = new File(txtFile.getAbsolutePath() + ".old");
        if (txtFile.renameTo(newOldFile)) {
            scanFileToFileSystemIndex( newOldFile);
        }
    }

    private static void scanFileToFileSystemIndex( File newOldFile) {
        Uri txtUri = Uri.fromFile(newOldFile);

        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(context,
                new String[]{txtUri.getPath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    public String readFile(File inputFile) {
        return readFile(context, inputFile);
    }

    @Nullable
    public static String readFile(Context context, File inputFile) {

        StringBuffer sb = new StringBuffer();
        FileInputStream fileInputStream = null;
        try {
            char[] inputBuffer = new char[1024];

            fileInputStream = new FileInputStream(inputFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            int nRead = 0;
            while ((nRead = inputStreamReader.read(inputBuffer)) > 0) {
                sb.append(String.copyValueOf(inputBuffer, 0, nRead));
            }
            return sb.toString();

        } catch (FileNotFoundException e) {
            Log.e("tag", "Problem", e);
            Toast.makeText(context, "File not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("tag", "Problem", e);
            Toast.makeText(context, "Problem text.", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public String getDirectoryType() {
        return directoryType;
    }

    public void setDirectoryType(String directoryType) {
        this.directoryType = directoryType;
    }

    public static String getDefaultFileName(Context context) {
        return PreferencesUtilites.getDefaultFileName(context);
    }
}