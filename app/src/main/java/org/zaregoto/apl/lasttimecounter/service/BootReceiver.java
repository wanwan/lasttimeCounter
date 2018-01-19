package org.zaregoto.apl.lasttimecounter.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    public static final String ALARM_RECEIVER_INTENT_TRIGGER = "org.zaregoto.apl.lasttimecounter.ALARM_TRIGGER";

    @Override
    public void onReceive(Context context, Intent intent) {
        //端末起動時にサービスを起動する
        Log.d("Debug TEST", "Debug START");
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            context.startService(new Intent(context, NotificationService.class));
        }
        else {

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String str;

            str = sdf.format(now);
            Log.d(TAG, "onHandleIntent: " + str);

            Log.d(TAG, "***** BootReceiver *****");
        }
    }
}
