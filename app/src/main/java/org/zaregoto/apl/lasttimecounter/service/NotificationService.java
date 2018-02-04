package org.zaregoto.apl.lasttimecounter.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


public class NotificationService extends Service {

    private static final String TAG = "NotificationService";

    AlarmManager am;

    @Override
    public void onCreate() {
        super.onCreate();
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        scheduleNotification();
        // check alarm task available

        // set alarms

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void scheduleNotification(){

        Context context = getBaseContext();
        Intent intent = new Intent(MessageReceiver.ALARM_RECEIVER_INTENT_TRIGGER);
        intent.setClass(this, MessageReceiver.class);

        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 10*60*1000, pendingIntent);
    }
}
