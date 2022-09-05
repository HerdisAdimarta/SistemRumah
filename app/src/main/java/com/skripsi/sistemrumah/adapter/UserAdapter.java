package com.skripsi.sistemrumah.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.api.GetDataUser;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    List<GetDataUser> mGetDataUser;

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

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }
}