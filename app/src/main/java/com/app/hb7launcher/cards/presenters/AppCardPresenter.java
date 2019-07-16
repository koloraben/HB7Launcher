package com.app.hb7launcher.cards.presenters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.hb7launcher.R;
import com.app.hb7launcher.model.AppModel;

/**
 * ImageCard Presenter
 *
 * @author jacky
 * @version v1.0
 * @since 16/7/16
 */
public class AppCardPresenter extends Presenter {

    private Context mContext;
    private Drawable mDefaultCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.background_food);
        ImageCardView cardView = new ImageCardView(mContext) {
            @Override
            public void setSelected(boolean selected) {
                int selected_background = mContext.getResources().getColor(R.color.detail_background);
                int default_background = mContext.getResources().getColor(R.color.default_background);
                int color = selected ? selected_background : default_background;
                findViewById(R.id.info_field).setBackgroundColor(color);
                super.setSelected(selected);
            }
        };
        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent ));
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        Resources resources = cardView.getContext().getResources();
        int cardWidth = Math.round(resources.getDimensionPixelSize(R.dimen.card_width)
        );
        int cardHeight = resources.getDimensionPixelSize(R.dimen.card_height);
        cardView.setMainImageDimensions(cardWidth,cardHeight);
        AppModel appBean = (AppModel) item;
        cardView.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cardView.getMainImageView().setImageDrawable(appBean.getIcon());
        cardView.setTitleText(appBean.getName());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
