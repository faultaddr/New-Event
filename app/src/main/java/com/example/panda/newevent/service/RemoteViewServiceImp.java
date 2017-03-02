package com.example.panda.newevent.service;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.panda.newevent.R;
import com.example.panda.newevent.provider.NewAppWidget;
import com.example.panda.newevent.tools.ACache;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by panyunyi on 2017/2/28.
 */
class widgetData{
    public String getWidgetTime() {
        return widgetTime;
    }

    public void setWidgetTime(String widgetTime) {
        this.widgetTime = widgetTime;
    }

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public void setWidgetTitle(String widgetTitle) {
        this.widgetTitle = widgetTitle;
    }

    String widgetTime;
    String widgetTitle;
    public widgetData(String time,String title){
        widgetTime=time;
        widgetTitle=title;
    }
        }
public class RemoteViewServiceImp extends RemoteViewsService {
    @Override
    public void onStart(Intent intent, int startId){
        super.onCreate();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactoryImp(this, intent);
    }




}
