package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.service.ShowTypeDetailService;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class ShowTypeDetailClassifySection2 extends Section {

    private List<ShowTypeDetailService.HotClassify2> itemList;
    private Context mContext;

    public ShowTypeDetailClassifySection2(Context mContext, List<ShowTypeDetailService.HotClassify2> itemList) {
        super(SectionParameters.builder()
//                .itemResourceId(R.layout.layout_region_recommend_card_item)
                .itemResourceId(R.layout.layout_recycleview)
                .headerResourceId(R.layout.layout_region_recommend_hot_head)
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
        multiTypeAdapter.register(ShowTypeDetailService.HotClassify2.class, new MultiItemView<ShowTypeDetailService.HotClassify2>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.layout_region_recommend_card_item;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull ShowTypeDetailService.HotClassify2 hotClassify2, int i) {
                viewHolder.setImageResource(R.id.item_img, hotClassify2.getClassifyImage());
                viewHolder.setText(R.id.item_title, hotClassify2.getClassifyName());
                viewHolder.setText(R.id.item_play, "888");
                viewHolder.setText(R.id.item_review, "888");
            }
        });
        multiTypeAdapter.setItems(itemList);
        itemHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        itemHolder.recyclerView.setAdapter(multiTypeAdapter);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}
