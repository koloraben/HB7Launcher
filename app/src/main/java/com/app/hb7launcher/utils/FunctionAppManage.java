package com.app.hb7launcher.utils;

import android.content.Context;

import com.app.hb7launcher.R;
import com.app.hb7launcher.model.FunctionModel;

import java.util.ArrayList;
import java.util.List;

public class FunctionAppManage {
    private final Context mContext;

    public FunctionAppManage(Context mContext) {
        this.mContext = mContext;
    }

    public static List<FunctionModel> getFunctionList(Context context) {
        List<FunctionModel> functionModels = new ArrayList<>();

        FunctionModel networkSettings = new FunctionModel();
        networkSettings.setName("Connectivité");
        networkSettings.setIcon(R.drawable.ic_settings_ethernet_active);
        networkSettings.setClassName("com.android.tv.settings.connectivity.NetworkActivity");
        networkSettings.setPck("com.android.tv.settings");
        functionModels.add(networkSettings);
        /*//////////////////////////////////////////////////
        FunctionModel settings = new FunctionModel();
        settings.setName("Settings");
        settings.setIcon(R.drawable.ic_settings_settings);
        settings.setClassName("com.android.tv.settings.Settings");
        settings.setPck("com.android.tv.settings");
        functionModels.add(settings);
        //////////////////////////////////////////////////
        FunctionModel settingsBluetooth = new FunctionModel();
        settingsBluetooth.setName("Bluetooth");
        settingsBluetooth.setIcon(R.drawable.ic_settings_settings);
        settingsBluetooth.setClassName("com.android.tv.settings.bluetoothSettings");
        settingsBluetooth.setPck("com.android.tv.settings");
        functionModels.add(settingsBluetooth);*/

        return functionModels;
    }
}
