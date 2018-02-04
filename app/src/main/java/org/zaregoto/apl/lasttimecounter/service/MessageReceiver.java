package org.zaregoto.apl.lasttimecounter.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.ui.MainActivity;

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

            String str;
            str = sdf.format(now);
            Log.d(TAG, "receive update intent: " + str + " " + intent.getAction());

            // check alarm
            items = ItemStore.checkAlarmList(context, now);
            showItemInNotification(context, items);

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

    private void showItemInNotification(Context context, ArrayList<Item> items) {

        NotificationManager manager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Content Activity is a activity that is transferred from a list content.
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.ARG_ID_SORT_TYPE, MainActivity.SORT_TYPE.SORT_TYPE_NEARLEST_ALARM);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        builder.setContentText("LasttimeCounter ALARM");
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);

        Iterator<Item> it = items.iterator();
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        Item item;
        while (it.hasNext()) {
            item = it.next();
            style.addLine(item.getName());
        }
        builder.setStyle(style);
        Notification n = builder.build();

        manager.notify(1, n);

    }
}
