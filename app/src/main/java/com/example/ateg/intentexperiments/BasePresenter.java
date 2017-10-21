package com.example.ateg.intentexperiments;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract presenter that provides the view to the specific presenters.
 */
public class BasePresenter<T> {

    private List<AsyncTask> networkCalls;

    private T mViewInstance;

    public BasePresenter(T viewInstance) {
        this.mViewInstance = viewInstance;
    }

    protected T getView() {
        return mViewInstance;
    }

    public void detachView() {
        mViewInstance = null;
    }

    public void attachView(T viewInstance){
        this.mViewInstance = viewInstance;
    }
}