package com.skripsi.sistemrumah.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.api.GetDataUser;
import com.skripsi.sistemrumah.api.MultiResponse;
import com.skripsi.sistemrumah.api.rest.REST_Controller;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends ActivityFramework {

    public static final String FROM_DATA = "data_user";
    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;

    private ProgressDialog mProgressDialog;
    private GetDataUser dataUser;

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
        updateUsers();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        ButterKnife.bind(this);
        initPreparation();
    }

    private void initPreparation() {
        try {
            dataUser = (GetDataUser) getIntent().getSerializableExtra(FROM_DATA);
            etNama.setText(dataUser.getName());
            etPassword.setText(dataUser.getPassword());
            etUsername.setText(dataUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUsers() {
        String mEtNama = etNama.getText().toString();
        String mEtUserName = etUsername.getText().toString();
        String mEtPass = etPassword.getText().toString();

        final Map<String, RequestBody> data = new HashMap<>();
        data.put("id_user", RequestBody.create(MediaType.parse("text/plain"), dataUser.getId_user()));
        data.put("name", RequestBody.create(MediaType.parse("text/plain"), mEtNama));
        data.put("username", RequestBody.create(MediaType.parse("text/plain"), mEtUserName));
        data.put("password", RequestBody.create(MediaType.parse("text/plain"), mEtPass));

        mProgressDialog = UtilsDialog.showLoading(EditUserActivity.this, mProgressDialog);

        REST_Controller.CLIENT.editUser(dataUser.getId_user() ,data).enqueue(new Callback<MultiResponse>() {
            @Override
            public void onResponse(Call<MultiResponse> call, Response<MultiResponse> response) {
                UtilsDialog.dismissLoading(mProgressDialog);
                if (response.isSuccessful()) {
                    if (!response.body().getSeverity().equals("success")) {
                        UtilsDialog.dismissLoading(mProgressDialog);
                        UtilsDialog.showBasicDialog(EditUserActivity.this, "OK", response.body().getMessage()).show();
                        return;
                    }

                    UtilsDialog.dismissLoading(mProgressDialog);
                    onBackPressed();
                } else {
                    UtilsDialog.showBasicDialog(mActivity, "OK", response.errorBody().toString()).show();
                }
            }

            @Override
            public void onFailure(Call<MultiResponse> call, Throwable t) {
                UtilsDialog.dismissLoading(mProgressDialog);
                UtilsDialog.showBasicDialog(EditUserActivity.this, "OK", t.toString()).show();
            }
        });

    }
}