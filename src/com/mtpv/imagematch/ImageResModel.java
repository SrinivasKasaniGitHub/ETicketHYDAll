package com.mtpv.imagematch;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageResModel extends ArrayList<ImageModel> implements Serializable {
    private ArrayList<ImageModel> imageModelArrayList;

    public ArrayList<ImageModel> getImageModelArrayList() {
        return imageModelArrayList;
    }

    public void setImageModelArrayList(ArrayList<ImageModel> imageModelArrayList) {
        this.imageModelArrayList = imageModelArrayList;
    }
}
