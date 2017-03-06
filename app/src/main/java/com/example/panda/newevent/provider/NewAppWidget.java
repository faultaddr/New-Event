package com.example.panda.newevent.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.panda.newevent.MainActivity;
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
        for (int appWidgetId:appWidgetIds) {
            // 获取AppWidget对应的视图
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            Intent intent1 = new Intent(ITEMCLICK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            rv.setPendingIntentTemplate(R.id.listviewWidget, pendingIntent);

            Intent serviceIntent = new Intent(context, RemoteViewServiceImp.class);
            rv.setRemoteAdapter(R.id.listviewWidget, serviceIntent);


            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ITEMCLICK)) {
            Toast.makeText(context, intent.getIntExtra("position", 0) + "", Toast.LENGTH_SHORT).show();
        }


        }
        }
