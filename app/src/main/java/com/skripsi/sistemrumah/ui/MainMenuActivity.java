package com.skripsi.sistemrumah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.storage.SharedPreferencesProvider;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends ActivityFramework {
    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        setUser();
    }

    @OnClick(R.id.llMain2)
    public void llMain2(View view) {
        preventMultiClick(view);
        startActivity(new Intent(mActivity, DaftarKartuActivity.class));
    }

    private void setUser() {
        tvUserName.setText(SharedPreferencesProvider.getInstance().get_pref_user_name(mActivity));
    }

    @Override
    public void onBackPressed() {

    }

}