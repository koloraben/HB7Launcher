
package com.app.hb7launcher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.app.hb7launcher.model.AppModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AppDataManage {

    private final Context mContext;
    private static final String SharedlistPackage = "SharedlistPackage";
    private static final String listPackage = "listPackage";
    ArrayList<String> arraylistPackage = null;
    public AppDataManage(Context context) {
        mContext = context;
        getListPackage();
    }

    public ArrayList<AppModel> getLaunchAppList() {
        ArrayList<AppModel> listAppLeanback= getLeanbackLaunchAppList();
        ArrayList<AppModel> listApp= getLauncherAppList();
        ArrayList<AppModel> mergedApp= (ArrayList<AppModel>) listAppLeanback.clone();
        System.out.println(" listAppLeanback :: " +listAppLeanback.size());
        System.out.println(" listApp :: " +listApp.size());
        System.out.println(" mergedApp :: " +mergedApp.size());

        Set<String> uniquApps=new HashSet<>();

        for(AppModel app : listAppLeanback){
            uniquApps.add(app.getPackageName());
        }
        System.out.println(" uniquApps b :: " +uniquApps);
        for(AppModel app : listApp){
            if(!uniquApps.contains(app.getPackageName()) ){
                mergedApp.add(app);
                uniquApps.add(app.getPackageName());
            }
        }
        for (int i =0; i < mergedApp.size(); i++){
            AppModel app = mergedApp.get(i);
            if (app.getPackageName().equals("com.droidlogic.FileBrower")){
                app.setName("Fichiers");
                app.setLauncherName("Fichiers");
            }
            if (app.getPackageName().equals("com.app.hb7live")){
                Collections.swap(mergedApp, i, 0);
            }
        }
        return mergedApp;
    }

    public ArrayList<AppModel> getLeanbackLaunchAppList() {
        List<ResolveInfo> localList=null;
        PackageManager localPackageManager = mContext.getPackageManager();
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        localIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK|
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );
        localList = localPackageManager.queryIntentActivities(localIntent,0);

        Iterator<ResolveInfo> localIterator = null;
        ArrayList<AppModel> localArrayList = new ArrayList<>();
        if (localList.size() != 0) {
            localIterator = localList.iterator();
        }
        while (localIterator.hasNext()) {
            ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
            if (getListPackage().contains(localResolveInfo.activityInfo.packageName)){
                AppModel localAppBean = new AppModel();
                localAppBean.setIcon(localResolveInfo.activityInfo.loadBanner(localPackageManager) == null ? localResolveInfo.activityInfo.loadIcon(localPackageManager) : localResolveInfo.activityInfo.loadBanner(localPackageManager));
                localAppBean.setName(localResolveInfo.activityInfo.loadLabel(localPackageManager).toString());
                localAppBean.setPackageName(localResolveInfo.activityInfo.packageName);
                localAppBean.setDataDir(localResolveInfo.activityInfo.applicationInfo.publicSourceDir);
                localAppBean.setLauncherName(localResolveInfo.activityInfo.name);
                String pkgName = localResolveInfo.activityInfo.packageName;
                PackageInfo mPackageInfo;
                try {
                    mPackageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
                    if ((mPackageInfo.applicationInfo.flags & mPackageInfo.applicationInfo.FLAG_SYSTEM) > 0) {
                        localAppBean.setSysApp(true);
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (!localAppBean.getPackageName().equals(mContext.getPackageName())) {
                    localArrayList.add(localAppBean);
                }
            }
        }
        Log.e("Applications size",localArrayList.size()+"");
        return localArrayList;
    }

    public ArrayList<AppModel> getLauncherAppList() {
        List<ResolveInfo> localList=null;
        PackageManager localPackageManager = mContext.getPackageManager();
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LEANBACK_LAUNCHER");
        localIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK|
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );
        localList = localPackageManager.queryIntentActivities(localIntent,0);
        Set<ResolveInfo> foo = new HashSet<>(localList);

        Iterator<ResolveInfo> localIterator = null;
        ArrayList<AppModel> localArrayList = new ArrayList<>();
        if (localList.size() != 0) {
            localIterator = localList.iterator();
        }
        while (localIterator.hasNext()) {
            ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
            if (getListPackage().contains(localResolveInfo.activityInfo.packageName)){
                AppModel localAppBean = new AppModel();
                localAppBean.setIcon(localResolveInfo.activityInfo.loadBanner(localPackageManager) == null ? localResolveInfo.activityInfo.loadIcon(localPackageManager) : localResolveInfo.activityInfo.loadBanner(localPackageManager));
                localAppBean.setName(localResolveInfo.activityInfo.loadLabel(localPackageManager).toString());
                localAppBean.setPackageName(localResolveInfo.activityInfo.packageName);
                localAppBean.setDataDir(localResolveInfo.activityInfo.applicationInfo.publicSourceDir);
                localAppBean.setLauncherName(localResolveInfo.activityInfo.name);
                String pkgName = localResolveInfo.activityInfo.packageName;
                PackageInfo mPackageInfo;
                try {
                    mPackageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
                    if ((mPackageInfo.applicationInfo.flags & mPackageInfo.applicationInfo.FLAG_SYSTEM) > 0) {
                        localAppBean.setSysApp(true);
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (!localAppBean.getPackageName().equals(mContext.getPackageName())) {
                    localArrayList.add(localAppBean);
                }
            }
        }
        Log.e("Applications size",localArrayList.size()+"");
        return localArrayList;
    }

    private ArrayList<String> getListPackage(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedlistPackage,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(listPackage,null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        arraylistPackage = gson.fromJson(json,type);
        if (arraylistPackage == null){
            arraylistPackage = new ArrayList<>();
            arraylistPackage.add("com.canal.android.canal");
            arraylistPackage.add("ptv.bein.ui");
            arraylistPackage.add("com.sfr.android.sfrsport");
            arraylistPackage.add("com.app.hb7live");
            arraylistPackage.add("com.droidlogic.FileBrower");
            arraylistPackage.add("com.droidlogic.videoplayer");
            arraylistPackage.add("com.amazon.amazonvideo.livingroom");
            arraylistPackage.add("com.orange.ocsgo");
            arraylistPackage.add("net.aietec.epg");
            arraylistPackage.add("com.instagram.android");
            arraylistPackage.add("io.makeroid.sarl_alzeto.radio");
            arraylistPackage.add("com.facebook.katana");
            arraylistPackage.add("com.skype.raider");
            arraylistPackage.add("com.twitter.android");
            arraylistPackage.add("com.google.android.youtube.tv");
            arraylistPackage.add("com.netflix.mediaclient");
            arraylistPackage.add("com.android.musicfx");
            arraylistPackage.add("fr.meteo");
            saveListPackage();
        }
        System.out.println(arraylistPackage);
        return arraylistPackage;
    }
    public void saveListPackage(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedlistPackage,Context.MODE_PRIVATE);
        SharedPreferences.Editor listEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arraylistPackage);
        listEditor.putString(listPackage,json);
        listEditor.apply();

    }

    public ArrayList<String> getArraylistPackage() {
        return arraylistPackage;
    }
}
