package com.example.panda.newevent.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.panda.newevent.tools.ACache;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by panyunyi on 2017/2/27.
 */
class widgetData{
    String time;
    String title;
    widgetData(String Time,String Title){
        time=Time;
        title=Title;
    }
}
public class WidgetProvider extends ContentProvider {

    private static ArrayList<String> listTime = new ArrayList<>();//时间
    private static ArrayList<String> listTitle = new ArrayList<>();//事项标题
    private static ArrayList<widgetData> wData=new ArrayList<>();

    public static class Columns{
        public static final String time="Time";
        public static final String content="Content";
    }

    @Override
    public boolean onCreate() {
        initData();
        return false;
    }

    private void initData() {
        ACache mCache=ACache.get(getContext(),"ACache");
        try {

            JSONArray titlejsonArray = mCache.getAsJSONArray("title");
            //JSONArray idjsonArray = mCache.getAsJSONArray("id");
            JSONArray timejsonArray = mCache.getAsJSONArray("time");
            //JSONArray contentjsonArray = mCache.getAsJSONArray("content");
            //JSONArray emergencyjsonArray = mCache.getAsJSONArray("emergency");
            for (int i = 0; i < titlejsonArray.length(); i++) {
                listTitle.add(titlejsonArray.get(i).toString());
                //listId.add(idjsonArray.get(i).toString());
                listTime.add(timejsonArray.get(i).toString());
//                listContent.add(contentjsonArray.get(i).toString());
//                listEmergency.add(emergencyjsonArray.get(i).toString());
            }
            Log.i("listTitle", listTitle.toString());
        } catch (Exception ex) {
            //record = 1;
            Log.e("exception", ex.getMessage());

        } finally {
            //TAG=true;
        }
        for(int i=0;i<listTime.size();i++){
            wData.add(new widgetData(listTime.get(i),listTitle.get(i)));
        }
        ACache aCache=ACache.get(getContext(),"widget");
        aCache.put("widgetData",wData);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        final MatrixCursor c = new MatrixCursor(new String[]{ Columns.time, Columns.content });
        for (int i = 0; i < wData.size(); ++i) {
            final widgetData data = wData.get(i);
            c.addRow(new Object[]{  data.time, data.title });
        }

        return c;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final int index = Integer.parseInt(uri.getPathSegments().get(0));
        final widgetData data = wData.get(index);
        data.time=contentValues.getAsString("Content");
        /**
         * 提醒ContentObserver数据改变。
         */
        getContext().getContentResolver().notifyChange(uri, null);

        return 1;
    }
}
