package com.example.panda.newevent.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.panda.newevent.MainActivity;
import com.example.panda.newevent.database.Info;
import com.example.panda.newevent.tools.ACache;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by panda on 2016/11/30.
 */

public class myReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent){
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String s=intent.getStringExtra("msg");
            Log.i(">>>msg",s);
            JSONObject jsonObject= null;
            try {
                jsonObject=new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String value=jsonObject.optString("alert");
            Log.i(">>>val",value+"");
            String s1[]=new String [100];
            s1=value.split(",");
            Log.i(">>>",""+s1);
            String time=s1[0];
            String title=s1[1];
            String content=s1[2];
            String emergency=s1[3];

            Info p2 = new Info();
            p2.setTitle(title);
            p2.setContent(content);
            p2.setTime(time);
            p2.setEmergency(emergency);
            ACache userCache = ACache.get(context,"User");
            p2.setUser(userCache.getAsString("username"));
            p2.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Log.i("done", "done");
                        Toast.makeText(context,"接受通知成功",Toast.LENGTH_LONG).show();
                        //saved = true;
                    } else {
                        Log.i("fail", "fail" + e);
                        Toast.makeText(context,"接受通知失败",Toast.LENGTH_LONG).show();
                    }
                }
            });

            Log.i(">>>msg",time+title+content+"");
            Bundle bundle=new Bundle();
            bundle.putString("time",time);
            bundle.putString("title",title);
            bundle.putString("content",content);
            bundle.putString("emergency",emergency);
            Intent pIntent=new Intent();
            pIntent.putExtras(bundle);
            pIntent.setClass(context,MainActivity.class);
            pIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(pIntent);
        }
    }

}
