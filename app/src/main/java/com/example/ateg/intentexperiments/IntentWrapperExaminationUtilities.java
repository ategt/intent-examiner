package com.example.ateg.intentexperiments;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by ATeg on 6/14/2017.
 */

public class IntentWrapperExaminationUtilities {

    public static StringBuilder stringifyIntent(IntentWrapper intent, StringBuilder sb) {
        String action = intent.getAction();
        int flags = intent.getFlags();
        Set<String> categories = intent.getCategories();

        sb.append("Action: ");
        sb.append(action);
        sb.append(System.lineSeparator());

        sb.append("Flags: ");
        sb.append(flags);
        sb.append(System.lineSeparator());

        if (categories != null) {
            sb.append("Categories: ");
            Iterator<String> categoriesIterator = categories.iterator();
            while (categoriesIterator.hasNext()) {
                sb.append(categoriesIterator.next());
                if (categoriesIterator.hasNext())
                    sb.append(", ");
            }
            sb.append(System.lineSeparator());
        }

        String type = intent.getType();
        if (type != null) {
            sb.append("Type: ");
            sb.append(type);
            sb.append(System.lineSeparator());
        }

        String dataString = intent.getDataString();
        if (dataString != null) {
            sb.append("Data: ");
            sb.append(dataString);
            sb.append(System.lineSeparator());
        }

        ComponentName componentName = intent.getComponent();

        if (componentName != null) {
            String componentString = componentName.getPackageName();
            if (componentString != null) {
                sb.append("Component: ");
                sb.append(componentString);
                sb.append(System.lineSeparator());
            }
        }

        String intentPackage = intent.getPackage();
        if (intentPackage != null) {
            sb.append("Package: ");
            sb.append(intentPackage);
            sb.append(System.lineSeparator());
        }

        String scheme = intent.getScheme();
        if (scheme != null) {
            sb.append("Scheme: ");
            sb.append(scheme);
            sb.append(System.lineSeparator());
        }

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
