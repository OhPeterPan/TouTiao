package com.kotlin.toutiao.binder.news;

import android.content.Context;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.kotlin.toutiao.ErrorAction;
import com.kotlin.toutiao.IntentAction;
import com.kotlin.toutiao.R;
import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.util.ImageLoader;
import com.kotlin.toutiao.util.SettingUtil;
import com.kotlin.toutiao.util.TimeUtil;
import com.kotlin.toutiao.widget.CircleImageView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Meiji on 2017/6/8.
 * 不带图片的 item
 */

public class NewsArticleTextViewBinder extends ItemViewBinder<MultiNewsArticleDataBean, NewsArticleTextViewBinder.ViewHolder> {

    private static final String TAG = "NewsArticleTextViewBind";

    @NonNull
    @Override
    protected NewsArticleTextViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_news_article_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NewsArticleTextViewBinder.ViewHolder holder,
                                    @NonNull final MultiNewsArticleDataBean item) {

        final Context context = holder.itemView.getContext();

        try {
            if (null != item.getUser_info()) {
                String avatar_url = item.getUser_info().getAvatar_url();
                if (!TextUtils.isEmpty(avatar_url)) {
                    ImageLoader.loadCenterCrop(context, avatar_url, holder.iv_media, R.color.viewBackground);
                }
            }

            String tv_title = item.getTitle();
            String tv_abstract = item.getAbstractX();
            String tv_source = item.getSource();
            String tv_comment_count = item.getComment_count() + "评论";
            String tv_datetime = item.getBehot_time() + "";
            if (!TextUtils.isEmpty(tv_datetime)) {
                tv_datetime = TimeUtil.getTimeStampAgo(tv_datetime);
            }

            holder.tv_title.setText(tv_title);
            holder.tv_title.setTextSize(SettingUtil.getInstance().getTextSize());
            holder.tv_abstract.setText(tv_abstract);
            holder.tv_extra.setText(tv_source + " - " + tv_comment_count + " - " + tv_datetime);
            holder.iv_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context,
                            holder.iv_dots, Gravity.END, 0, R.style.MyPopupMenu);
                    popupMenu.inflate(R.menu.menu_share);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menu) {
                            int itemId = menu.getItemId();
                            if (itemId == R.id.action_share) {
                                IntentAction.send(context, item.getTitle() + "\n" + item.getShare_url());
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                            // TODO: 2019/12/9 新闻详情页
                            //NewsContentActivity.launch(item);
                        }
                    });
        } catch (Exception e) {
            ErrorAction.print(e);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_media;
        private TextView tv_extra;
        private TextView tv_title;
        private TextView tv_abstract;
        private ImageView iv_dots;

        ViewHolder(View itemView) {
            super(itemView);
            this.iv_media = itemView.findViewById(R.id.iv_media);
            this.tv_extra = itemView.findViewById(R.id.tv_extra);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_abstract = itemView.findViewById(R.id.tv_abstract);
            this.iv_dots = itemView.findViewById(R.id.iv_dots);
        }
    }
}
