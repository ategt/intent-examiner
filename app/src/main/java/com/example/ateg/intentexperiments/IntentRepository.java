package com.example.ateg.intentexperiments;

import java.util.List;

/**
 * Created by ATeg on 11/1/2017.
 */

interface IntentRepository {
    List<IntentWrapper> create(List<IntentWrapper> intentWrapperList);

    IntentWrapper create(IntentWrapper intentWrapper);

    List<IntentWrapper> getAll();

    List<IntentWrapper> getDifferential();

    void markAllArchived();

    void deleteAll();
}
