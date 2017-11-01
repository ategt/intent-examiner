package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ATeg on 10/20/2017.
 */

public class ExamineServices {
    
    public String examineIntent(Intent intent) {
        //TextView textView = (TextView) view.getRootView().findViewById(R.id.central_textView);
        Log.d("njhy", "Exm examineIntent");

        StringBuilder sb = new StringBuilder();

        sb = IntentExaminationUtilities.stringifyIntent(intent, sb);

        Bundle bundle = intent.getExtras();
        Log.d("njhy", "Exm examineIntent1");

        if (bundle != null) {
            sb = IntentExaminationUtilities.stringifyBundle(sb, bundle);
            Log.d("njhy", "Exm examineIntent2");

            return sb.toString();
            //textView.setText(sb.toString());
            //textView.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            Log.d("njhy", "Exm examineIntent3");

            return null;
            //textView.setText(R.string.intent_empty);
            //textView.setGravity(Gravity.CENTER);
        }
    }

    public String examineIntent(IntentWrapper intent) {
        StringBuilder sb = new StringBuilder();

        sb = IntentWrapperExaminationUtilities.stringifyIntent(intent, sb);

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            sb = IntentExaminationUtilities.stringifyBundle(sb, bundle);
            return sb.toString();
        } else {
            return null;
        }
    }
}