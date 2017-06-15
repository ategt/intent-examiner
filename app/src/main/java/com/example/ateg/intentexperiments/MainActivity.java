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
                Intent intent = getIntent();
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();
                int flags = intent.getFlags();

                if (bundle != null) {
                    int size = bundle.size();
                    Set<String> keysSet = bundle.keySet();

                    TextView textView1 = (TextView) view.findViewById(R.id.central_textView);
                    //TextView textView2 = (TextView) view.getParent()..findViewById(R.id.central_textView);
                    TextView textView3 = (TextView) view.getRootView().findViewById(R.id.central_textView);

                    View rootView = view.getRootView();

                    StringBuilder sb = new StringBuilder();

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
                        Log.i("asf", object.toString());
                    }

                    textView3.setText(sb.toString());
                }

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
