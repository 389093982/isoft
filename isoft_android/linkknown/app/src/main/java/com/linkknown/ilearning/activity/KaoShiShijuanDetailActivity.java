package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.common.LinkKnownOnNextObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.KaoshiShijuanDetailResponse;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.popup.KaoshiCenterPopupView;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class KaoShiShijuanDetailActivity extends BaseActivity{

    // 题目所在区域的滚动视图
    @BindView(R.id.timuScorllView)
    ScrollView timuScorllView;
    // 提示区域,用于辅助提示
    @BindView(R.id.tipInfo)
    TextView tipInfo;

    // 正确答案
    @BindView(R.id.timuAnswerView)
    TextView timuAnswerView;

    // 问题
    @BindView(R.id.timu_question)
    TextView timu_question;
    // 选项选择框
    @BindView(R.id.choiceA)
    CheckBox choiceA;
    @BindView(R.id.choiceB)
    CheckBox choiceB;
    @BindView(R.id.choiceC)
    CheckBox choiceC;
    @BindView(R.id.choiceD)
    CheckBox choiceD;
    @BindView(R.id.choiceE)
    CheckBox choiceE;
    @BindView(R.id.choiceF)
    CheckBox choiceF;
    // 选项值
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

    // 当前正在查看的题目索引
    private int currentTimuIndex = 0;

    private Context mContext;
    // 试卷 id
    private int shijuan_id;
    // 考试是否完成,分为已考完（true）和考试中（false）两种状态
    private boolean kaoshiCompleted;
    // 考试试卷xinxi
    KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan;

    // 存储考试试卷中的题目列表,每一道题目就是一个 KaoshiShijuanDetail
    private List<KaoshiShijuanDetailResponse.KaoshiShijuanDetail> kaoshiShijuanDetailList = new ArrayList<>();

    // 加载弹框,用于进入界面渲染初始的加载效果
    private BasePopupView loadingPopupView;

    // 底部答题进度 layout，包含底部答题进度 和 倒计时
    @BindView(R.id.answerProgressLayout)
    LinearLayout answerProgressLayout;
    // 底部答题进度
    @BindView(R.id.answerProgress)
    TextView answerProgress;
    // 倒计时
    @BindView(R.id.daojishi)
    TextView daojishi;


    // 题目和底部布局,一开始隐藏,内容加载成功后显示
    @BindView(R.id.timuLayout)
    LinearLayout timuLayout;
    @BindView(R.id.footerLayout)
    LinearLayout footerLayout;

    @BindView(R.id.ic_click)
    ImageView ic_click;

    // 考试倒计时
    private Disposable kaoshiTimeCostDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoshi_shijuan_detail);

        mContext = this;
        ButterKnife.bind(this);
        // 一开始隐藏页面所有内容，只显示加载对话框
        timuLayout.setVisibility(View.GONE);
        footerLayout.setVisibility(View.GONE);
        ic_click.setVisibility(View.GONE);
        // 初始化加载弹框
        loadingPopupView = new XPopup.Builder(mContext).asLoading("试卷加载中...").show();

        shijuan_id = getIntent().getIntExtra("shijuan_id", -1);
        kaoshiCompleted = getIntent().getBooleanExtra("kaoshiCompleted", false);
        kaoshiShijuan = (KaoshiShijuanListResponse.KaoshiShijuan) getIntent().getBundleExtra("bundle").getSerializable("kaoshiShijuan");
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
                            initTimuInfo();

                            // 显示点击手势图标提示
                            initClickTipView();
                            initKaoshiProgress();
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

    private void initTimuInfo () {
        if (currentTimuIndex < kaoshiShijuanDetailList.size()) {
            // 滚动组件滚动到顶部
            timuScorllView.fullScroll(ScrollView.FOCUS_UP);

            KaoshiShijuanDetailResponse.KaoshiShijuanDetail detail = kaoshiShijuanDetailList.get(currentTimuIndex);

            String timuIndex = "共 36 题，第 " + (currentTimuIndex + 1) + " 题，" + detail.getTimu_score() + " 分题";
            tipInfo.setText(timuIndex);
            tipInfo.setTextColor(UIUtils.getResourceColor(mContext, R.color.red));

            timu_question.setText(detail.getTimu_question());
            renderChoiceAndAnswer(detail.getTimu_answer_a(), StringUtils.contains(detail.getGiven_answer(), "A"), choiceA, timu_answer_a, "A：");
            renderChoiceAndAnswer(detail.getTimu_answer_b(), StringUtils.contains(detail.getGiven_answer(), "B"), choiceB, timu_answer_b, "B：");
            renderChoiceAndAnswer(detail.getTimu_answer_c(), StringUtils.contains(detail.getGiven_answer(), "C"), choiceC, timu_answer_c, "C：");
            renderChoiceAndAnswer(detail.getTimu_answer_d(), StringUtils.contains(detail.getGiven_answer(), "D"), choiceD, timu_answer_d, "D：");
            renderChoiceAndAnswer(detail.getTimu_answer_e(), StringUtils.contains(detail.getGiven_answer(), "E"), choiceE, timu_answer_e, "E：");
            renderChoiceAndAnswer(detail.getTimu_answer_f(), StringUtils.contains(detail.getGiven_answer(), "F"), choiceF, timu_answer_f, "F：");

            // 设置答题进度
            answerProgress.setText((currentTimuIndex + 1) + "/" + kaoshiShijuanDetailList.size());

            initPrefixOrNextView();

            initTimuAnswer(detail);
        }
    }

    // 显示题目正确答案
    private void initTimuAnswer(KaoshiShijuanDetailResponse.KaoshiShijuanDetail detail) {
        if (kaoshiCompleted) {
            timuAnswerView.setText(detail.getTimu_answer() + (detail.getIs_correct() == 1 ? " 恭喜你答对啦" : " 很遗憾你答错了，分数就这么溜走啦"));
            timuAnswerView.setTextColor(UIUtils.getResourceColor(mContext, detail.getIs_correct() == 1 ? R.color.green : R.color.red));
            timuAnswerView.setVisibility(View.VISIBLE);
        } else {
            timuAnswerView.setVisibility(View.GONE);
        }
    }

    // 渲染每一项答案
    private void renderChoiceAndAnswer(String answer, boolean checked, CheckBox checkBox, TextView textView, String prefix) {
        checkBox.setChecked(checked);
        if (StringUtils.isNotEmpty(StringUtils.trim(answer))) {
            textView.setText(prefix + answer);
            checkBox.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        // 考试考完查看试卷场景则不能点击
        checkBox.setClickable(!kaoshiCompleted);
    }

    // 记住当前选中的答案,并提交到远程服务器
    private void memoryCurrentAnswer (int is_completed) {
        // 考试中（考试未结束）
        if (!kaoshiCompleted) {
            KaoshiShijuanDetailResponse.KaoshiShijuanDetail kaoshiShijuanDetail = kaoshiShijuanDetailList.get(currentTimuIndex);
            StringBuilder sb = new StringBuilder();
            StringBuilder result = sb.append(choiceA.isChecked() ? "A" : "")
                    .append(choiceB.isChecked() ? "B" : "")
                    .append(choiceC.isChecked() ? "C" : "")
                    .append(choiceD.isChecked() ? "D" : "")
                    .append(choiceE.isChecked() ? "E" : "")
                    .append(choiceF.isChecked() ? "F" : "");

            kaoshiShijuanDetail.setGiven_answer(result.toString());
            if (kaoshiCenterPopupView != null) {
                kaoshiCenterPopupView.updateKaoshiTimuStatus();
            }

            LinkKnownApiFactory.getLinkKnownApi().updateKaoshiShijuanTimuAnswer(kaoshiShijuanDetail.getId(),
                    kaoshiShijuanDetail.getShijuan_id(), is_completed, kaoshiShijuanDetail.getGiven_answer())
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownOnNextObserver<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse o) {

                        }
                    });
        }
    }

    private BasePopupView progressPopupView;
    private KaoshiCenterPopupView kaoshiCenterPopupView;

    private void initClickTipView() {
        ic_click.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation1 = new TranslateAnimation(0,0,0,15);
        translateAnimation1.setDuration(1000);
        translateAnimation1.setInterpolator(new DecelerateInterpolator());
        translateAnimation1.setRepeatCount(-1);
        ic_click.startAnimation(translateAnimation1);
    }

    private void initKaoshiProgress() {
        kaoshiCenterPopupView = new KaoshiCenterPopupView(mContext, kaoshiShijuan,
                kaoshiShijuanDetailList, new KaoshiCenterPopupView.CallBackListener() {
            @Override
            public void updateKaoshiTimuWithIndex(int index) {
                // 先记住答案
                memoryCurrentAnswer(0);
                // 再切换其它题目
                currentTimuIndex = index;
                initTimuInfo();
            }

            @Override
            public void submitAll() {
                // 提交试卷
                handleSubmitKaoshiShijuan();
            }
        });
        progressPopupView = new XPopup.Builder(mContext).asCustom(kaoshiCenterPopupView);

        answerProgressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先清除动画再隐藏
                ic_click.clearAnimation();
                ic_click.setVisibility(View.GONE);

                memoryCurrentAnswer(0);
                progressPopupView.show();
            }
        });

        // 考试中才显示倒计时,查看试卷不显示
        if (!kaoshiCompleted) {
            daojishi.setVisibility(View.VISIBLE);
            // 计算考试时长
            // 定时任务定时修改 TextView 中的提示文字
            kaoshiTimeCostDisposable = Observable.interval(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())                   // 在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(aLong -> {
                        long totalTime = Constants.KAOSHI_TOTAL_TIME;
                        if (totalTime - aLong >= 0) {
                            kaoshiCenterPopupView.updateKaoshiTimeCost(totalTime - aLong);
                            daojishi.setText(DateUtil.secToMinuteAndSec((int)(totalTime - aLong)));

                            if (totalTime - aLong == 0){
                                // 倒计时结束则提交试卷
                                new XPopup.Builder(mContext).asConfirm("温馨提示", "考试试卷已结束,确认提交试卷？",
                                        () -> this.handleSubmitKaoshiShijuan(), () -> this.handleSubmitKaoshiShijuan()).show();
                            }
                        }
                    });
        } else {
            daojishi.setVisibility(View.GONE);
        }
    }

    // 记住答案并提交到远程服务器
    private void handleSubmitKaoshiShijuan() {
        memoryCurrentAnswer(1);

        UIUtils.gotoActivity(mContext, KaoShiShijuanScoreActivity.class, intent -> {
            intent.putExtra("shijuan_id", kaoshiShijuan.getId());
            return intent;
        });
        finish();
    }

    private void initPrefixOrNextView() {
        prefixView.setOnClickListener(v -> {
            memoryCurrentAnswer(0);
            currentTimuIndex --;
            initTimuInfo();
        });
        nextView.setOnClickListener(v -> {
            memoryCurrentAnswer(0);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (kaoshiTimeCostDisposable != null) {
            kaoshiTimeCostDisposable.dispose();
        }
    }


    //安卓重写返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            // 考试未结束返回则弹框提示提交
            if (!kaoshiCompleted) {
                progressPopupView.show();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return true;
    }

}
