package com.example.ateg.intentexperiments;

import android.content.Context;
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
    private Context context;
    private File logFile;
    private String directoryType = Environment.DIRECTORY_DOCUMENTS;

    public LoggingUtilities(@NonNull Context context, @NonNull File logFile) {
        this.context = context;
        this.setLogFile(logFile);
    }

    public LoggingUtilities(@NonNull Context context, @NonNull String logFile, String directoryType) {
        this.context = context;

        initLogFile(context, logFile, directoryType);
    }

    private void initLogFile(@NonNull Context context, @NonNull String logFile, String directoryType) {
        File createdLogFile = establishLogFile(context, directoryType);
        this.setLogFile(createdLogFile);
    }

    public static LoggingUtilities defaultLogger(Context context) {
        return new LoggingUtilities(context, "everythingLog.txt", Environment.DIRECTORY_DOCUMENTS);
    }

    public File establishLogFile(String fileName) {
        return establishLogFile(context, getDirectoryType());
    }

    public static File establishLogFile(Context context, String directoryType) {

        File[] files = ContextCompat.getExternalFilesDirs(context, directoryType);

        Log.i(TAG, "Files Matching request: " + files.length);

        File storageDir = null;

        for (File x : files) {
            storageDir = x;
        }

        generateFolderTree(context, storageDir);

        String fileName = PreferencesUtilities.getDefaultFileName(context);

        return new File(storageDir, fileName);
    }

    public static void generateFolderTree(Context context, File storageDir) {
        if (!storageDir.exists() || !storageDir.isDirectory()) {
            if (!storageDir.mkdirs()) {
                Toast.makeText(context, "Folder Generation Failed.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean updateTextFile(String str) {
        return updateTextFile(context, str, getLogFile());
    }

    public static boolean updateTextFile(Context context, String str, File inputFile) {

        StringBuilder stringBuilder = new StringBuilder();

        if (inputFile.exists()) {
            String outputString = readFile(context, inputFile);

            stringBuilder.append(outputString);
            stringBuilder.append(System.getProperty("line.separator"));
        }

        stringBuilder.append(str);
        File txtFile = inputFile;

        try {
            backupCurrentLog(context, txtFile);
        } catch (IOException e) {
            Log.e(TAG, "IO problem ", e);
            Toast.makeText(context, "Something went wrong while backing up the logs.", Toast.LENGTH_SHORT).show();
        }

        boolean didWriteSucceed = writeFile(context, stringBuilder, txtFile);

        scanFileToFileSystemIndex(context, txtFile);

        return didWriteSucceed;
    }

    private static boolean writeFile(Context context, StringBuilder stringBuilder, File txtFile) {
        File storageDir = null;
        FileOutputStream fileOutputStream = null;
        try {

            storageDir = txtFile.getParentFile();

            generateFolderTree(context, storageDir);

            File tempLog = File.createTempFile("log_", ".txt", storageDir);

            fileOutputStream = new FileOutputStream(tempLog);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(stringBuilder.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            tempLog.renameTo(txtFile);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Problem " + (storageDir.isDirectory() ? "Dir" : "File") + " " + txtFile.getParentFile().toURI(), e);
            Toast.makeText(context, "Problem text.", Toast.LENGTH_SHORT).show();
        } finally {
            if (fileOutputStream != null)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Big IO problem.", e);
                    Toast.makeText(context, "Big Io Problem.", Toast.LENGTH_SHORT).show();
                }
        }
        return false;
    }

    private static void backupCurrentLog(Context context, File txtFile) throws IOException {
        File oldFile = new File(txtFile.getAbsolutePath() + ".old");
        if (oldFile.exists()) {
            File junkFile = File.createTempFile("log_", ".txt.old", txtFile.getParentFile());
            if (oldFile.renameTo(junkFile)) {
                junkFile.delete();
            }
        }

        File newOldFile = new File(txtFile.getAbsolutePath() + ".old");
        if (txtFile.renameTo(newOldFile)) {
            scanFileToFileSystemIndex(context, newOldFile);
        }
    }

    private static void scanFileToFileSystemIndex(Context context, File newOldFile) {
        Uri txtUri = Uri.fromFile(newOldFile);

        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(context,
                new String[]{txtUri.getPath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    public String readFile() {
        return readFile(context, getLogFile());
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
            Log.e(TAG, "Problem", e);
            Toast.makeText(context, "File not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Problem", e);
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
        return PreferencesUtilities.getDefaultFileName(context);
    }
}