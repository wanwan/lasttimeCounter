package org.zaregoto.apl.lasttimecounter.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
            Item item;

            String str;
            str = sdf.format(now);
            Log.d(TAG, "receive update intent: " + str + " " + intent.getAction());

            // check alarm
            items = ItemStore.checkAlarmList(context, now);
            if (null != items && !items.isEmpty()) {
                Iterator<Item> it = items.iterator();
                while (it.hasNext()) {
                    item = it.next();
                    showItemInNotification(context, item);
                }
            }
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

    private void showItemInNotification(Context context, Item item) {

        NotificationManager manager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        builder.setContentText(item.getName());
        Notification n = builder.build();

        manager.notify(1, n);

    }
}
