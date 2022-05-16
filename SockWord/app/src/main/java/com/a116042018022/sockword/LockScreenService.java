package com.a116042018022.sockword;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class LockScreenService extends Service {

    private LockScreenReceiver mReceiver;
    private IntentFilter mIntentFilter = new IntentFilter();
    private boolean isNotiShow = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //锁屏服务启动后动态注册相关事件监听
        //mIntentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        //mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        //mIntentFilter.addAction(Intent.ACTION_TIME_TICK);

        //设置优先级为最高
        mIntentFilter.setPriority(Integer.MAX_VALUE);

        if (null == mReceiver) {
            mReceiver = new LockScreenReceiver();
            //动态注册该广播接收者
            registerReceiver(mReceiver, mIntentFilter);

            //使其成为前台服务foreground service
            buildNotification();
        }

        return START_STICKY;
    }

    /**
     * 通知栏显示
     */
    private void buildNotification() {
        if (!isNotiShow){ //避免多次显示

            Intent intent = new Intent(this, DetailActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            String channelId = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channelId = createNotificationChannel("lockScreenService", "My LockScreen Service");
            }

            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setTicker("APP正在运行")
                    .setAutoCancel(false)
                    .setContentTitle("APP正在运行")
                    .setContentText("运行中")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0x101, notification);

            startForeground(0x11, notification);

            isNotiShow = true;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){

        NotificationChannel chan =new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);

        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager service = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }


    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            stopForeground(true);
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

}