package com.linkknown.ilearning.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.widget.RadarChartView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KaoShiShijuanScoreActivity extends BaseActivity {

    private KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan;

    // 试卷名称
    @BindView(R.id.shijuanName)
    TextView shijuanName;
    // 分类描述
    @BindView(R.id.classifyDesc)
    TextView classifyDesc;
    // 试卷考试开始和结束时间
    @BindView(R.id.kaoshiStartEndTime)
    TextView kaoshiStartEndTime;

    @BindView(R.id.viewShijuan)
    TextView viewShijuan;
    @BindView(R.id.radarChartView)
    RadarChartView radarChartView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_shi_shijuan_score);

        mContext = this;
        ButterKnife.bind(this);
        kaoshiShijuan = (KaoshiShijuanListResponse.KaoshiShijuan) getIntent().getSerializableExtra("kaoshiShijuan");

        initView();
    }

    private void initView() {
        // 设置试卷名称
        initShijuanName();
        // 设置试卷描述
        classifyDesc.setText(kaoshiShijuan.getClassify_desc());

        kaoshiStartEndTime.setText("实际提交时间：" + DateUtil.formateDate(kaoshiShijuan.getKaoshi_start_time(), DateUtil.PATTERN5) + " - " + DateUtil.formateDate(kaoshiShijuan.getKaoshi_end_time(), DateUtil.PATTERN5));

        // 点击按钮查看试卷
        initViewShijuanView();
        // 初始化雷达图
        initRadarChartView();
    }

    private void initShijuanName() {
        // 设置试卷名称（即分类名称 - 试卷创建试卷）
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String part1 = kaoshiShijuan.getClassify_name() + " - ";
        String part2 = DateUtil.formateDate(kaoshiShijuan.getCreated_time(), DateUtil.PATTERN4);
        sb.append(part1).append(part2);
        // 字体大小设置
        sb.setSpan(new RelativeSizeSpan(0.8f), part1.length(), (part1 + part2).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(UIUtils.getResourceColor(mContext, R.color.gray1)), part1.length(), (part1 + part2).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        shijuanName.setText(sb);
    }

    private void initViewShijuanView() {
        viewShijuan.setOnClickListener(v -> {
            // 去考试
            UIUtils.gotoActivity(mContext, KaoShiShijuanDetailActivity.class, intent -> {
                intent.putExtra("shijuan_id", kaoshiShijuan.getId());
                Bundle bundle = new Bundle();
                bundle.putSerializable("kaoshiShijuan", kaoshiShijuan);
                intent.putExtra("bundle", bundle);
                intent.putExtra("kaoshiCompleted", kaoshiShijuan.getIs_completed() == 1);
                return intent;
            });
        });
    }

    private void initRadarChartView() {
        String[] labels = new String[]{"勤奋", "天赋", "兴趣", "智力", "效果"};
        float[] values = new float[] {10, 20, 30, 40, 50};
        radarChartView.show(labels, values);
    }
}
