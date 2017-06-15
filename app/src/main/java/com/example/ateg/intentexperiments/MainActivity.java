package com.example.ateg.intentexperiments;

import android.content.Intent;
import android.os.Bundle;
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
                StringBuilder sb = new StringBuilder();
                TextView textView = (TextView) view.getRootView().findViewById(R.id.central_textView);

                Intent intent = getIntent();
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();
                int flags = intent.getFlags();

                sb.append("Action: ");
                sb.append(action);
                sb.append(System.lineSeparator());

                sb.append("Flags: ");
                sb.append(flags);
                sb.append(System.lineSeparator());

                if (bundle != null) {

                    sb.append("Intent Bundle Size: ");
                    sb.append(bundle.size());
                    sb.append(System.lineSeparator());
                    sb.append(System.lineSeparator());

                    Set<String> keysSet = bundle.keySet();

                    View rootView = view.getRootView();


                    for (String key : keysSet) {
                        sb.append(key);
                        sb.append(System.lineSeparator());
                        Object object = bundle.get(key);

                        sb.append("\t");
                        String canClassName = object.getClass().getCanonicalName();
                        String classString = object.getClass().toString();
                        sb.append(canClassName);
                        sb.append("\t");
                        sb.append(classString);
                        sb.append(System.lineSeparator());

                        sb.append("\t\t");
                        sb.append(object.toString());
                    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
