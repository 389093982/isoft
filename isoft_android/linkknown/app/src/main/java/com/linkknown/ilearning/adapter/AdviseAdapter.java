package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.AdviseListResponse;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdviseAdapter extends BaseQuickAdapter<AdviseListResponse.Advise, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public AdviseAdapter(Context mContext, List<AdviseListResponse.Advise> advises) {
        super(R.layout.item_advise_info, advises);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, AdviseListResponse.Advise advise) {
        viewHolder.setText(R.id.adviseText, StringUtils.trim(advise.getAdvise()));
        viewHolder.setText(R.id.dateTimeView, DateUtil.formateDate(advise.getCreated_time()));
    }
};