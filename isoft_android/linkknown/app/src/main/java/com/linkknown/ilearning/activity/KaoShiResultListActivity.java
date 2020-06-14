package com.linkknown.ilearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
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
                viewHolder.setText(R.id.shijuanName, kaoshiShijuan.getClassify_name() + kaoshiShijuan.getCreated_time().toString());
                viewHolder.findView(R.id.shijuanName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.gotoActivity(mContext, KaoshiShijuanDetailActivity.class, new UIUtils.IntentParamWrapper() {
                            @Override
                            public Intent wrapper(Intent intent) {
                                intent.putExtra("shijuan_id", kaoshiShijuan.getId());
                                return intent;
                            }
                        });
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(baseQuickAdapter);
    }
}
