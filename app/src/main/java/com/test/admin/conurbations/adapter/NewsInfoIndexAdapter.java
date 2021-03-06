package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.labelview.LabelView;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.BaseActivity;
import com.test.admin.conurbations.activitys.NewsInfoListDetailActivity;
import com.test.admin.conurbations.fragments.NewsInfoListDetailFragment;
import com.test.admin.conurbations.model.entity.News;
import com.test.admin.conurbations.utils.DateUtils;
import com.test.admin.conurbations.utils.RatioImageView;
import com.test.admin.conurbations.widget.SolidApplication;

/**
 * Created by zhouqiong on 2017/1/12.
 */

public class NewsInfoIndexAdapter extends BaseListAdapter<News> {

    private String date;
    private Context mContext;
    private RatioImageView imageView;

    public void setDate(String date) {
        this.date = date;
    }

    public NewsInfoIndexAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, News item) {

        imageView = vh.getView(R.id.iv_item_news_info_index);
        final LabelView labelView = vh.getView(R.id.lv_item_news_info_index_view);
        imageView.setRatio(0.618f);
        vh.setTypeface(R.id.tv_item_news_info_index_title, SolidApplication.songTi);
        vh.setText(R.id.tv_item_news_info_index_title, item.getTitle());
        if (!item.isRead()) {
            vh.setTextColor(R.id.tv_item_news_info_index_title, ContextCompat.getColor(mContext, R.color.textColorFirst_Day));
        } else {
            vh.setTextColor(R.id.tv_item_news_info_index_title, ContextCompat.getColor(mContext, R.color.textColorThird_Day));
        }
        labelView.setText(DateUtils.formatDate(date));
        Glide.with(imageView.getContext())
                .load(item.getImages().get(0))
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        vh.setOnClickListener(R.id.cv_item_news_info_index, getListener(vh, item));
    }


    @NonNull
    private View.OnClickListener getListener(final BaseViewHolder holder, final News news) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!news.isRead()) {
                    news.setRead(true);
                    holder.setTextColor(R.id.tv_item_news_info_index_title, ContextCompat.getColor(mContext, R.color.color_read));
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, NewsInfoListDetailActivity.class);
                intent.putExtra(NewsInfoListDetailFragment.KEY_NEWS, news.getId());
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context
                        , new Pair<View, String>(imageView, BaseActivity.TRANSLATE_WEB_VIEW_BG_IMG)
                        , new Pair<View, String>(holder.getView(R.id.tv_item_news_info_index_title), BaseActivity.TRANSLATE_WEB_VIEW_TITLE));
                ActivityCompat.startActivity(context, intent, activityOptionsCompat.toBundle());
            }
        };
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_info_index, parent, false));
    }

    public class CategoryHeaderViewHolder extends BaseViewHolder {

        public CategoryHeaderViewHolder(View view) {
            super(view);
        }
    }
}
