package com.example.ateg.intentexperiments;

/**
 * Created by ATeg on 10/26/2017.
 */

public class Preferences {

    private boolean autoExamine;
    private boolean autoSave;
    private boolean showExamineButton = true;
    private boolean autoLog;
    private boolean clickAnywhere;
    private boolean filterEmpties;
    private String defaultFileName = "IntentExamination.txt";

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

    public boolean isAutoLog() {
        return autoLog;
    }

    public void setAutoLog(boolean autoLog) {
        this.autoLog = autoLog;
    }

    public boolean isClickAnywhere() {
        return clickAnywhere;
    }

    public void setClickAnywhere(boolean clickAnywhere) {
        this.clickAnywhere = clickAnywhere;
    }

    public boolean isFilterEmpties() {
        return filterEmpties;
    }

    public void setFilterEmpties(boolean filterEmpties) {
        this.filterEmpties = filterEmpties;
    }
}
