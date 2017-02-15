package de.jonas_moennig.powerbuttoncounter;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.SortedMap;

/**
 * Created by jonas on 18.04.16.
 */
public class UpdateService extends Service {

    private static long time = 0;
    private BroadcastReceiver mReceiver;
    public static int mId = 34534534;

    @Override
    public void onCreate() {
        super.onCreate();
        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new PowerButtonReceiver();
        registerReceiver(mReceiver, filter);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("PowerButtonCounter läuft")
                        .setContentText("Count: " + Storage.getInstance(this).getCount());

        startForeground(mId, mBuilder.build());
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            time = System.currentTimeMillis();
        } else {
            if(System.currentTimeMillis() - time < 1500){
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(new long[] {0, 100}, -1);
                Storage.getInstance(this).increment();
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                        .setContentText("Count: " + Storage.getInstance(this).getCount());
                mNotificationManager.notify(mId, mNotifyBuilder.build());
            }
            time = 0;
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
       unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
