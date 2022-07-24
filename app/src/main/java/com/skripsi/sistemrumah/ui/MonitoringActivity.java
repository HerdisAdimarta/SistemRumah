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

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        ButterKnife.bind(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//                byte[] payload = dumpTagData(tag).getBytes();
                byte[] payload = setDataId(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            displayMsgs(msgs);
        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    private String setDataId(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        monitoring();
        return sb.toString();
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