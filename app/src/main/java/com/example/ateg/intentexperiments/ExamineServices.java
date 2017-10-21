package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ATeg on 10/20/2017.
 */

public class ExamineServices {

    private Context context;

    public ExamineServices(Context context) {
        this.context = context;
    }

    public void examineIntent(View view, Intent intent) {
        TextView textView = (TextView) view.getRootView().findViewById(R.id.central_textView);
        StringBuilder sb = new StringBuilder();

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
                        Toast.makeText(context, "Snackbar Clicked On.", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}