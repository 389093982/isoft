package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.FlowLayout;
import com.linkknown.ilearning.R;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class CommonTagSection extends Section {

    private List<String> tagList;
    private Context mContext;

    public CommonTagSection(Context mContext, List<String> tagList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_flow)
                .build());
        this.tagList = tagList;
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
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        viewHolder.flowLayout.removeAllViews();
        for (int i = 0; i < tagList.size(); i++) {
            TextView textView = (TextView) View.inflate(viewHolder.flowLayout.getContext(), R.layout.item_common_tag, null);
            textView.setText(tagList.get(i));
            viewHolder.flowLayout.addView(textView);
        }

//        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
//        multiTypeAdapter.register(String.class, new MultiItemView<String>() {
//            @NonNull
//            @Override
//            public int getLayoutId() {
//                return R.layout.item_common_tag;
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull String tag, int position) {
//                viewHolder.setText(R.id.tagName, tag);
//            }
//        });
//        multiTypeAdapter.setItems(tagList);
//
//
//        FlowLayout flowLayout = new FlowLayout(mContext);
//        viewHolder.recyclerView.setLayoutManager(flowLayout);
//        viewHolder.recyclerView.setAdapter(multiTypeAdapter);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private FlowLayout flowLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            flowLayout = itemView.findViewById(R.id.flowLayout);
        }
    }
}
