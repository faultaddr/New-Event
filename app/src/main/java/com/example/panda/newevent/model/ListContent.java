package com.example.panda.newevent.model;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by panda on 2016/11/23.
 */

public class ListContent {
    private ArrayList<String> adapterListContent=new ArrayList<>();
    private ArrayList<String>adapterListTitle=new ArrayList<>();
    private int adapterListDrawable[]=new int[3];
    public ListContent(){

    }
    public ArrayList<String> getAdapterListContent() {
        return adapterListContent;
    }

    public void setAdapterListContent(ArrayList<String> adapterListContent) {
        this.adapterListContent = adapterListContent;
    }

    public ArrayList<String> getAdapterListTitle() {
        return adapterListTitle;
    }

    public void setAdapterListTitle(ArrayList<String> adapterListTitle) {
        this.adapterListTitle = adapterListTitle;
    }

    public int[] getAdapterListDrawable() {
        return adapterListDrawable;
    }

    public void setAdapterListDrawable(int[] adapterListDrawable) {
        this.adapterListDrawable = adapterListDrawable;
    }



}
