package com.example.death.serviceassignment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class MyService extends Service {
    int a;
    final static String key = "key";
    final static String update = "update";
    final static String message = "message";
    final static String msgservice = "msgservice";

    MyServiceReceiver myServiceReceiver;
    MyServiceThread myServiceThread;
    int b = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        myServiceReceiver = new MyServiceReceiver();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(msgservice);
        registerReceiver(myServiceReceiver, intentFilter);

        myServiceThread = new MyServiceThread();
        myServiceThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        myServiceThread.setRunning(false);
        unregisterReceiver(myServiceReceiver);
        super.onDestroy();
    }

    public class MyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(msgservice)) {

                String msg = intent.getStringExtra(message);
                a = Integer.parseInt(msg);
            }
        }
    }

    private class MyServiceThread extends Thread {

        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            b = 0;
            running = true;
            while (running && b <= MainActivity.time) {
                try {
                    Thread.sleep(1000);

                    Intent intent = new Intent();
                    intent.setAction(update);
                    intent.putExtra(key, b);
                    sendBroadcast(intent);

                    b++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}