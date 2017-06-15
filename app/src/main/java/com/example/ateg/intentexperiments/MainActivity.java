package com.example.ateg.intentexperiments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
                } else {
                    textView.setText(R.string.intent_empty);
                }

                Snackbar.make(view, "Scan Completed", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener(){
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
            return true;
            case R.id.save_to_file:
                TextView textView = (TextView) findViewById(R.id.central_textView);
                String examinationReport = textView.getText().toString();
                LoggingUtilities loggingUtilities = new LoggingUtilities(this, "IntentExamination.txt", Environment.DIRECTORY_DOCUMENTS);
                        loggingUtilities.updateTextFile(examinationReport);
                final File logFile = loggingUtilities.getLogFile();

                Snackbar.make(textView, "File Saved", Snackbar.LENGTH_LONG)
                        .setAction("Open", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.fromFile(logFile));
                                startActivity(intent);
                            }
                        }).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
