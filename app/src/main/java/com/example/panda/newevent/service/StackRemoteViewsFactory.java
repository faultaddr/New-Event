package com.example.panda.newevent.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by panyunyi on 2017/2/27.
 */
public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext=applicationContext;
    }
    @Override
    public  void onCreate(){}

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        if(mCursor!=null){
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }
//获取每个listView 的每一个条目View
    @Override
    public RemoteViews getViewAt(int i) {



        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
