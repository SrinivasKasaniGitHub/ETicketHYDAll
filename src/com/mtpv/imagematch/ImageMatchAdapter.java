package com.mtpv.imagematch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mtpv.mobilee_ticket.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ImageMatchAdapter extends BaseAdapter {

    ArrayList<Telangana> telanganaArrayList;
    ArrayList<ImageModel> imageModelArrayList;
    Context context;
    private LayoutInflater inflater;


    public ImageMatchAdapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale", "ViewHolder"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;

        View view = null;
        //if (view == null) {
        view = inflater.inflate(R.layout.list_imgmatch, null);
        holder = new Holder();
        holder.txt_MatchPer = view.findViewById(R.id.txt_MatchPer);
        holder.txt_Name = view.findViewById(R.id.txt_Name);
        holder.txt_FName = view.findViewById(R.id.txt_FName);
        holder.txt_Age = view.findViewById(R.id.txt_Age);
        holder.txt_FirNo = view.findViewById(R.id.txt_FirNo);
        holder.imgMatchFrmServer = view.findViewById(R.id.imgMatchFrmServer);
        holder.cb_Img = view.findViewById(R.id.cb_Img);
       /*     view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }*/

        final ImageModel imageModel = imageModelArrayList.get(position);

        Double percentage = imageModel.getConf();
        String per = String.format("%.2f", percentage);
        holder.txt_MatchPer.setText(Html.fromHtml("Matching Percentage : " + "<font color=#e40404>" + per + "</font>"));
        holder.txt_Name.setText(": " + imageModel.getDriverName());
        holder.txt_FName.setText(": " + imageModel.getDriverName());
        holder.txt_Age.setText(": " + imageModel.getChallanNo());
        holder.txt_FirNo.setText(": " + imageModel.getPsName());
        Glide.with(context).load(imageModel.getDriverBase64ImgData()).into(holder.imgMatchFrmServer);
        if (imageModel.isSelected()) {
            holder.cb_Img.setChecked(true);
        } else {
            holder.cb_Img.setChecked(false);
        }
        holder.cb_Img.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageModel.setSelected(true);
                } else {
                    imageModel.setSelected(false);
                }
            }
        });

       /* final String pureBase64Encoded = imageModelArrayList.get(position).getDriverBase64ImgData().substring(imageModelArrayList.get(position).getDriverBase64ImgData().indexOf(",")  + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.imgMatchFrmServer.setImageBitmap(decodedImage);*/

        return view;
    }

    private class Holder {
        AppCompatTextView txt_MatchPer, txt_Name, txt_FName, txt_Age, txt_FirNo;
        AppCompatImageView imgMatchFrmServer;
        CheckBox cb_Img;
    }
}
