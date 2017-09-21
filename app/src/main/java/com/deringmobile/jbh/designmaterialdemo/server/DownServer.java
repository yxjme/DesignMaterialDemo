package com.deringmobile.jbh.designmaterialdemo.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.activity.DetailActivity;
import com.deringmobile.jbh.designmaterialdemo.util.LogUtil;


/**
 * Created by zbsdata on 2017/9/6.
 */

public class DownServer  extends Service {


    public MyBinder binder=new MyBinder();
    public NotificationManager manager;
    private Notification notification;
    private NotificationCompat.Builder builder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.showLogV("======tag=","onCreate 服务已经启动了");
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notifyState() {
        builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置图标
        builder.setContentTitle("My notification");//内容标题
        builder.setContentText("Hello World!");//内容

        //设置点击通知跳转的activity
        Intent resultIntent = new Intent(this, DetailActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //创建一个任务栈，用于处理在通知页面，返回时现实的页面
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
         notification = builder.build();

       //这通知的其他属性，比如：声音和振动
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        Progress();
    }


    private int progress;
    private void Progress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress < 100){
                    progress++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    builder.setProgress(100,progress,false);
                    manager.notify(0,builder.build());
                }
            }
        }).start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }


    public class MyBinder extends Binder{
        public DownServer getServerContext() {
            return DownServer.this;
        }
    }
}
