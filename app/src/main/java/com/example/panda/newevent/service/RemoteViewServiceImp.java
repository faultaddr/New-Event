package com.example.panda.newevent.service;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactoryImp(this, intent);
    }

    private static ArrayList<widgetData> data=new ArrayList<>();

    public  void loadData() {
        ACache mCache=ACache.get(getApplication(),"ACache");
        try {

            JSONArray titlejsonArray = mCache.getAsJSONArray("title");
            //JSONArray idjsonArray = mCache.getAsJSONArray("id");
            JSONArray timejsonArray = mCache.getAsJSONArray("time");
            //JSONArray contentjsonArray = mCache.getAsJSONArray("content");
            //JSONArray emergencyjsonArray = mCache.getAsJSONArray("emergency");
            Log.i("data>>>",timejsonArray+"");
            for (int i = 0; i < titlejsonArray.length(); i++) {

                widgetData widgetData=new widgetData(timejsonArray.get(i).toString(),titlejsonArray.get(i).toString());
                data.add(widgetData);
                Log.i("data>>>",data.toString()+"");
            }
            //Log.i("listTitle", listTitle.toString());
        } catch (Exception ex) {
            //record = 1;
            Log.e("exception", ex.getMessage());

        } finally {
            //TAG=true;
        }
    }

    class RemoteViewsFactoryImp implements RemoteViewsFactory {
        private Intent requestIntent;
        private Context requestContext;


        public RemoteViewsFactoryImp(Context context, Intent intent) {
            requestContext = context;
            requestIntent = intent;
        }

        @Override
        public void onCreate() {
            loadData();

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(requestContext.getPackageName(), R.layout.widgetitem);

            Log.i("requestVonter",requestContext.getPackageName().toString());
            Log.i("DATA___",data.get(position).getWidgetTime()+"<<>>"+position);
            //remoteViews.setTextViewText(R.id.widgetTime, "12344");
            remoteViews.setTextViewText(R.id.widgetTime,data.get(position).getWidgetTime());
            remoteViews.setTextViewText(R.id.widgetTitle,data.get(position).getWidgetTitle());
            Log.i(">>>>data",""+remoteViews);
            Intent intent = new Intent(NewAppWidget.ITEMCLICK);
            intent.putExtra("position", position);
            remoteViews.setOnClickFillInIntent(R.id.widgetitem1, intent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
