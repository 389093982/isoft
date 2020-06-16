package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KaoShiResultListActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // 渲染试卷列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<KaoshiShijuanListResponse.KaoshiShijuan> kaoshiShijuans = new ArrayList<>();
    private BaseQuickAdapter baseQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_shi_result_list);

        mContext = this;
        ButterKnife.bind(this);

        initView();
        
        initData();
    }

    private void initData() {
        LinkKnownApiFactory.getLinkKnownApi().queryPageKaoshiShijuan(1, 10)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<KaoshiShijuanListResponse>() {
                    @Override
                    public void onNext(KaoshiShijuanListResponse o) {
                        if (o.isSuccess()){
                            kaoshiShijuans.addAll(o.getKaoshi_shijuans());
                            baseQuickAdapter.setList(kaoshiShijuans);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initView() {
        initToolBar(toolbar, true, "我的试卷");

        initShijuanList();
    }

    private void initShijuanList() {
        baseQuickAdapter = new BaseQuickAdapter<KaoshiShijuanListResponse.KaoshiShijuan, BaseViewHolder>(R.layout.item_kaoshi_shijuan_list, kaoshiShijuans) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan) {
                viewHolder.setText(R.id.shijuanName, kaoshiShijuan.getClassify_name() + "-" + DateUtil.formateDate(kaoshiShijuan.getCreated_time(), DateUtil.PATTERN4));
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
                    textView.setTranslationY(-35);
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

                viewHolder.findView(R.id.shijuanName).setOnClickListener(v -> UIUtils.gotoActivity(mContext, KaoshiShijuanDetailActivity.class, intent -> {
                    intent.putExtra("shijuan_id", kaoshiShijuan.getId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("kaoshiShijuan", kaoshiShijuan);
                    intent.putExtra("bundle", bundle);
                    intent.putExtra("kaoshiCompleted", kaoshiShijuan.getIs_completed() == 1);
                    return intent;
                }));
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(baseQuickAdapter);
    }
}
