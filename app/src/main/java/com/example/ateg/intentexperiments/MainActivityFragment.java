package com.example.ateg.intentexperiments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        int flags = intent.getFlags();

        if (bundle != null) {
            int size = bundle.size();
            Set<String> keysSet = bundle.keySet();

            TextView textView = (TextView) container.findViewById(R.id.central_textView);

            StringBuilder sb = new StringBuilder();

            for (String key : keysSet) {
                sb.append(key);
                sb.append(System.lineSeparator());
            }

            textView.setText(sb.toString());
        }

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
