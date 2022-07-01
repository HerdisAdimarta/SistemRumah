package com.skripsi.sistemrumah.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.storage.SharedPreferencesProvider;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends ActivityFramework {
    @BindView(R.id.btLogin)
    Button btLogin;

    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etEmail)
    EditText etEmail;

    private ProgressDialog mProgressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+.[a-z]+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferencesProvider.getInstance().clearSession(mActivity);
        etPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    @OnClick(R.id.btLogin)
    public void btLogin(View view) {
        preventMultiClick(view);
        if (etEmail.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Alamat email harus diisi").show();
            return;
        } else {
            if (etEmail.getText().toString().trim().matches(emailPattern)) {
            } else {
                UtilsDialog.showBasicDialog(mActivity, "OK", "Format email salah").show();
                return;
            }
        }

        if (etPassword.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Password harus diisi").show();
            return;
        }
        if (etPassword.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Konfirmasi Password harus diisi").show();
            return;
        }
//        login();
        startActivity(new Intent(mActivity, MainMenuActivity.class));
        finish();


    }

    private void login() {
        String mEtEmail = etEmail.getText().toString();
        String mEtPassword = etPassword.getText().toString();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", mEtEmail);
        data.put("password", mEtPassword);

        mProgressDialog = UtilsDialog.showLoading(LoginActivity.this, mProgressDialog);
        mProgressDialog.show();

//        REST_Controller.CLIENT.postLogin(data).enqueue(new Callback<ApiLogin>() {
//            @Override
//            public void onResponse(Call<ApiLogin> call, Response<ApiLogin> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getError_code() != 0) {
//                        UtilsDialog.dismissLoading(mProgressDialog);
//                        UtilsDialog.showBasicDialog(LoginActivity.this, "OK", response.body().getMessage()).show();
//                        return;
//                    }
//
//                    UtilsDialog.dismissLoading(mProgressDialog);
//                    LoginInfo body = response.body().getProfile_info();
//                    SharedPreferencesProvider.getInstance().set_pref_id_te(mActivity, body.getId_te());
//                    SharedPreferencesProvider.getInstance().set_pref_user_id(mActivity, body.getUser_id());
//                    SharedPreferencesProvider.getInstance().set_pref_user_name(mActivity, body.getNama_pic());
//                    SharedPreferencesProvider.getInstance().set_pref_alamat_toko(mActivity, body.getAlamat());
//                    SharedPreferencesProvider.getInstance().set_pref_nama_toko(mActivity, body.getNama());
//
//                    startActivity(new Intent(mActivity, FindPromoActivity.class));
//                    finish();
//                } else {
//                    ApiBasic error = ErrorUtils.parseError(response, LoginActivity.this);
//                    UtilsDialog.showBasicDialog(LoginActivity.this, "OK", error.getMessage()).show();
//                    UtilsDialog.dismissLoading(mProgressDialog);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiLogin> call, Throwable t) {
//                UtilsDialog.dismissLoading(mProgressDialog);
//                UtilsDialog.showBasicDialog(LoginActivity.this, "OK", ErrorUtils.parseError(t.toString()).getMessage()).show();
//            }
//        });

    }

    private class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

}
