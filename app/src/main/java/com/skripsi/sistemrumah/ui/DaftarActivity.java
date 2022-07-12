package com.skripsi.sistemrumah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarActivity extends ActivityFramework {

    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @OnClick(R.id.btnSimpan)
    public void btnSimpan(View view) {
        preventMultiClick(view);
        if (etNama.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Nama harus diisi").show();
            return;
        }

        if (etUsername.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Username harus diisi").show();
            return;
        }

        if (etPassword.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Password harus diisi").show();
            return;
        }

//        login();
        startActivity(new Intent(mActivity, LoginActivity.class));
        finish();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mActivity, LoginActivity.class));
        finish();
    }

}