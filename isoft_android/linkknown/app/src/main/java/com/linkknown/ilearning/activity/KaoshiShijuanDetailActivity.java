package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.KaoshiShijuanDetailResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoshi_shijuan_detail);

        mContext = this;
        ButterKnife.bind(this);

        shijuan_id = getIntent().getIntExtra("shijuan_id", -1);

        initView();

        initData();
    }

    private void initData() {
        LinkKnownApiFactory.getLinkKnownApi().queryKaoshiShijuanDetailById(shijuan_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<KaoshiShijuanDetailResponse>() {
                    @Override
                    public void onNext(KaoshiShijuanDetailResponse o) {
                        if (o.isSuccess()) {
                            kaoshiShijuanDetailList.addAll(o.getKaoshi_shijuandetail());
                            initTimuIndexView();
                            initTimuInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initView() {

    }

    private void initTimuIndexView() {
        for (int i=1; i<= kaoshiShijuanDetailList.size(); i++) {
            AppCompatButton button = new AppCompatButton(mContext);
            button.setText(i + "");
            button.setOnClickListener(v -> {
               currentTimuIndex = Integer.parseInt(button.getText().toString()) - 1;
               initTimuInfo();
            });
            timuIndexLayout.addView(button);
        }
    }

    private void initTimuInfo () {
        if (currentTimuIndex < kaoshiShijuanDetailList.size()) {
            KaoshiShijuanDetailResponse.KaoshiShijuanDetail detail = kaoshiShijuanDetailList.get(currentTimuIndex);
            timu_question.setText(detail.getTimu_question());
            timu_answer_a.setText(detail.getTimu_answer_a());
            timu_answer_b.setText(detail.getTimu_answer_b());
            timu_answer_c.setText(detail.getTimu_answer_c());
            timu_answer_d.setText(detail.getTimu_answer_d());
            timu_answer_e.setText(detail.getTimu_answer_e());
            timu_answer_f.setText(detail.getTimu_answer_f());

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

        if (currentTimuIndex > 0) {
            prefixView.setClickable(true);
            prefixView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.red));
            prefixView.setTextColor(UIUtils.getResourceColor(mContext, R.color.white));
        } else {
            prefixView.setClickable(false);
            prefixView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.gray));
            prefixView.setTextColor(UIUtils.getResourceColor(mContext, R.color.black));
        }

        if (currentTimuIndex < kaoshiShijuanDetailList.size() - 1) {
            nextView.setClickable(true);
            nextView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.red));
            nextView.setTextColor(UIUtils.getResourceColor(mContext, R.color.white));
        } else {
            nextView.setClickable(false);
            nextView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.gray));
            nextView.setTextColor(UIUtils.getResourceColor(mContext, R.color.black));
        }
    }
}
