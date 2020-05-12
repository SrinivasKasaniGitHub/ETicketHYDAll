package com.mtpv.covid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mtpv.mobilee_ticket.R;

import java.util.List;

public class ChallansAdapter extends BaseAdapter {
    public Context context;
    private static LayoutInflater inflater = null;
    List<ChallansModel> list_VltnModel;
    String title;

    public ChallansAdapter(Context context, List<ChallansModel> list_VltnModel) {
        this.context = context;
        this.list_VltnModel = list_VltnModel;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_VltnModel.size();
    }

    @Override
    public Object getItem(int i) {
        return list_VltnModel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;

        view = inflater.inflate(R.layout.cvpendindclns, null);
        TextView txt_ClnNO = view.findViewById(R.id.txt_ClnNO);
        TextView txt_Dtae = view.findViewById(R.id.txt_Dtae);
        TextView txt_Lctn = view.findViewById(R.id.txt_Lctn);
        TextView txt_PSName = view.findViewById(R.id.txt_PSName);
        TextView txt_FAmnt = view.findViewById(R.id.txt_FAmnt);

        txt_ClnNO.setText("" +list_VltnModel.get(i).getChallanNo());
        txt_Dtae.setText(""+list_VltnModel.get(i).getDate());
        txt_Lctn.setText("" +list_VltnModel.get(i).getLocation());
        txt_PSName.setText(""+list_VltnModel.get(i).getPSName());
        txt_FAmnt.setText("" +list_VltnModel.get(i).getFINE());

        return view;
    }
}