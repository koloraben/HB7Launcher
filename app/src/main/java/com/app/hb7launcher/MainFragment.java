package com.app.hb7launcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
    private Drawable mDefaultCardImage;
    private ArrayObjectAdapter mAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRowAdapter();
        setupEventListeners();
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
                    getActivity().startActivity(launchIntent);

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

}
