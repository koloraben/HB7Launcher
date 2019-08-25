package com.app.hb7launcher.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.processphoenix.ProcessPhoenix;

public class InstallationReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        // when package removed
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

            AppDataManage appDataManage = new AppDataManage(context);
            if (!appDataManage.getArraylistPackage().contains(intent.getData().getSchemeSpecificPart())) {
                appDataManage.getArraylistPackage().add(intent.getData().getSchemeSpecificPart());
                appDataManage.saveListPackage();
                Log.e(" BroadcastReceiver ", "InstallationReceiver onReceive called "
                        + " PACKAGE_ADDED ");
            }
            Toast.makeText(context, "Application intallée: " + intent.getData().getSchemeSpecificPart(),
                    Toast.LENGTH_LONG).show();


        }
        // when package installed
        else if (intent.getAction().equals(
                "android.intent.action.PACKAGE_REMOVED")) {
            AppDataManage appDataManage = new AppDataManage(context);
            boolean isRemoved = appDataManage.getArraylistPackage().remove(intent.getData().getSchemeSpecificPart());
            if (isRemoved) appDataManage.saveListPackage();
            Log.e(" BroadcastReceiver ", " InstallationReceiver onReceive called " + "PACKAGE_REMOVED :" + intent.getData().getSchemeSpecificPart());
            Toast.makeText(context, "Application Supprimé: "+intent.getData().getSchemeSpecificPart(),
                    Toast.LENGTH_LONG).show();

        }
        // TODO: 8/9/2019
        ProcessPhoenix.triggerRebirth(context);

    }
}