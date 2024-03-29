package com.skripsi.sistemrumah.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.api.GetDataMonitoring;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    List<GetDataMonitoring> mGetKartu;

    public MyAdapter(List <GetDataMonitoring> tokoList) {
        mGetKartu = tokoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_adapter, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder (MyViewHolder holder,final int position){

        holder.tvIdTag.setText(mGetKartu.get(position).getId_tag());
        holder.tvAction.setText(mGetKartu.get(position).getAction());
        holder.tvDateTime.setText(mGetKartu.get(position).getDatetime());

    }

    @Override
    public int getItemCount () {
        return mGetKartu.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIdTag, tvAction, tvDateTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvIdTag = itemView.findViewById(R.id.tvIdTag);
            tvAction = itemView.findViewById(R.id.tvAction);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }
    }
}