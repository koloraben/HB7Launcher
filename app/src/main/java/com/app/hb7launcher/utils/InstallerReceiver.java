package com.app.hb7launcher.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.app.hb7launcher.MainActivity;
import com.app.hb7launcher.model.AppModel;

public class InstallerReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        // when package removed
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            addApp(context, intent);
        }
        // when package installed
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            removeApp(context, intent);
        }
        if(intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")) {
            Log.e("BroadcastReceiver ", "connexion "
                    + " connexion ");
                MyToast.createNotif(context,checkInternet(context),ColorToast.GREEN);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addApp(Context context, Intent intent) {
        AppDataManage appDataManage = new AppDataManage(context);
        if (!appDataManage.getArraylistPackage().contains(intent.getData().getSchemeSpecificPart())) {
            appDataManage.getArraylistPackage().add(intent.getData().getSchemeSpecificPart());
            appDataManage.saveListPackage();
            /*Intent dialogIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialogIntent);*/
            Log.e("BroadcastReceiver ", "InstallationReceiver onReceive called "
                    + " PACKAGE_ADDED ");
            Toast.makeText(context, "Application intallée: " + intent.getData().getSchemeSpecificPart(),
                    Toast.LENGTH_LONG).show();
        }


    }

    public void removeApp(Context context, Intent intent) {
        AppDataManage appDataManage = new AppDataManage(context);
        boolean isRemoved = appDataManage.getArraylistPackage().remove(intent.getData().getSchemeSpecificPart());
        if (isRemoved) {
            appDataManage.saveListPackage();
            /*Intent dialogIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialogIntent);*/
            Log.e(" BroadcastReceiver ", " InstallationReceiver onReceive called " + "PACKAGE_REMOVED :" + intent.getData().getSchemeSpecificPart());
            Toast.makeText(context, "Application Supprimé: " + intent.getData().getSchemeSpecificPart(),
                    Toast.LENGTH_LONG).show();
        }

    }

    String checkInternet(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr.getActiveNetworkInfo().isConnected()) {
            return "connecté";
        } else {
            return "pas de connexion !";
        }
    }
}
