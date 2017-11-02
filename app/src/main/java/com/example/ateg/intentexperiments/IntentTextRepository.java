package com.example.ateg.intentexperiments;

import java.util.List;

/**
 * Created by ATeg on 11/1/2017.
 */

public class IntentTextRepository implements IntentRepository {

    LoggingUtilities loggingUtilities;

    public IntentTextRepository(LoggingUtilities loggingUtilities) {
        this.loggingUtilities = loggingUtilities;
    }

    @Override
    public List<IntentWrapper> create(List<IntentWrapper> intentWrapperList) {
        ExamineServices examineServices = new ExamineServices();

        StringBuilder stringBuilder = new StringBuilder();

        for (IntentWrapper intentWrapper : intentWrapperList) {
            stringBuilder.append(examineServices.examineIntent(intentWrapper));
        }

        boolean success = loggingUtilities.updateTextFile(stringBuilder.toString());

        if (success) {
            return intentWrapperList;
        } else
            throw new LoggingUtilitiesException("Intent Text Repository Had Problems Creating The File.\n\t"
                    + loggingUtilities.getLogFile().getAbsolutePath());
    }

    @Override
    public IntentWrapper create(IntentWrapper intentWrapper) {
        ExamineServices examineServices = new ExamineServices();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(examineServices.examineIntent(intentWrapper));

        boolean success = loggingUtilities.updateTextFile(stringBuilder.toString());

        if (success) {
            return intentWrapper;
        } else
            throw new LoggingUtilitiesException("Intent Text Repository Had Problems Creating The File.\n\t"
                    + loggingUtilities.getLogFile().getAbsolutePath());
    }

    @Override
    public List<IntentWrapper> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<IntentWrapper> getDifferential() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void markAllArchived() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }
}
