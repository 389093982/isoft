package com.linkknown.ilearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.KaoshiClassifyResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KaoShiDetailActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    BaseQuickAdapter baseQuickAdapter;
    private List<KaoshiClassifyResponse.KaoshiClassify> kaoshiClassifies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_shi_detail);

        mContext = this;
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initKaoshiClassifys();
    }

    private void initKaoshiClassifys() {
        baseQuickAdapter = new BaseQuickAdapter<KaoshiClassifyResponse.KaoshiClassify, BaseViewHolder> (R.layout.item_kaoshi_classify, kaoshiClassifies){

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, KaoshiClassifyResponse.KaoshiClassify kaoshiClassify) {
                viewHolder.setText(R.id.kaoshiClassify, kaoshiClassify.getClassify_name());
                viewHolder.findView(R.id.kaoshiClassify).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 查询题库生成试卷
                        // 1 分题 x 10
                        // 2 分题 x 10
                        // 3 分题 x 10
                        // 5 分题 x 4
                        // 10 分题 x 2
                    }
                });
            }
        };
        baseQuickAdapter.addChildClickViewIds(R.id.kaoshiClassify);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(baseQuickAdapter);

        LinkKnownApiFactory.getLinkKnownApi().queryAllKaoshiClassify()
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<KaoshiClassifyResponse>() {
                    @Override
                    public void onNext(KaoshiClassifyResponse kaoshiClassifyResponse) {
                        if (kaoshiClassifyResponse.isSuccess()) {
                            kaoshiClassifies.addAll(kaoshiClassifyResponse.getKaoshi_classifys());
                            baseQuickAdapter.setList(kaoshiClassifies);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
