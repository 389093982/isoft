package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.service.CourseClassifyService;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class CourseClassifyQuickSection extends Section {

    private List<CourseClassifyService.HotClassify> itemList;
    private Context mContext;

    public CourseClassifyQuickSection(Context mContext, List<CourseClassifyService.HotClassify> itemList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_recycleview_bg_white)
                .build());
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter<CourseClassifyService.HotClassify, BaseViewHolder> (R.layout.item_types_icon, this.itemList) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, CourseClassifyService.HotClassify hotClassify) {
                viewHolder.setImageResource(R.id.item_icon, hotClassify.getClassifyImage());
                viewHolder.setText(R.id.item_title, hotClassify.getClassifyName());
                viewHolder.itemView.setOnClickListener(v -> ToastUtil.showText(mContext, "您点击的过快奥~~"));
            }
        };


        itemHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        itemHolder.recyclerView.setAdapter(baseQuickAdapter);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recyclerView);

        }
    }
}
