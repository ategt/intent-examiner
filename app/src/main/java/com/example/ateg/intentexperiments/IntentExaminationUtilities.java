package com.example.ateg.intentexperiments;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;
import java.util.Set;

/**
 * Created by ATeg on 6/14/2017.
 */

public class IntentExaminationUtilities {

    public static StringBuilder stringifyIntent(Intent intent, StringBuilder sb) {
        String action = intent.getAction();
        int flags = intent.getFlags();

        sb.append("Action: ");
        sb.append(action);
        sb.append(System.lineSeparator());

        sb.append("Flags: ");
        sb.append(flags);
        sb.append(System.lineSeparator());
        return sb;
    }

    public static StringBuilder stringifyBundle(StringBuilder sb, Bundle bundle) {
        sb.append("Intent Bundle Size: ");
        sb.append(bundle.size());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        Set<String> keysSet = bundle.keySet();

        for (String key : keysSet) {
            sb.append(key);
            sb.append(System.lineSeparator());
            Object object = bundle.get(key);

            sb.append("\t");
            if (object == null)
                sb.append("-Null-");
            else {
                String canClassName = object.getClass().getCanonicalName();
                String classString = object.getClass().toString();
                sb.append(canClassName);
                sb.append("\t");
                sb.append(classString);

                sb.append(System.lineSeparator());

                sb.append("\t\t");
                sb.append(object.toString());
            }
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }

        return sb;
    }


}
