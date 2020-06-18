package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.widget.RadarChartView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KaoShiShijuanScoreActivity extends BaseActivity {

    private KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan;

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
        // 点击按钮查看试卷
        initViewShijuanView();
        // 初始化雷达图
        initRadarChartView();
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
