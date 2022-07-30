package com.skripsi.sistemrumah.ui;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.api.MultiResponse;
import com.skripsi.sistemrumah.api.rest.REST_Controller;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.nfc.Utils;
import com.skripsi.sistemrumah.nfc.parser.NdefMessageParser;
import com.skripsi.sistemrumah.nfc.record.ParsedNdefRecord;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringActivity extends ActivityFramework {

    private ProgressDialog mProgressDialog;

    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.tvStatus)
    public void tvStatus(View view) {
        preventMultiClick(view);
        monitoring();
    }

    @OnClick(R.id.btnBack)
    public void btnBack(View view) {
        preventMultiClick(view);
        startActivity(new Intent(mActivity, MainMenuActivity.class));
        finish();

    }

    private void monitoring() {

        mProgressDialog = UtilsDialog.showLoading(MonitoringActivity.this, mProgressDialog);

        REST_Controller.CLIENT.sendDataMonitoring().enqueue(new Callback<MultiResponse>() {
            @Override
            public void onResponse(Call<MultiResponse> call, Response<MultiResponse> response) {
                UtilsDialog.dismissLoading(mProgressDialog);
                if (response.isSuccessful()) {
                    Log.e("masuk", "0");
                    if (!response.body().getSeverity().equals("success")) {
                        Log.e("masuk", "1");
                        UtilsDialog.dismissLoading(mProgressDialog);
                        UtilsDialog.showBasicDialog(MonitoringActivity.this, "OK", response.body().getMessage()).show();
                        return;
                    }
                    Log.e("masuk", "2");
                    tvStatus.setText(response.body().getMessage());


                } else {
                    Log.e("masuk", "3");
                    UtilsDialog.showBasicDialog(mActivity, "OK", response.errorBody().toString()).show();
                }
            }

            @Override
            public void onFailure(Call<MultiResponse> call, Throwable t) {
                Log.e("masuk", "4");
                UtilsDialog.dismissLoading(mProgressDialog);
                tvStatus.setText("history loker berhasil terkirim");
//                UtilsDialog.showBasicDialog(MonitoringActivity.this, "OK", t.toString()).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}