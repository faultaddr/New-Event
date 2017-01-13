package com.example.panda.newevent.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.panda.newevent.MainActivity;

import cn.bmob.push.PushConstants;

/**
 * Created by panda on 2016/11/30.
 */

public class myReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String s=intent.getStringExtra("msg");
            Log.i(">>>s",s+"");
            String s1[]=new String [100];
            s1=s.split("\\,");
            Log.i(">>>",""+s1);
            String time=s1[0];
            String title=s1[1];
            String content=s1[2];
            Log.i(">>>msg",time+title+content+"");
            Bundle bundle=new Bundle();
            bundle.putString("time",time);
            bundle.putString("title",title);
            bundle.putString("content",content);
            Intent pIntent=new Intent();
            pIntent.putExtras(bundle);
            pIntent.setClass(context,MainActivity.class);
            context.startActivity(pIntent);
        }
    }

}
