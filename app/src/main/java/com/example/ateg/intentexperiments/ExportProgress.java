package com.example.ateg.intentexperiments;

/**
 * Created by ATeg on 10/31/2017.
 */

interface ExportProgress {
    public void updateExportProgress(int current, int total);
    public void exportDone();
    public void exportError(Throwable throwable);
}
