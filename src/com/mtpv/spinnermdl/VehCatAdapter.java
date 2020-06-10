package com.mtpv.spinnermdl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtpv.mobilee_ticket.R;

import java.util.List;

public class VehCatAdapter extends BaseAdapter {
    public Context context;
    private static LayoutInflater inflater = null;
    List<VehCatModel> vehCatModels;
    String title;

    public VehCatAdapter(Context context, List<VehCatModel> vehCatModels) {
        this.context = context;
        this.vehCatModels = vehCatModels;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vehCatModels.size();
    }

    @Override
    public Object getItem(int i) {
        return vehCatModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;

        view = inflater.inflate(R.layout.single_text_row_item, null);
        TextView txt_Title = view.findViewById(R.id.value);
        txt_Title.setText("" + vehCatModels.get(i).getVehCatName());

        return view;
    }


}