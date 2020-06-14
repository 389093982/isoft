package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.api.LinkKnownApi;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.KaoshiShijuanDetailResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KaoshiShijuanDetailActivity extends BaseActivity {

    private Context mContext;
    private int shijuan_id;

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
                            ToastUtil.showText(mContext, o.getKaoshi_shijuandetail().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initView() {

    }
}
