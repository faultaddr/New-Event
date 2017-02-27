package com.example.panda.newevent.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by panyunyi on 2017/2/27.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(),intent);
    }
}
