package com.linkknown.ilearning.popup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.internal.FlowLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.KaoShiShijuanScoreActivity;
import com.linkknown.ilearning.model.KaoshiShijuanDetailResponse;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class KaoshiCenterPopupView extends CenterPopupView {

    private Context mContext;
    private CallBackListener listener;
    private List<KaoshiShijuanDetailResponse.KaoshiShijuanDetail> kaoshiShijuanDetailList;
    KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan;

    private FlowLayout flowLayout;
    AppCompatButton submitAll;

    public KaoshiCenterPopupView(@NonNull Context context, KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan,
                                 List<KaoshiShijuanDetailResponse.KaoshiShijuanDetail> kaoshiShijuanDetailList, CallBackListener listener) {
        super(context);
        this.mContext = context;
        this.kaoshiShijuan = kaoshiShijuan;
        this.kaoshiShijuanDetailList = kaoshiShijuanDetailList;
        this.listener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        initSubmitView();

        initTimuIndexView();

        initRateView();
    }

    private void initRateView() {
        View rateLayout = findViewById(R.id.rateLayout);
        if (kaoshiShijuan.getIs_completed() == 1) {
            rateLayout.setVisibility(VISIBLE);

            TextView rateTextView = findViewById(R.id.rateTextView);
            if (kaoshiShijuan.getSum_score() >= 90) {
                rateTextView.setText(kaoshiShijuan.getSum_score() + "分，你太棒啦，考的这么好");
            } else if (kaoshiShijuan.getSum_score() >= 70) {
                rateTextView.setText(kaoshiShijuan.getSum_score() + "分，考的不错吆，不要骄傲");
            } else if (kaoshiShijuan.getSum_score() >= 60) {
                rateTextView.setText(kaoshiShijuan.getSum_score() + "分，刚刚及格奥，继续努力");
            } else {
                rateTextView.setText(kaoshiShijuan.getSum_score() + "分，没有认真考吧，下次争取考及格");
            }
        } else {
            rateLayout.setVisibility(GONE);
        }

        rateLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.gotoActivity(mContext, KaoShiShijuanScoreActivity.class, intent -> {
                    intent.putExtra("shijuan_id", kaoshiShijuan.getId());
                    return intent;
                });
            }
        });
    }

    private void initTimuIndexView() {
        flowLayout = findViewById(R.id.flowLayout);
        for (int i=1; i<= kaoshiShijuanDetailList.size(); i++) {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.textview_common, flowLayout, false);

            textView.setText(i + "");
            textView.setOnClickListener(v -> {
                listener.updateKaoshiTimuWithIndex(Integer.parseInt(textView.getText().toString()) - 1);
                dismiss();
            });
            flowLayout.addView(textView);
        }

        this.updateKaoshiTimuStatus();
    }

    private void initSubmitView() {
        submitAll = findViewById(R.id.submitAll);

        if (kaoshiShijuan.getIs_completed() == 1) {
            // 考试结束不显示提交按钮
            submitAll.setVisibility(GONE);
        } else {
            submitAll.setVisibility(VISIBLE);
            submitAll.setOnClickListener(v -> listener.submitAll());
        }

    }

    public void updateKaoshiTimeCost (long time) {
        if (submitAll != null) {
            submitAll.setText("交卷（" + DateUtil.secToMinuteAndSec((int)time) + "）");
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_kaoshi_progress;
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.6f);
    }

    public void updateKaoshiTimuStatus() {
        if (flowLayout != null) {
            for (int i=0; i<kaoshiShijuanDetailList.size(); i++) {
                TextView textView = (TextView) flowLayout.getChildAt(i);

                if (kaoshiShijuan.getIs_completed() == 1) {
                    // 查看试卷,正确显示绿色,错误显示红色
                    if (kaoshiShijuanDetailList.get(i).getIs_correct() == 1) {
                        textView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.green));
                    } else {
                        textView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.red));
                    }
                } else {
                    // 正在考试,已选择显示绿色，未选择显示灰色
                    if (StringUtils.isNotEmpty(kaoshiShijuanDetailList.get(i).getGiven_answer())) {
                        textView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.green));
                    } else {
                        textView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.gray));
                    }
                }
            }
        }
    }

    public interface CallBackListener {
        void updateKaoshiTimuWithIndex(int index);
        void submitAll();
    }
}
