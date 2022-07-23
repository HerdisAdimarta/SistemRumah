package com.skripsi.sistemrumah.ui;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.api.RegisterResponse;
import com.skripsi.sistemrumah.api.rest.REST_Controller;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.nfc.Utils;
import com.skripsi.sistemrumah.nfc.parser.NdefMessageParser;
import com.skripsi.sistemrumah.nfc.record.ParsedNdefRecord;
import com.skripsi.sistemrumah.storage.SharedPreferencesProvider;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarKartuActivity extends ActivityFramework {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.etIdKartu)
    EditText etIdKartu;
    @BindView(R.id.cbLokerA)
    CheckBox cbLokerA;
    @BindView(R.id.cbLokerB)
    CheckBox cbLokerB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kartu);
        ButterKnife.bind(this);
        checkBox();
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

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(Utils.toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(Utils.toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(Utils.toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(Utils.toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String setDataId(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        etIdKartu.setText(sb.append(Utils.toDec(id)).append('\n'));

        return sb.toString();
    }

    private void checkBox() {
        cbLokerA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesProvider.getInstance().set_pref_type(mActivity, "0");
                    cbLokerB.setChecked(false);
                }
            }
        });
        cbLokerB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesProvider.getInstance().set_pref_type(mActivity, "1");
                    cbLokerA.setChecked(false);
                }
            }
        });

    }

    @OnClick(R.id.btnDaftar)
    public void btnDaftar(View view) {
        preventMultiClick(view);
        if (etIdKartu.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Tap Kartu Dengan Smartphnone Anda").show();
            return;
        }

        if (!cbLokerA.isChecked()&&!cbLokerB.isChecked()) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Pilih Tipe Kartu").show();
            return;
        }
        registerKartu();

    }

    private void registerKartu() {
        String mEtIdKartu = etIdKartu.getText().toString();
        String mTipeKartu = SharedPreferencesProvider.getInstance().get_pref_type(mActivity);

        final Map<String, RequestBody> data = new HashMap<>();
        data.put("id_tag", RequestBody.create(MediaType.parse("text/plain"), mEtIdKartu));
        data.put("type", RequestBody.create(MediaType.parse("text/plain"), mTipeKartu));

        mProgressDialog = UtilsDialog.showLoading(DaftarKartuActivity.this, mProgressDialog);

        REST_Controller.CLIENT.getDaftarKartu(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                UtilsDialog.dismissLoading(mProgressDialog);
                if (response.isSuccessful()) {
                    if (!response.body().getSeverity().equals("success")) {
                        UtilsDialog.dismissLoading(mProgressDialog);
                        UtilsDialog.showBasicDialog(DaftarKartuActivity.this, "OK", response.body().getMessage()).show();
                        return;
                    }

                    UtilsDialog.showBasicDialog(DaftarKartuActivity.this, "OK", "Daftar Kartu Berhasil").show();
                    startActivity(new Intent(mActivity, MainMenuActivity.class));
                    finish();
                } else {
                    UtilsDialog.showBasicDialog(mActivity, "OK", response.errorBody().toString()).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                UtilsDialog.dismissLoading(mProgressDialog);
                UtilsDialog.showBasicDialog(DaftarKartuActivity.this, "OK", t.toString()).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}
