package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KaoshiShijuanListAdapter extends BaseQuickAdapter<KaoshiShijuanListResponse.KaoshiShijuan, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    private CallbackListener callbackListener;
    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public KaoshiShijuanListAdapter(Context mContext, List<KaoshiShijuanListResponse.KaoshiShijuan> kaoshiShijuans) {
        super(R.layout.item_kaoshi_shijuan_list, kaoshiShijuans);
        this.mContext = mContext;
    }
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan) {
        // 设置试卷名称
        initShijuanName(viewHolder, kaoshiShijuan);

        // 设置分数或者试卷状态
        if (kaoshiShijuan.getIs_completed() == 1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            String part1 = kaoshiShijuan.getSum_score() + "";
            String part2 = " 分";
            spannableStringBuilder.append(part1).append(part2);

            // 设置红色
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, part1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            // 字体大小设置
            spannableStringBuilder.setSpan(new RelativeSizeSpan(2.5f), 0, part1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            // 加粗倾斜
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, part1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            // 设置红色
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), part1.length(), (part1 + part2).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            TextView textView = viewHolder.findView(R.id.shijuanStatus);
            textView.setTranslationY(-25);
            textView.setText(spannableStringBuilder);
        } else {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            String part1 = "待考试";
            spannableStringBuilder.append(part1);
            // 设置绿色
            spannableStringBuilder.setSpan(new ForegroundColorSpan(UIUtils.getResourceColor(mContext, R.color.green_300)), 0, part1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            TextView textView = viewHolder.findView(R.id.shijuanStatus);
            textView.setTranslationY(0);
            textView.setText(spannableStringBuilder);
        }

        viewHolder.findView(R.id.shijuanName).setOnClickListener(v -> {
            if (callbackListener != null){
                callbackListener.onConfirm(kaoshiShijuan);
            }
        });
    }

    // 设置试卷名称
    void initShijuanName(@NotNull BaseViewHolder viewHolder, KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan) {
        // 设置试卷名称（即分类名称 - 试卷创建试卷）
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String part1 = kaoshiShijuan.getClassify_name() + " - ";
        String part2 = DateUtil.formateDate(kaoshiShijuan.getCreated_time(), DateUtil.PATTERN4);
        sb.append(part1).append(part2);
        // 字体大小设置
        sb.setSpan(new RelativeSizeSpan(0.8f), part1.length(), (part1 + part2).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(UIUtils.getResourceColor(mContext, R.color.gray1)), part1.length(), (part1 + part2).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        viewHolder.setText(R.id.shijuanName,sb);
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public interface CallbackListener {
        void onConfirm (KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan);
    }
}
