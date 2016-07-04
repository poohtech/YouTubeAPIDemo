package com.esp.testvideo.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;

import com.esp.testvideo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 4/2/16.
 */
public class UtilsFunc {
    public static String millisToDate(long millis, String format) {
        return new SimpleDateFormat(format).format(new Date(millis));
    }

    public static Date convertStringToDate(String strDate, String parseFormat) {
        try {
            if (strDate == null || strDate.equals(""))
                return null;
            return new SimpleDateFormat(parseFormat).parse(strDate);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertDateToString(Date strDate, String parseFormat) {
        try {
            if (strDate == null || strDate.equals(""))
                return null;
            return new SimpleDateFormat(parseFormat).format(strDate);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (Exception e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }
    public static boolean isOnline(Context context) {
        try {
            if (context == null)
                return false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (cm.getActiveNetworkInfo() != null) {
                    return cm.getActiveNetworkInfo().isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.error("Exception", e);
            return false;
        }
    }

}
