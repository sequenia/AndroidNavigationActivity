package com.navigationactivity;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

/**
 * Created by chybakut2004 on 04.08.15.
 */
public class FragmentWithProgress extends Fragment {

    private ProgressDialog progressDialog;

    public void showProgress(String title, String message) {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public void dismissProgress() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}