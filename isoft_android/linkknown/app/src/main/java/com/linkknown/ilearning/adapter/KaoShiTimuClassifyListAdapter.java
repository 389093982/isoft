package com.linkknown.ilearning.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.KaoShiResultListActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.KaoshiClassifyResponse;
import com.linkknown.ilearning.model.KaoshiShijuanListResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KaoShiTimuClassifyListAdapter extends BaseQuickAdapter<KaoshiClassifyResponse.KaoshiClassify, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public KaoShiTimuClassifyListAdapter(Context mContext, List<KaoshiClassifyResponse.KaoshiClassify> kaoshiClassifies) {
        super(R.layout.item_kaoshi_classify, kaoshiClassifies);
        this.mContext = mContext;
    }

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
