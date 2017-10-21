package com.example.ateg.intentexperiments;

import android.app.Fragment;

/**
 * Created by ATeg on 10/20/2017.
 */

public class MainActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
}
