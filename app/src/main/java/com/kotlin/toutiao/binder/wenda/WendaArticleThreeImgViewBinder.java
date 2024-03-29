package com.kotlin.toutiao.binder.wenda;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.kotlin.toutiao.ErrorAction;
import com.kotlin.toutiao.R;
import com.kotlin.toutiao.bean.wenda.WendaArticleDataBean;
import com.kotlin.toutiao.util.ImageLoader;
import com.kotlin.toutiao.util.SettingUtil;
import com.kotlin.toutiao.util.TimeUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Meiji on 2017/6/11.
 */

public class WendaArticleThreeImgViewBinder extends ItemViewBinder<WendaArticleDataBean, WendaArticleThreeImgViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected WendaArticleThreeImgViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_wenda_article_three_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final WendaArticleDataBean item) {

        Context context = holder.itemView.getContext();

        try {
            int size = item.getExtraBean().getWenda_image().getThree_image_list().size();
            String[] ivs = new String[size];
            for (int i = 0; i < size; i++) {
                ivs[i] = item.getExtraBean().getWenda_image().getThree_image_list().get(i).getUrl();
            }
            switch (ivs.length) {
                case 1:
                    ImageLoader.loadCenterCrop(context, ivs[0], holder.iv_0, R.color.viewBackground);
                    break;
                case 2:
                    ImageLoader.loadCenterCrop(context, ivs[0], holder.iv_0, R.color.viewBackground);
                    ImageLoader.loadCenterCrop(context, ivs[1], holder.iv_1, R.color.viewBackground);
                    break;
                case 3:
                    ImageLoader.loadCenterCrop(context, ivs[0], holder.iv_0, R.color.viewBackground);
                    ImageLoader.loadCenterCrop(context, ivs[1], holder.iv_1, R.color.viewBackground);
                    ImageLoader.loadCenterCrop(context, ivs[2], holder.iv_2, R.color.viewBackground);
                    break;
            }

            String tv_title = item.getQuestionBean().getTitle();
            String tv_answer_count = item.getQuestionBean().getNormal_ans_count() + item.getQuestionBean().getNice_ans_count() + "回答";
            String tv_datetime = item.getQuestionBean().getCreate_time() + "";
            if (!TextUtils.isEmpty(tv_datetime)) {
                tv_datetime = TimeUtil.getTimeStampAgo(tv_datetime);
            }
            holder.tv_title.setText(tv_title);
            holder.tv_title.setTextSize(SettingUtil.getInstance().getTextSize());
            holder.tv_answer_count.setText(tv_answer_count);
            holder.tv_time.setText(tv_datetime);

            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                            // TODO: 2019/12/23 去问答详情
                          //  WendaContentActivity.launch(item.getQuestionBean().getQid() + "");
                        }
                    });
        } catch (Exception e) {
            ErrorAction.print(e);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private ImageView iv_0;
        private ImageView iv_1;
        private ImageView iv_2;
        private TextView tv_answer_count;
        private TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.iv_0 = itemView.findViewById(R.id.iv_0);
            this.iv_1 = itemView.findViewById(R.id.iv_1);
            this.iv_2 = itemView.findViewById(R.id.iv_2);
            this.tv_answer_count = itemView.findViewById(R.id.tv_answer_count);
            this.tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
