package com.example.ateg.intentexperiments.FileSelector;

/**
 * Created by ATeg on 10/26/2017.
 */

public class Preferences {

    private boolean autoExamine;
    private boolean autoSave;
    private boolean showExamineButton;
    private String defaultFileName;

    public boolean isAutoExamine() {
        return autoExamine;
    }

    public void setAutoExamine(boolean autoExamine) {
        this.autoExamine = autoExamine;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public boolean isShowExamineButton() {
        return showExamineButton;
    }

    public void setShowExamineButton(boolean showExamineButton) {
        this.showExamineButton = showExamineButton;
    }

    public String getDefaultFileName() {
        return defaultFileName;
    }

    public void setDefaultFileName(String defaultFileName) {
        this.defaultFileName = defaultFileName;
    }
}
