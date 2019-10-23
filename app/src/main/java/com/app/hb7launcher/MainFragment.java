package com.app.hb7launcher;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.util.Log;
import android.widget.Toast;

import com.app.hb7launcher.cards.presenters.CardPresenterSelector;
import com.app.hb7launcher.model.AppModel;
import com.app.hb7launcher.model.FunctionModel;
import com.app.hb7launcher.utils.AppDataManage;
import com.app.hb7launcher.utils.FunctionAppManage;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends VerticalGridFragment {
    private static final int COLUMNS = 4;
    private static final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_MEDIUM;
    private Context mContext=getContext();
    private static final String TAG = "MainFragmentLauncherHB7";
    BroadcastReceiver mServiceReceiver;
    private ArrayObjectAdapter mAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRowAdapter();
        setupEventListeners();
        mServiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // when package removed
                if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
                    addApp(context, intent);
                }
                // when package installed
                else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                    removeApp(context, intent);
                }
            }
        };

    }
    private void setupRowAdapter() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(ZOOM_FACTOR);
        gridPresenter.setNumberOfColumns(COLUMNS);
        setGridPresenter(gridPresenter);

        PresenterSelector cardPresenterSelector = new CardPresenterSelector(getActivity());
        mAdapter = new ArrayObjectAdapter(cardPresenterSelector);
        setAdapter(mAdapter);
        prepareEntranceTransition();
        createRows();
        startEntranceTransition();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addDataScheme("package");
        getActivity().registerReceiver(mServiceReceiver, filter);
    }

    private void createSettings(){
        FunctionAppManage functionAppManage = new FunctionAppManage(getContext());

        List<FunctionModel> functionModels = functionAppManage.getFunctionList(mContext);
        mAdapter.addAll(mAdapter.size(),functionModels);
    };
    private void createRows() {
        ArrayList<AppModel> appDataList = new AppDataManage(getContext()).getLaunchAppList();
        mAdapter.addAll(0,  appDataList);
        createSettings();
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof AppModel) {
                AppModel appBean = (AppModel) item;
                Intent launchIntent = getActivity().getPackageManager().getLeanbackLaunchIntentForPackage(
                        appBean.getPackageName());
                if (launchIntent != null) {
                    getActivity().startActivity(launchIntent);
                }else{
                     launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(
                            appBean.getPackageName());
                    try {
                        getActivity().startActivity(launchIntent);
                    }catch (Exception e){
                        Log.e("my launcher",e.getMessage());
                        Toast.makeText(getContext(), "probleme dans l'Application",
                                Toast.LENGTH_LONG).show();
                    }

                }
             }
             if (item instanceof FunctionModel){
                 FunctionModel function = (FunctionModel) item;
                 try {
                     if (function.getClassName()!=null){
                         ComponentName name = new ComponentName(function.getPck(),
                                 ((FunctionModel) item).getClassName());
                         Intent i=new Intent(Intent.ACTION_MAIN);

                         i.addCategory(Intent.CATEGORY_LAUNCHER);
                         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                 Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                         i.setComponent(name);

                         startActivity(i);
                     }
                     else {
                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.android.tv.settings");
                        startActivity(intent);
                     }
                 }catch(Exception e){
                     Log.e("my launcher",e.getMessage());
                     startActivity(new Intent(Settings.ACTION_SETTINGS));
                 }
             }

        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {

        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
        }
    }

    public void addApp(Context context, Intent intent) {
        AppDataManage appDataManage = new AppDataManage(context);
        if (!appDataManage.getArraylistPackage().contains(intent.getData().getSchemeSpecificPart())) {
            appDataManage.getArraylistPackage().add(intent.getData().getSchemeSpecificPart());
            appDataManage.saveListPackage();
            mAdapter.clear();
            prepareEntranceTransition();
            createRows();
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
            for (int i = 0; i < mAdapter.size(); i++) {
                AppModel appModel;
                if (mAdapter.get(i) instanceof AppModel) {
                    appModel = (AppModel) mAdapter.get(i);
                    if (appModel.getPackageName().equals(intent.getData().getSchemeSpecificPart())) {
                        mAdapter.remove(appModel);
                        break;
                    }
                }

            }
            Log.e(" BroadcastReceiver ", " InstallationReceiver onReceive called " + "PACKAGE_REMOVED :" + intent.getData().getSchemeSpecificPart());
            Toast.makeText(context, "Application Supprimé: " + intent.getData().getSchemeSpecificPart(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
