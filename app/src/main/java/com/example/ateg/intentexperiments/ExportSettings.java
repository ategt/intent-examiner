package com.example.ateg.intentexperiments;

import java.io.File;

/**
 * Created by ATeg on 11/1/2017.
 */

public class ExportSettings {
    private boolean markArchived;

    private Format format;
    private Scope scope;
    private Destination destination;
    private File file;

    public boolean isMarkArchived() {
        return markArchived;
    }

    public void setMarkArchived(boolean markArchived) {
        this.markArchived = markArchived;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean validate() {
        return format != null && scope != null && destination != null;
    }


    public enum Format {
        TEXT,
        JSON;
    }

    public enum Scope {
        FULL,
        DIFF,
        SINGLE;
    }

    public enum Destination {
        LOCAL,
        SEND;
    }
}
