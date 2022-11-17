package com.skripsi.sistemrumah.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.api.LoginResponse;
import com.skripsi.sistemrumah.api.rest.ErrorUtils;
import com.skripsi.sistemrumah.api.rest.REST_Controller;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.storage.SharedPreferencesProvider;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends ActivityFramework {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.btLogin)
    Button btLogin;

    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etUsername)
    EditText etUsername;

    private ProgressDialog mProgressDialog;

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
        if (etUsername.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Username harus diisi").show();
            return;
        }

        if (etPassword.getText().toString().equals("")) {
            UtilsDialog.showBasicDialog(mActivity, "OK", "Password harus diisi").show();
            return;
        }
        login();
//        startActivity(new Intent(mActivity, MainMenuActivity.class));
//        finish();
    }

    @OnClick(R.id.btDaftar)
    public void btDaftar(View view) {
        preventMultiClick(view);
        startActivity(new Intent(mActivity, DaftarActivity.class));
        finish();
    }

    private void login() {
        String mEtUsername = etUsername.getText().toString();
        String mEtPassword = etPassword.getText().toString();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", mEtUsername);
        data.put("password", mEtPassword);

        mProgressDialog = UtilsDialog.showLoading(LoginActivity.this, mProgressDialog);
        mProgressDialog.show();

        REST_Controller.CLIENT.getLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getSeverity().equals("success")) {
                        UtilsDialog.dismissLoading(mProgressDialog);
                        UtilsDialog.showBasicDialog(LoginActivity.this, "OK", response.body().getMessage()).show();
                        return;
                    }

                    UtilsDialog.dismissLoading(mProgressDialog);
//                    Log.d(TAG, "onResponse: " + (response.body().getRole() == 0));
                    SharedPreferencesProvider.getInstance().set_pref_user_name(mActivity, response.body().getUser());
                    SharedPreferencesProvider.getInstance().set_pref_id_user(mActivity, response.body().getIdUser());
                    SharedPreferencesProvider.getInstance().set_pref_is_admin(mActivity, response.body().getRole() == 0);

                    startActivity(new Intent(mActivity, MainMenuActivity.class));
                    finish();
                } else {
                    UtilsDialog.showBasicDialog(LoginActivity.this, "OK", "Username atau Password salah").show();
                    UtilsDialog.dismissLoading(mProgressDialog);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                UtilsDialog.dismissLoading(mProgressDialog);
                UtilsDialog.showBasicDialog(LoginActivity.this, "OK", ErrorUtils.parseError(t.toString()).getMessage()).show();
            }
        });

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
