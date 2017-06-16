package com.example.ateg.intentexperiments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ateg.intentexperiments.FileSelector.FileOperation;
import com.example.ateg.intentexperiments.FileSelector.FileSelector;
import com.example.ateg.intentexperiments.FileSelector.OnHandleFileListener;

import java.io.File;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TEXT_WINDOW_VALUE = "com.example.ateg.intentexperiments.TEXT_WINDOW_VALUE";

    /** Sample filters array */
    final String[] mFileFilter = { ".txt", "*.*" };
    final String defaultLogFileName = "IntentExamination.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null){
            CharSequence textWindowValue = savedInstanceState.getCharSequence(TEXT_WINDOW_VALUE);
            TextView textViewAs = (TextView) findViewById(R.id.central_textView);
            textViewAs.setText(textWindowValue);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view.getRootView().findViewById(R.id.central_textView);
                StringBuilder sb = new StringBuilder();

                Intent intent = getIntent();
                sb = IntentExaminationUtilities.stringifyIntent(intent, sb);

                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    sb = IntentExaminationUtilities.stringifyBundle(sb, bundle);
                    textView.setText(sb.toString());
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                } else {
                    textView.setText(R.string.intent_empty);
                    textView.setGravity(Gravity.CENTER);
                }

                Snackbar.make(view, "Scan Completed", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "Snackbar Clicked On.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.save_to_file:
                TextView textView = (TextView) findViewById(R.id.central_textView);
                File logFile = saveTextViewContentToFile(textView,
                        LoggingUtilities.establishLogFile(this, defaultLogFileName, Environment.DIRECTORY_DOCUMENTS));

                fileSaveCompleteSnackBar(textView, logFile);

                return true;
            case R.id.save_to_file_as:
                File[] files = ContextCompat.getExternalFilesDirs(this, Environment.DIRECTORY_DOCUMENTS);

                File storageDir = files[files.length-1];

                new FileSelector(this,
                        FileOperation.SAVE,
                            new OnHandleFileListener() {
                                @Override
                                public void handleFile(String filePath) {
                                    TextView textViewAs = (TextView) findViewById(R.id.central_textView);
                                    File logFileAs = saveTextViewContentToFile(textViewAs, new File(filePath));

                                    fileSaveCompleteSnackBar(textViewAs, logFileAs);
                                }
                            },
                        mFileFilter,
                        new File(storageDir, defaultLogFileName))
                .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView textViewAs = (TextView) findViewById(R.id.central_textView);
        outState.putCharSequence(TEXT_WINDOW_VALUE, textViewAs.getText());
    }

    private void fileSaveCompleteSnackBar(TextView textViewAs, final File logFileAs) {
        Snackbar.make(textViewAs, "File Saved", Snackbar.LENGTH_INDEFINITE)
                .setAction("Open", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File tempFile = copyToTempFile(logFileAs);
                        Intent intent = intentToOpenFile(tempFile);
                        startActivity(intent);
                    }
                }).show();
    }

    private File saveTextViewContentToFile(TextView textView, File destinationFile) {
        String examinationReport = textView.getText().toString();
        LoggingUtilities loggingUtilities = new LoggingUtilities(this, destinationFile);
        loggingUtilities.updateTextFile(examinationReport);
        return loggingUtilities.getLogFile();
    }

    @NonNull
    private Intent intentToOpenFile(File tempFile) {
        Uri fileUri = FileProvider.getUriForFile(
                getApplicationContext(),
                "com.example.ateg.intentexperiments.FileProvider",
                tempFile);

        Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(fileUri,
                getContentResolver().getType(fileUri));
        return intent;
    }

    @NonNull
    private File copyToTempFile(File logFile) {
        File tempFile = new File(getCacheDir(), "temp.txt");

        new LoggingUtilities(getApplicationContext(), tempFile)
                .updateTextFile(LoggingUtilities.readFile(getApplicationContext(), logFile));

        tempFile.deleteOnExit();
        return tempFile;
    }
}
