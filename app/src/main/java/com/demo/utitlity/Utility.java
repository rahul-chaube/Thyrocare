package com.demo.utitlity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rahul on 13/9/17.
 */

public class Utility {
    private static ProgressDialog progressBar;
    public static final void createProgressBar(Context context)
    {
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait ");
        progressBar.show();
    }
    public static final void hideProgressBar(Context context)
    {
        progressBar.dismiss();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
