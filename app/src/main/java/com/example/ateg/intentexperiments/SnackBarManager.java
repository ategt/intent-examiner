protected ArrayList<Snackbar> mSnackbarList = new ArrayList<>();

protected Snackbar.Callback mCallback = new Snackbar.Callback() {
    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        mSnackbarList.remove(snackbar);
        if (mSnackbarList.size() > 0)
           displaySnackbar(mSnackbarList.get(0));
    }
};

public void addQueue(Snackbar snackbar){
    setLayoutParams(snackbar);
    snackbar.setCallback(mCallback);
    boolean first = mSnackbarList.size() == 0;
    mSnackbarList.add(snackbar);
    if(first)
        displaySnackbar(snackbar);
}

public void displaySnackbar(Snackbar snackbar){
    snackbar.show();
}