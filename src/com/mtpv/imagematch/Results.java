package com.mtpv.imagematch;

import java.io.Serializable;
import java.util.ArrayList;

public class Results extends ArrayList<Telangana> implements Serializable {

    private ArrayList<Telangana> arrayList_Telangana;
   /* private ArrayList<Missing> arrayList_Missing;
    private ArrayList<Udb> arrayList_Udb;
    private ArrayList<Media> arrayList_Media;
    private ArrayList<Wanted> wantedArrayList;*/


    public ArrayList<Telangana> getArrayList_Telangana() {
        return arrayList_Telangana;
    }

    public void setArrayList_Telangana(ArrayList<Telangana> arrayList_Telangana) {
        this.arrayList_Telangana = arrayList_Telangana;
    }

    /*public ArrayList<Missing> getArrayList_Missing() {
        return arrayList_Missing;
    }

    public void setArrayList_Missing(ArrayList<Missing> arrayList_Missing) {
        this.arrayList_Missing = arrayList_Missing;
    }

    public ArrayList<Udb> getArrayList_Udb() {
        return arrayList_Udb;
    }

    public void setArrayList_Udb(ArrayList<Udb> arrayList_Udb) {
        this.arrayList_Udb = arrayList_Udb;
    }

    public ArrayList<Media> getArrayList_Media() {
        return arrayList_Media;
    }

    public void setArrayList_Media(ArrayList<Media> arrayList_Media) {
        this.arrayList_Media = arrayList_Media;
    }

    public ArrayList<Wanted> getWantedArrayList() {
        return wantedArrayList;
    }

    public void setWantedArrayList(ArrayList<Wanted> wantedArrayList) {
        this.wantedArrayList = wantedArrayList;
    }*/


}
