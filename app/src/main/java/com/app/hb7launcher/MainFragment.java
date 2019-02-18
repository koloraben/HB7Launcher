package com.app.hb7launcher;

import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.VerticalGridPresenter;

import com.app.hb7launcher.cards.presenters.CardPresenterSelector;
import com.app.hb7launcher.model.CardRow;
import com.app.hb7launcher.utils.Utils;
import com.google.gson.Gson;

public class MainFragment extends VerticalGridFragment {
    private static final int COLUMNS = 4;
    private static final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_MEDIUM;

    private ArrayObjectAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(getString(R.string.app_name));
        setupRowAdapter();
        setBadgeDrawable(getResources().getDrawable(R.drawable.thumbnail_example_grid));
    }

    private void setupRowAdapter() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(ZOOM_FACTOR);
        gridPresenter.setNumberOfColumns(COLUMNS);
        setGridPresenter(gridPresenter);

        PresenterSelector cardPresenterSelector = new CardPresenterSelector(getActivity());
        mAdapter = new ArrayObjectAdapter(cardPresenterSelector);
        setAdapter(mAdapter);

        prepareEntranceTransition();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                startEntranceTransition();
            }
        }, 1000);
    }

    private void createRows() {
        String json = Utils.inputStreamToString(getResources()
                .openRawResource(R.raw.grid_example));
        CardRow row = new Gson().fromJson(json, CardRow.class);
        mAdapter.addAll(0, row.getCards());
    }
}
