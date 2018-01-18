package org.zaregoto.apl.lasttimecounter.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //端末起動時にサービスを起動する
        Log.d("Debug TEST", "Debug START");
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            context.startService(new Intent(context, NotificationService.class));
        }
    }
}
