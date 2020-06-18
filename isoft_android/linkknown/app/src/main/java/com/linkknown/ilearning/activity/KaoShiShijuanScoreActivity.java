package com.linkknown.ilearning.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.KaoshiShijuanDetailResponse;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.widget.RadarChartView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KaoShiShijuanScoreActivity extends BaseActivity {

    private KaoshiShijuanListResponse.KaoshiShijuan kaoshiShijuan;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // 试卷名称
    @BindView(R.id.shijuanName)
    TextView shijuanName;
    // 分类描述
    @BindView(R.id.classifyDesc)
    TextView classifyDesc;
    @BindView(R.id.classifyImage)
    ImageView classifyImage;

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

        initData();
    }

    private void initData() {
                LinkKnownApiFactory.getLinkKnownApi().queryKaoshiShijuanDetailById(kaoshiShijuan.getId())
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<KaoshiShijuanDetailResponse>() {
                    @Override
                    public void onNext(KaoshiShijuanDetailResponse o) {
                        if (o.isSuccess()) {
                            // 显示雷达图
                            initRadarChartView(o.getKaoshi_shijuandetail());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initView() {
        initToolBar(toolbar, true, "试卷详情");

        // 设置试卷名称和分类信息
        initShijuanInfo();
        // 设置试卷描述
        classifyDesc.setText(kaoshiShijuan.getClassify_desc());

        if (StringUtils.isNotEmpty(kaoshiShijuan.getKaoshi_start_time()) && StringUtils.isNotEmpty(kaoshiShijuan.getKaoshi_end_time())) {
            String startTime = DateUtil.formateDate(DateUtil.getDate(kaoshiShijuan.getKaoshi_start_time()), DateUtil.PATTERN5);
            String endTime = DateUtil.formateDate(DateUtil.getDate(kaoshiShijuan.getKaoshi_end_time()), DateUtil.PATTERN5);
            kaoshiStartEndTime.setText("实际考试时间：" + startTime + " - " + endTime);
        }
                // 点击按钮查看试卷
        initViewShijuanView();
    }

    private void initShijuanInfo() {
        // 设置试卷名称（即分类名称 - 试卷创建试卷）
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String part1 = kaoshiShijuan.getClassify_name() + " - ";
        String part2 = DateUtil.formateDate(kaoshiShijuan.getCreated_time(), DateUtil.PATTERN4);
        sb.append(part1).append(part2);
        // 字体大小设置
        sb.setSpan(new RelativeSizeSpan(0.8f), part1.length(), (part1 + part2).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(UIUtils.getResourceColor(mContext, R.color.gray1)), part1.length(), (part1 + part2).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        shijuanName.setText(sb);
        UIUtils.setImage(mContext, classifyImage, kaoshiShijuan.getClassify_image());
    }

    private void initViewShijuanView() {
        viewShijuan.setOnClickListener(v -> {
            // 去考试
            UIUtils.gotoActivity(mContext, KaoShiShijuanDetailActivity.class, intent -> {
                kaoshiShijuan.setIs_completed(1);
                intent.putExtra("shijuan_id", kaoshiShijuan.getId());
                Bundle bundle = new Bundle();
                bundle.putSerializable("kaoshiShijuan", kaoshiShijuan);
                intent.putExtra("bundle", bundle);
                intent.putExtra("kaoshiCompleted", true);
                return intent;
            });
        });
    }

    private void initRadarChartView(List<KaoshiShijuanDetailResponse.KaoshiShijuanDetail> details) {
        String[] labels = new String[]{"勤奋", "天赋", "兴趣", "智力", "效果"};

        // 勤奋值(填写过答案的值)
        int qinfen = 0;
        // 天赋值(答对的值)
        int tianfu = 0;
        // 兴趣值(勤奋值和天赋值得平均值)
        int xingqu = 0;
        // 智力值
        int zhili = 80 + new Random().nextInt(20);
        int xiaoguo = kaoshiShijuan.getSum_score();
        for (KaoshiShijuanDetailResponse.KaoshiShijuanDetail detail : details){
            if (StringUtils.isNotEmpty(detail.getGiven_answer())) {
                qinfen ++;
            }
            if (detail.getIs_correct() == 1){
                tianfu ++;
            }
        }

        float[] values = new float[] {qinfen / (float) details.size() * 100,
                tianfu / (float)details.size() * 100,
                (qinfen + tianfu) / (float)(2 * details.size())* 100,
                zhili,
                xiaoguo};

        for (int i=0; i<values.length; i++){
            if (values[i] + 50 < 100){
                // 若分数小于 50 分则直接添加 50 分
                values[i] = values[i] + 50;
            }
        }
        radarChartView.show(labels, values);
    }
}
