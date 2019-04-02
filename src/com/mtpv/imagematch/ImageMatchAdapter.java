package com.mtpv.imagematch;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mtpv.mobilee_ticket.R;

import java.util.ArrayList;

public class ImageMatchAdapter extends BaseAdapter {

    ArrayList<Telangana> telanganaArrayList;
    Context context;
    private LayoutInflater inflater;


    public ImageMatchAdapter(Context context, ArrayList<Telangana> telanganaArrayList) {
        this.context=context;
        this.telanganaArrayList=telanganaArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return telanganaArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return telanganaArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_imgmatch, null);
            holder = new Holder();
            holder.txt_MatchPer =view.findViewById(R.id.txt_MatchPer);
            holder.txt_Name =view.findViewById(R.id.txt_Name);
            holder.txt_FName =view.findViewById(R.id.txt_FName);
            holder.txt_Age =view.findViewById(R.id.txt_Age);
            holder.txt_FirNo =view.findViewById(R.id.txt_FirNo);
            holder.imgMatchFrmServer =view.findViewById(R.id.imgMatchFrmServer);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.txt_MatchPer.setText(telanganaArrayList.get(position).getConf());
        holder.txt_Name.setText(telanganaArrayList.get(position).getName());
        holder.txt_FName.setText(telanganaArrayList.get(position).getFather_name());
        holder.txt_Age.setText(telanganaArrayList.get(position).getAge());
        holder.txt_FirNo.setText(telanganaArrayList.get(position).getFir_no());
        Glide.with(context).load(telanganaArrayList.get(position).getImage()).into(holder.imgMatchFrmServer);
        return view;
    }

    private class Holder {
        AppCompatTextView txt_MatchPer,txt_Name,txt_FName,txt_Age,txt_FirNo;
        AppCompatImageView imgMatchFrmServer;
    }
}
