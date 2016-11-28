package com.example.panda.newevent.model;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by panda on 2016/11/23.
 */

public class ListContent {
    private String adapterListContent;


    private String adapterListTitle;
    private int adapterListDrawable;

    public ListContent(String AdapterListTitle,String AdapterListContent,int i){
        adapterListTitle=AdapterListTitle;
        adapterListContent=AdapterListContent;
        adapterListDrawable=i;
    }

    public String getAdapterListTitle() {
        return adapterListTitle;
    }

    public void setAdapterListTitle(String adapterListTitle) {
        this.adapterListTitle = adapterListTitle;
    }

    public String getAdapterListContent() {
        return adapterListContent;
    }

    public void setAdapterListContent(String adapterListContent) {
        this.adapterListContent = adapterListContent;
    }


    public int getAdapterListDrawable() {
        return adapterListDrawable;
    }

    public void setAdapterListDrawable(int adapterListDrawable) {
        this.adapterListDrawable = adapterListDrawable;
    }




    }





