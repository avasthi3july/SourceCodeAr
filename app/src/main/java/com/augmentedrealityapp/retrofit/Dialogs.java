package com.augmentedrealityapp.retrofit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.view.Window;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.delegates.OnRetryCallback;


/**
 * Created by prasharma on 3/24/2017.
 */

public class Dialogs {

    private static ProgressDialog progressDialog;

    /**
     * Method to show progress dialog
     *
     * @param context
     * @param message
     */
    public static void showProgressDialog(Context context, String message) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }


    /**
     * Method to hide progress dialog
     *
     * @param activity
     */
    public static void hideProgressDialog(Activity activity) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                    progressDialog = null;
                }
            }
        });

    }


    /**
     * Method to display a alert dialog with ok
     *
     * @param mContext
     * @param message
     */
    public static void showAlert(Context mContext, String message) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
//        builder.setTitle(mContext.getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton(mContext.getString(R.string.TAG_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }


    /**
     * Method to show try again dialog
     *
     * @param mContext
     * @param message
     */
    public static void showTryAgainDialog(final Context mContext, String message, final OnRetryCallback mOnRetryCallback) {
        final long[] mLastClickTime = {0};
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setMessage(message);
        builder.setPositiveButton(mContext.getString(R.string.TAG_YES), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) {
                    return;
                }
                mLastClickTime[0] = SystemClock.elapsedRealtime();
                dialog.dismiss();
                mOnRetryCallback.OnRetry(true);
            }
        });

        builder.setNegativeButton(mContext.getString(R.string.TAG_CANCEL), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mOnRetryCallback.OnRetry(false);
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


}
