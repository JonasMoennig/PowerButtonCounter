package de.jonas_moennig.powerbuttoncounter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jonas on 18.04.16.
 */
public class UpdateService extends Service {

    private static long time = 0;
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new PowerButtonReceiver();
        registerReceiver(mReceiver, filter);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            time = System.currentTimeMillis();
        } else {
            if(System.currentTimeMillis() - time < 1500){
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                Storage.getInstance(this).increment();
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
