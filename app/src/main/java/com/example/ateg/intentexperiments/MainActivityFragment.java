package com.example.ateg.intentexperiments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.action_button);
//        //FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.action_button);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                //        .setAction("Action", null).show();
//                Intent intent = getActivity().getIntent();
//                String action = intent.getAction();
//                Bundle bundle = intent.getExtras();
//                int flags = intent.getFlags();
//
//                if (bundle != null) {
//                    int size = bundle.size();
//                    Set<String> keysSet = bundle.keySet();
//
//                    TextView textView = (TextView) view.findViewById(R.id.central_textView);
//
//                    StringBuilder sb = new StringBuilder();
//
//                    for (String key : keysSet) {
//                        sb.append(key);
//                        sb.append(System.lineSeparator());
//                    }
//
//                    textView.setText(sb.toString());
//                }
//            }
//        });
//    }
}
