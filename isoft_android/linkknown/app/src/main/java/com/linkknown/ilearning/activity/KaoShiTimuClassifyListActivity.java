package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.KaoshiClassifyResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KaoShiTimuClassifyListActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    BaseQuickAdapter baseQuickAdapter;
    private List<KaoshiClassifyResponse.KaoshiClassify> kaoshiClassifies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoshi_timu_classify_list);

        mContext = this;
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initToolBar(toolbar, true, "试卷分类");
        initKaoshiClassifys();
    }

    private void initKaoshiClassifys() {
        baseQuickAdapter = new BaseQuickAdapter<KaoshiClassifyResponse.KaoshiClassify, BaseViewHolder> (R.layout.item_kaoshi_classify, kaoshiClassifies){

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, KaoshiClassifyResponse.KaoshiClassify kaoshiClassify) {
                viewHolder.setText(R.id.classifyName, kaoshiClassify.getClassify_name());
                viewHolder.setText(R.id.classifyDesc, kaoshiClassify.getClassify_desc());
                UIUtils.setImage(mContext, viewHolder.findView(R.id.classifyImage), kaoshiClassify.getClassify_image());
                viewHolder.findView(R.id.cardView).setOnClickListener(v -> {
                    new XPopup.Builder(mContext).asConfirm("温馨提示", "是否参与 " + kaoshiClassify.getClassify_name() + " 考试？",
                            // 确认后前去生成考试试卷
                            () -> toCreateShijuan(kaoshiClassify)).show();
                });
            }
        };
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

    // 前去生成试卷
    private void toCreateShijuan(KaoshiClassifyResponse.KaoshiClassify kaoshiClassify) {
        // 查询题库生成试卷
        // 1 分题 x 10
        // 2 分题 x 10
        // 3 分题 x 10
        // 5 分题 x 4
        // 10 分题 x 26
        LinkKnownApiFactory.getLinkKnownApi().createKaoshiShijuan(kaoshiClassify.getClassify_name())
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse o) {
                        if (o.isSuccess()) {
                            ToastUtil.showText(mContext, "试卷生成成功!");
                            UIUtils.gotoActivity(mContext, KaoShiResultListActivity.class);
                        } else {
                            ToastUtil.showText(mContext, o.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
