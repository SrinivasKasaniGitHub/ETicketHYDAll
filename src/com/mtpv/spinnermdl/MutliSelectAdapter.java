package com.mtpv.spinnermdl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mtpv.mobilee_ticket.ExtraPassengers;
import com.mtpv.mobilee_ticket.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Srinivas on 11/14/2018.
 */

public class MutliSelectAdapter extends RecyclerView.Adapter<MutliSelectAdapter.MultiSelectSearchSpinnerDlgViewHolder> {

    private ArrayList<MultiSelectModel> mDataSet = new ArrayList<>();
    private String mSearchQuery = "";
    private Context mContext;
    private AlertDialog alertDialog;
    private AppCompatEditText edt_Txt_ExtraPass;
    private AppCompatButton Btn_ExtraPasSubmit,Btn_ExtraPasCancel;


    MutliSelectAdapter(ArrayList<MultiSelectModel> dataSet, Context context) {
        this.mDataSet = dataSet;
        this.mContext = context;
    }

    @Override
    public MultiSelectSearchSpinnerDlgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_select_item, parent, false);
        return new MultiSelectSearchSpinnerDlgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MultiSelectSearchSpinnerDlgViewHolder holder, int position) {

        if (!mSearchQuery.equals("") && mSearchQuery.length() > 1) {
            setHighlightedText(position, holder.dialog_name_item);
        } else {
            /*String vilatnText = "(" + mDataSet.get(position).getOffence_cd()
                    + ")" + mDataSet.get(position).getVltnSec() + "," + mDataSet.get(position).getVltnDis() +
                    ", " + "<font color='red'>" + mDataSet.get(position).getFine_max() + "</font>";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.dialog_name_item.setText(Html.fromHtml(vilatnText, Html.FROM_HTML_MODE_LEGACY));
            }else {
                holder.dialog_name_item.setText(Html.fromHtml(vilatnText));
            }*/
            holder.dialog_name_item.setText(mDataSet.get(position).getVltnSecName());
        }

        if (mDataSet.get(position).getSelected()) {

            if (!MultiSelectSearchSpinnerDlg.selectedIdsForCallback.contains(mDataSet.get(position).getId())) {
                MultiSelectSearchSpinnerDlg.selectedIdsForCallback.add(mDataSet.get(position).getId());
            }
        }


        if (checkForSelection(mDataSet.get(position).getId())) {
            holder.dialog_item_checkbox.setChecked(true);
        } else {
            holder.dialog_item_checkbox.setChecked(false);
        }

        /*holder.dialog_item_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectSearchSpinnerDlg.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                }
            }
        });*/


        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectSearchSpinnerDlg.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(true);
                    notifyItemChanged(holder.getAdapterPosition());
                    if (mDataSet.get(holder.getAdapterPosition()).getOffence_cd()==7 || mDataSet.get(holder.getAdapterPosition()).getOffence_cd()==9 ){
                        Intent extra_pasnger = new Intent(mContext, ExtraPassengers.class);
                        mContext.startActivity(extra_pasnger);
                    }
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(false);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    private void setHighlightedText(int position, TextView textview) {
        String name = mDataSet.get(position).getVltnSecName();
        SpannableString str = new SpannableString(name);
        int endLength = name.toLowerCase().indexOf(mSearchQuery) + mSearchQuery.length();
        ColorStateList highlightedColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{ContextCompat.getColor(mContext, R.color.colorAccent)});
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.NORMAL, -1, highlightedColor, null);
        str.setSpan(textAppearanceSpan, name.toLowerCase().indexOf(mSearchQuery), endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(str);
    }

    private void removeFromSelection(Integer id) {
        for (int i = 0; i < MultiSelectSearchSpinnerDlg.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectSearchSpinnerDlg.selectedIdsForCallback.get(i))) {
                MultiSelectSearchSpinnerDlg.selectedIdsForCallback.remove(i);
            }
        }
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectSearchSpinnerDlg.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectSearchSpinnerDlg.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void extraPassengers_Dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.extra_passengers_alert, null);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        edt_Txt_ExtraPass=view.findViewById(R.id.edt_Txt_ExtraPass);
        Btn_ExtraPasSubmit=view.findViewById(R.id.Btn_ExtraPasSubmit);
        Btn_ExtraPasCancel=view.findViewById(R.id.Btn_ExtraPasCancel);

        Btn_ExtraPasSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(edt_Txt_ExtraPass.getText()).toString().trim().isEmpty()){
                    Toast.makeText(mContext,"Please Enter the Extra Passengers !",Toast.LENGTH_LONG).show();
                }else {
                  //  SpotChallanActivity.noOf_ExtraPass= Integer.parseInt(edt_Txt_ExtraPass.getText().toString().trim());
                    alertDialog.dismiss();
                }
            }
        });
    }


    /*//get selected name string seperated by coma
    public String getDataString() {
        String data = "";
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                data = data + ", " + mDataSet.get(i).getName();
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }
    //get selected name ararylist
    public ArrayList<String> getSelectedNameList() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                names.add(mDataSet.get(i).getName());
            }
        }
        //  return names.toArray(new String[names.size()]);
        return names;
    }*/


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    void setData(ArrayList<MultiSelectModel> data, String query, MutliSelectAdapter mutliSelectAdapter) {
        this.mDataSet = data;
        this.mSearchQuery = query;
        mutliSelectAdapter.notifyDataSetChanged();
    }

    class MultiSelectSearchSpinnerDlgViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView dialog_name_item;
        private AppCompatCheckBox dialog_item_checkbox;
        private LinearLayout main_container;

        MultiSelectSearchSpinnerDlgViewHolder(View view) {
            super(view);
            dialog_name_item = view.findViewById(R.id.dialog_item_name);
            dialog_item_checkbox = view.findViewById(R.id.dialog_item_checkbox);
            main_container = (LinearLayout) view.findViewById(R.id.main_container);
        }
    }
}