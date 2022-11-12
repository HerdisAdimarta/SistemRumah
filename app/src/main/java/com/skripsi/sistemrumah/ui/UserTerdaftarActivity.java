package com.skripsi.sistemrumah.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.adapter.UserAdapter;
import com.skripsi.sistemrumah.api.ApiListUser;
import com.skripsi.sistemrumah.api.GetDataUser;
import com.skripsi.sistemrumah.api.rest.REST_Controller;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTerdaftarActivity extends ActivityFramework {

    private ProgressDialog mProgressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @BindView(R.id.rvListUser)
    RecyclerView rvListUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_terdaftar);
        ButterKnife.bind(this);
        mLayoutManager = new LinearLayoutManager(this);
        rvListUser.setLayoutManager(mLayoutManager);

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @OnClick(R.id.btnAddUser)
    public void btnAddUser(View view) {
        preventMultiClick(view);
        Intent toDaftarUser = new Intent(mActivity, DaftarActivity.class);
        toDaftarUser.putExtra(DaftarActivity.FROM_REGISTER, false);
        startActivity(toDaftarUser);
    }

    @OnClick(R.id.btnBack)
    public void btnBack(View view) {
        preventMultiClick(view);
        startActivity(new Intent(mActivity, MainMenuActivity.class));
        finish();

    }

    public void loadData() {

        mProgressDialog = UtilsDialog.showLoading(UserTerdaftarActivity.this, mProgressDialog);

        REST_Controller.CLIENT.getDataUser().enqueue(new Callback<ApiListUser>() {
            @Override
            public void onResponse(Call<ApiListUser> call, Response<ApiListUser> response) {
                UtilsDialog.dismissLoading(mProgressDialog);
                if (response.isSuccessful()) {
                    Log.e("masuk", "0");
                    if (!response.body().getSeverity().equals("success")) {
                        Log.e("masuk", "1");
                        UtilsDialog.dismissLoading(mProgressDialog);
                        UtilsDialog.showBasicDialog(UserTerdaftarActivity.this, "OK", response.body().getMessage()).show();
                        return;
                    }
                    Log.e("masuk", "2");
                    List<GetDataUser> kartuList = response.body().getAffected();
                    Log.d("Retrofit Get", "Jumlah data Kartu: " +
                            String.valueOf(kartuList.size()));

                    mAdapter = new UserAdapter(kartuList);
                    rvListUser.setAdapter(mAdapter);


                } else {
                    Log.e("masuk", "3");
                    UtilsDialog.showBasicDialog(mActivity, "OK", response.errorBody().toString()).show();
                }
            }

            @Override
            public void onFailure(Call<ApiListUser> call, Throwable t) {
                Log.e("masuk", "4");
                UtilsDialog.dismissLoading(mProgressDialog);
                UtilsDialog.showBasicDialog(UserTerdaftarActivity.this, "OK", t.toString()).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mActivity, MainMenuActivity.class);
        startActivity(intent);
        finish();

    }
}