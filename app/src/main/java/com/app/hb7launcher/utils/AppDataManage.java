
package com.app.hb7launcher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.app.hb7launcher.model.AppModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AppDataManage {

    private final Context mContext;

    public AppDataManage(Context context) {
        mContext = context;
    }

    public ArrayList<AppModel> getLaunchAppList() {
        ArrayList<AppModel> listAppLeanback= getLeanbackLaunchAppList();
        ArrayList<AppModel> listApp= getLauncherAppList();
        ArrayList<AppModel> mergedApp= (ArrayList<AppModel>) listAppLeanback.clone();

        Set<String> uniquApps=new HashSet<>();

        for(AppModel app : listAppLeanback){
            uniquApps.add(app.getPackageName());
        }

        for(AppModel app : listApp){
            if(!uniquApps.contains(app.getPackageName()) ){
                mergedApp.add(app);
                uniquApps.add(app.getPackageName());
            }
        }
        AppModel hb7App = null;
        for (AppModel app : mergedApp){
            if (app.getPackageName().equals("com.app.hb7live")){
                hb7App = app;
                //mergedApp.remove(app);
                break;
            }
        }
        mergedApp.remove(hb7App);
        mergedApp.set(0,hb7App);

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
        ArrayList<String> listPackage = new ArrayList<>();
        listPackage.add("com.canal.android.canal");
        listPackage.add("com.sfr.android.sfrsport");
        listPackage.add("com.app.hb7live");
        listPackage.add("com.netflix.mediaclient");
        //listPackage.add("com.droidlogic.FileBrower");
        listPackage.add("com.android.music");
        listPackage.add("com.droidlogic.videoplayer");
        listPackage.add("");
        return listPackage;
    }
}
