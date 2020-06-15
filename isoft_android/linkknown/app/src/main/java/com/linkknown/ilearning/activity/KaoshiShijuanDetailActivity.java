package com.linkknown.ilearning.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.KaoshiShijuanDetailResponse;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KaoshiShijuanDetailActivity extends BaseActivity {

    @BindView(R.id.timuIndexLayout)
    LinearLayout timuIndexLayout;

    @BindView(R.id.timu_question)
    TextView timu_question;
    @BindView(R.id.timu_answer_a)
    TextView timu_answer_a;
    @BindView(R.id.timu_answer_b)
    TextView timu_answer_b;
    @BindView(R.id.timu_answer_c)
    TextView timu_answer_c;
    @BindView(R.id.timu_answer_d)
    TextView timu_answer_d;
    @BindView(R.id.timu_answer_e)
    TextView timu_answer_e;
    @BindView(R.id.timu_answer_f)
    TextView timu_answer_f;

    @BindView(R.id.prefixView)
    TextView prefixView;
    @BindView(R.id.nextView)
    TextView nextView;

    private int currentTimuIndex = 0;

    private Context mContext;
    private int shijuan_id;
    private List<KaoshiShijuanDetailResponse.KaoshiShijuanDetail> kaoshiShijuanDetailList = new ArrayList<>();

    // 加载弹框
    private BasePopupView loadingPopupView;

    @BindView(R.id.answerProgress)
    TextView answerProgress;

    // 题目和底部布局,一开始隐藏,内容加载成功后显示
    @BindView(R.id.timuLayout)
    LinearLayout timuLayout;
    @BindView(R.id.footerLayout)
    LinearLayout footerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoshi_shijuan_detail);

        mContext = this;
        ButterKnife.bind(this);
        // 一开始隐藏页面所有内容，只显示加载对话框
        timuLayout.setVisibility(View.GONE);
        footerLayout.setVisibility(View.GONE);
        // 初始化加载弹框
        loadingPopupView = new XPopup.Builder(mContext).asLoading("试卷加载中...").show();

        shijuan_id = getIntent().getIntExtra("shijuan_id", -1);

        initView();

        // 加载对话框显示 2 s 后再发送网络请求
        new Handler().postDelayed(() -> initData(), 2000);
    }

    private void initData() {
        LinkKnownApiFactory.getLinkKnownApi().queryKaoshiShijuanDetailById(shijuan_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<KaoshiShijuanDetailResponse>() {
                    @Override
                    public void onNext(KaoshiShijuanDetailResponse o) {
                        if (o.isSuccess()) {
                            timuLayout.setVisibility(View.VISIBLE);
                            footerLayout.setVisibility(View.VISIBLE);

                            kaoshiShijuanDetailList.addAll(o.getKaoshi_shijuandetail());
                            initTimuIndexView();
                            initTimuInfo();
                        }
                        loadingPopupView.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingPopupView.dismiss();
                    }
                });
    }

    private void initView() {

    }

    private void initTimuIndexView() {
        for (int i=1; i<= kaoshiShijuanDetailList.size(); i++) {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.textview_common, timuIndexLayout, false);

            textView.setText(i + "");
            textView.setOnClickListener(v -> {
               currentTimuIndex = Integer.parseInt(textView.getText().toString()) - 1;
               initTimuInfo();
            });
            timuIndexLayout.addView(textView);
        }
    }

    private void initTimuInfo () {
        if (currentTimuIndex < kaoshiShijuanDetailList.size()) {
            KaoshiShijuanDetailResponse.KaoshiShijuanDetail detail = kaoshiShijuanDetailList.get(currentTimuIndex);

            String timuIndex = "(共 36 题，第 " + (currentTimuIndex + 1) + " 题，" + detail.getTimu_score() + " 分题) ";
            String timuQuestion = detail.getTimu_question();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(timuIndex).append(timuQuestion);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, timuIndex.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            timu_question.setText(spannableStringBuilder);
            timu_answer_a.setText(detail.getTimu_answer_a());
            timu_answer_b.setText(detail.getTimu_answer_b());
            timu_answer_c.setText(detail.getTimu_answer_c());
            timu_answer_d.setText(detail.getTimu_answer_d());
            timu_answer_e.setText(detail.getTimu_answer_e());
            timu_answer_f.setText(detail.getTimu_answer_f());

            // 设置答题进度
            answerProgress.setText((currentTimuIndex + 1) + "/" + kaoshiShijuanDetailList.size());

            initPrefixOrNextView();
        }
    }

    private void initPrefixOrNextView() {
        prefixView.setOnClickListener(v -> {
            currentTimuIndex --;
            initTimuInfo();
        });
        nextView.setOnClickListener(v -> {
            currentTimuIndex ++;
            initTimuInfo();
        });

        // 设置上一题的样式
        if (currentTimuIndex > 0) {
            prefixView.setClickable(true);
            prefixView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.light_orange));
            prefixView.setTextColor(UIUtils.getResourceColor(mContext, R.color.white));
        } else {
            prefixView.setClickable(false);
            prefixView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.gray));
            prefixView.setTextColor(UIUtils.getResourceColor(mContext, R.color.black));
        }

        // 设置下一题的样式
        if (currentTimuIndex < kaoshiShijuanDetailList.size() - 1) {
            nextView.setClickable(true);
            nextView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.light_red));
            nextView.setTextColor(UIUtils.getResourceColor(mContext, R.color.white));
        } else {
            nextView.setClickable(false);
            nextView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.gray));
            nextView.setTextColor(UIUtils.getResourceColor(mContext, R.color.black));
        }
    }
}
