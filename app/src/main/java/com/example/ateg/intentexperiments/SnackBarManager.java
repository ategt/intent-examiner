package com.example.ateg.intentexperiments;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SnackBarManager {
    private final View createdView;
    protected List<Snackbar> mSnackbarList = new ArrayList<>();

    protected Snackbar.Callback mCallback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            mSnackbarList.remove(snackbar);
            if (mSnackbarList.size() > 0)
                displaySnackbar(mSnackbarList.get(0));
        }
    };

    public SnackBarManager(View createdView) {
        this.createdView = createdView;
    }

    public void addQueue(Snackbar snackbar) {
        snackbar.setCallback(mCallback);
        boolean first = mSnackbarList.size() == 0;
        mSnackbarList.add(snackbar);
        if (first)
            displaySnackbar(snackbar);
    }

    public void displaySnackbar(Snackbar snackbar) {
        snackbar.show();
    }
}