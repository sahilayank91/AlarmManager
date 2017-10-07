package com.example.sahil.alarmmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by sahil on 10/1/2017.
 */

public class PlayingService extends Service {

    private boolean isRunning;
    private Context context;
    private MediaPlayer mediaPlayer;
    private int startId;
    static PlayingService ps;

    @Override
    public void onDestroy() {
        this.isRunning=false;
        super.onDestroy();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ps = this;
        Log.e("Playing Service: ","inside onstartcommand");
        NotificationManager notificationManager = (NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
        intent1.putExtra("status","on");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);


        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Richard Dawkins is talking" + "!")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_alarm_off_black_24dp,"Cancel",pIntent)
                .addAction(R.drawable.ic_alarm_add_black_24dp,"Snooze",pIntent)
                .build();

        String state = intent.getExtras().getString("extra");



        assert state != null;
        switch (state) {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }

        if(!isRunning && startId == 1){
            Log.e("if there was not sound ", " and you want start");
            mediaPlayer = MediaPlayer.create(this,R.raw.music);
            mediaPlayer.start();
            notificationManager.notify(0,mNotify);
            this.isRunning = true;
            this.startId = 0;
        } else if (!this.isRunning && startId == 0){
            this.isRunning = false;
            this.startId = 0;
        }else if (this.isRunning && startId == 1){
            this.isRunning = true;
            this.startId = 0;
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.reset();
            this.isRunning = false;
            this.startId = 0;
        }


        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(ps!=null){
            /*Intent intent11 = new Intent(PlayingService.this,MainActivity.class);
            intent11.putExtra("status","on");
            startActivity(intent);*/

        }

        return null;
    }
}
