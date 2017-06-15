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

import java.io.File;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                final File logFile = saveTextViewContentToFile(textView);

                Snackbar.make(textView, "File Saved", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File tempFile = copyToTempFile(logFile);
                                Intent intent = intentToOpenFile(tempFile);
                                //startActivityForResult(intent, RESULT_OK);
                                startActivity(intent);
                            }
                        }).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private File saveTextViewContentToFile(TextView textView) {
        String examinationReport = textView.getText().toString();
        LoggingUtilities loggingUtilities = new LoggingUtilities(this, "IntentExamination.txt", Environment.DIRECTORY_DOCUMENTS);
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
