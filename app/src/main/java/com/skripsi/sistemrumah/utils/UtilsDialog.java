package com.skripsi.sistemrumah.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.skripsi.sistemrumah.R;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UtilsDialog {

    public static String checkErrorMessage(Activity act, String msg) {


        if (msg.contains("Failed to connect")) {
            return act.getString(R.string.check_connection);
        }

        if (msg.contains("Read error: ssl")) {
            return act.getString(R.string.check_connection);
        }

        if (msg.contains("Unable to resolve host")) {
            return act.getString(R.string.check_connection);
        }

        if (msg.contains("SocketException")) {
            return act.getString(R.string.check_connection);
        }

        if (msg.contains("UnknownHostException")) {
            return act.getString(R.string.check_connection);
        }

        if (msg.contains("EOFException")) {
            return act.getString(R.string.check_server_connection);
        }

        if (msg.contains("ConnectException")) {
            return act.getString(R.string.check_server_connection);
        }
        if (msg.contains("SSLHandshakeException")) {
            return act.getString(R.string.check_connection);
        }
        if (msg.contains("SocketTimeoutException")) {
            return act.getString(R.string.check_connection);
        }
        return msg;
    }

    public static void dismissLoading(ProgressDialog pDialog) {
        if (pDialog != null && pDialog.isShowing()) {
            try {
                pDialog.dismiss();
            } catch (Exception e) {

            }
        }
    }

    public static AlertDialog showBasicDialog(final Activity act, String positive_name, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(act);
        try {
            msg = checkErrorMessage(act, msg);
            builder.setMessage(Html.fromHtml(msg))
                    .setCancelable(false)
                    .setPositiveButton(positive_name,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();

                                }
                            });
            final AlertDialog a = builder.create();
            if (positive_name.toLowerCase().equals("error") || positive_name.toLowerCase().equals("mock detected")) {
                a.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        a.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(act.getResources().getColor(R.color.colorBlack));
                    }
                });
            }

            return a;
        }
        catch (Exception e) {

        }

        return null;
    }

    public static ProgressDialog showLoading(Activity act, ProgressDialog pDialog) {
        try {
            if (pDialog != null) {
                pDialog.dismiss();
            }
            pDialog = new ProgressDialog(act);
            pDialog.setTitle("Processing");
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.show();
            return pDialog;
        }
        catch (Exception e) {

        }
        return null;
    }

}