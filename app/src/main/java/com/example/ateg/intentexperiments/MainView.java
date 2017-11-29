package com.example.ateg.intentexperiments;

import java.io.File;

/**
 * Created by ATeg on 10/20/2017.
 */

public interface MainView extends LEView {
    void updatePreferences(Preferences preferences);

    void populateMainView(String stringifiedIntent);

    void announceExportComplete(File logFileWritten);

    void examineDone();

    void showUpdate(int updateMessageStringId);
}
