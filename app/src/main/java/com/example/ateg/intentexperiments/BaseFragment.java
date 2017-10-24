package com.example.ateg.intentexperiments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Custom Fragment implementation to bind basic elements and force the use of
 * the MVP pattern logically attaching a presenter to this Fragment.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    private static final String TAG = "BaseFragment";
    protected T mPresenter;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId = layout();
        Log.d(TAG, "Loading Fragment Layout. -" + layoutId + "- " +
                getResources().getResourceEntryName(layoutId) + ":" +
                getResources().getResourceName(layoutId) + " - " +
                getResources().getResourceTypeName(layoutId));
        v = inflater.inflate(layoutId, container, false);
        Log.d(TAG, "Done loading " + layoutId);
        mPresenter = createPresenter();
        setUi(v);
        init();
        populate();
        setListeners();
        return v;
    }

    protected View getCreatedView() {
        return v;
    }

    /**
     * Returns the layout id for the inflater so the view can be populated
     */
    protected abstract int layout();

    /**
     * Loads the view elements for the fragment
     */
    protected abstract void setUi(View v);

    /**
     * Initializes any variables that the fragment needs
     */
    protected abstract void init();

    /**
     * Populates the view elements of the fragment
     */
    protected abstract void populate();

    /**
     * Sets the listeners for the views of the fragment
     */
    protected abstract void setListeners();

    /**
     * Create the presenter for this fragment
     */
    protected abstract T createPresenter();

}
