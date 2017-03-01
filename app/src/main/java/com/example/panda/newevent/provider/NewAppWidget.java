package com.example.panda.newevent.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.panda.newevent.R;
import com.example.panda.newevent.service.RemoteViewServiceImp;

/**
 * Created by panyunyi on 2017/2/28.
 */

public class NewAppWidget extends AppWidgetProvider {
    public static final String BTNACTION = "com.xinxue.action.TYPE_BTN";
    public static final String ITEMCLICK = "com.xinxue.action.TYPE_LIST";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

//绑定service用来填充listview中的视图
        Intent intent = new Intent(context, RemoteViewServiceImp.class);
        remoteViews.setRemoteAdapter(R.id.listviewWidget, intent);
        //remoteViews.setTextViewText(R.id.widgetTitle,"111");

        Intent intent1 = new Intent(ITEMCLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.listviewWidget, pendingIntent);

        //如果你添加了多个实例的情况下需要下面的处理
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ITEMCLICK)) {
            Toast.makeText(context, intent.getIntExtra("position", 0) + "", Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,  R.id.listviewWidget);
            Log.e("finally after a whole", "working :");
        }
    }
}