
package com.app.hb7launcher.model;

import android.content.Context;
import android.content.Intent;


import com.app.hb7launcher.R;

import java.util.ArrayList;
import java.util.List;

public class FunctionModel {

    private int icon;
    private String id;
    private String name;
    private Intent mIntent;
    private String className;
    private String pck;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPck() {
        return pck;
    }

    public void setPck(String pck) {
        this.pck = pck;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public static List<FunctionModel> getFunctionList(Context context) {
        List<FunctionModel> functionModels = new ArrayList<>();

        FunctionModel networkSettings = new FunctionModel();
        networkSettings.setName("Connectivité");
        networkSettings.setIcon(R.drawable.ic_settings_ethernet_active);
        networkSettings.setClassName("com.android.tv.settings.connectivity.NetworkActivity");
        networkSettings.setPck("com.android.tv.settings");
        FunctionModel settings = new FunctionModel();
        settings.setName("Settings");
        settings.setIcon(R.drawable.ic_settings_settings);
        settings.setClassName("com.android.tv.settings.Settings");
        settings.setPck("com.android.tv.settings");
        functionModels.add(settings);

        return functionModels;
    }
}
