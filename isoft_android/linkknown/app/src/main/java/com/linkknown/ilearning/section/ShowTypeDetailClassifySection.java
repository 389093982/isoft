package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.service.ShowTypeDetailService;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class ShowTypeDetailClassifySection extends Section {

    private List<ShowTypeDetailService.HotClassify> itemList;
    private Context mContext;

    public ShowTypeDetailClassifySection(Context mContext, List<ShowTypeDetailService.HotClassify> itemList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_recycleview)
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

        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(ShowTypeDetailService.HotClassify.class, new MultiItemView<ShowTypeDetailService.HotClassify>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_types_icon;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShowTypeDetailService.HotClassify hotClassify, int i) {
                viewHolder.setImageResource(R.id.item_icon, hotClassify.getClassifyImage());
                viewHolder.setText(R.id.item_title, hotClassify.getClassifyName());
                viewHolder.getConvertView().setOnClickListener(v -> ToastUtil.showText(mContext, "您点击的过快奥~~"));
            }
        });
        multiTypeAdapter.setItems(this.itemList);
        itemHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        itemHolder.recyclerView.setAdapter(multiTypeAdapter);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recyclerView);

        }
    }
}
