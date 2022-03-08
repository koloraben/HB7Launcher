package com.app.hb7launcher.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/*
 * Created by Issam ELGUERCH on 3/7/2022.
 * mail: issamelguerch@gmail.com
 * All rights reserved.
 */


public class InputMethodChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_INPUT_METHOD_CHANGED)) {
            Intent i = new Intent();
            i.setClassName("com.app.hb7launcher", "com.app.hb7launcher.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}