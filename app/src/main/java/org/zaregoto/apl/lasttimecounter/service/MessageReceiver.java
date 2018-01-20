package org.zaregoto.apl.lasttimecounter.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = "MessageReceiver";

    public static final String ALARM_RECEIVER_INTENT_TRIGGER = "org.zaregoto.apl.lasttimecounter.ALARM_TRIGGER";

    @Override
    public void onReceive(Context context, Intent intent) {

        //端末起動時にサービスを起動する
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            context.startService(new Intent(context, NotificationService.class));
        }
        else if (intent.getAction().equals(ALARM_RECEIVER_INTENT_TRIGGER)) {

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            ArrayList<Item> items;

            String str;
            str = sdf.format(now);
            Log.d(TAG, "receive update intent: " + str + " " + intent.getAction());

            // check alarm
            items = ItemStore.checkAlarmList(context, now);

        }
        else {
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String str;

            str = sdf.format(now);
            Log.d(TAG, "[else case]: " + str + " " + intent.getAction());
            Log.d(TAG, "***** MessageReceiver *****");
        }
    }
}
