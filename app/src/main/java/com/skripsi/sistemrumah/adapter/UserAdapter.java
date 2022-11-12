package com.skripsi.sistemrumah.adapter;

import static androidx.core.content.ContextCompat.startActivity;
import static com.skripsi.sistemrumah.framework.ActivityFramework.preventMultiClick;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.api.GetDataUser;
import com.skripsi.sistemrumah.api.MultiResponse;
import com.skripsi.sistemrumah.api.rest.REST_Controller;
import com.skripsi.sistemrumah.ui.DaftarActivity;
import com.skripsi.sistemrumah.ui.EditUserActivity;
import com.skripsi.sistemrumah.ui.LoginActivity;
import com.skripsi.sistemrumah.ui.MainMenuActivity;
import com.skripsi.sistemrumah.ui.UserTerdaftarActivity;
import com.skripsi.sistemrumah.utils.UtilsDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    private static final String TAG = "UserAdapter";
    List<GetDataUser> mGetDataUser;
    private ProgressDialog mProgressDialog;

    public UserAdapter(List <GetDataUser> tokoList) {
        mGetDataUser = tokoList;
    }

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_adapter, parent, false);
        UserAdapter.MyViewHolder mViewHolder = new UserAdapter.MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder (UserAdapter.MyViewHolder holder, final int position){

        holder.tvNama.setText(mGetDataUser.get(position).getName());
        holder.tvUsername.setText(mGetDataUser.get(position).getUsername());

    }

    @Override
    public int getItemCount () {
        return mGetDataUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama, tvUsername;
        public ImageButton btnDelete, btnEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            btnDelete.setOnClickListener(view -> onDeleteUser(mGetDataUser.get(getAdapterPosition()), view, getAdapterPosition()));
            btnEdit.setOnClickListener(view -> onEditUser(mGetDataUser.get(getAdapterPosition()), view, getAdapterPosition()));

        }
    }

    private void onEditUser(GetDataUser dataUser, View view, int adapterPosition) {
        preventMultiClick(view);
        UserTerdaftarActivity mActivity = (UserTerdaftarActivity) view.getContext();
        Intent toEditUser = new Intent(mActivity, EditUserActivity.class);
        Bundle bundleUser = new Bundle();
        bundleUser.putSerializable(EditUserActivity.FROM_DATA, dataUser);
        toEditUser.putExtras(bundleUser);
        mActivity.startActivity(toEditUser);
    }

    private void onDeleteUser(GetDataUser data, View view, int position) {
        preventMultiClick(view);
        showMessage(data, view.getContext(), position);
    }

    public void showMessage(GetDataUser data, Context mContext, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle("Anda yakin?");
        builder.setMessage("Tekan 'Iya, Hapus' untuk menghapus " + data.getName() + "?");
        builder.setNegativeButton("Tidak", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Iya, Hapus", (dialogInterface, i) -> requestDeleteUser(data, mContext, position));
        builder.show();
    }

    private void requestDeleteUser(GetDataUser user, Context mContext, int position) {
        final Map<String, RequestBody> data = new HashMap<>();
        data.put("id_user", RequestBody.create(MediaType.parse("text/plain"), user.getId_user()));
        data.put("name", RequestBody.create(MediaType.parse("text/plain"), user.getName()));
        data.put("username", RequestBody.create(MediaType.parse("text/plain"), user.getUsername()));
        data.put("password", RequestBody.create(MediaType.parse("text/plain"), user.getPassword()));

        UserTerdaftarActivity mActivity = (UserTerdaftarActivity) mContext;
        mProgressDialog = UtilsDialog.showLoading(mActivity, mProgressDialog);

        REST_Controller.CLIENT.removeUser(user.getId_user(), data).enqueue(new Callback<MultiResponse>() {
            @Override
            public void onResponse(Call<MultiResponse> call, Response<MultiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    if (response.body().getSeverity().equals("success")) {

                        Log.d(TAG, "onResponse: BERHASIL");
                        UtilsDialog.dismissLoading(mProgressDialog);
                        UtilsDialog.showBasicDialog(mActivity, "OK", response.body().getMessage()).show();
                        mActivity.loadData();
                        mGetDataUser.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mGetDataUser.size());
                        return;
                    }

                    UtilsDialog.dismissLoading(mProgressDialog);

                } else {
                    UtilsDialog.showBasicDialog(mActivity, "OK", response.errorBody().toString()).show();
                }
            }

            @Override
            public void onFailure(Call<MultiResponse> call, Throwable t) {
                UtilsDialog.dismissLoading(mProgressDialog);
                UtilsDialog.showBasicDialog(mActivity, "OK", t.toString()).show();
            }
        });
    }
}