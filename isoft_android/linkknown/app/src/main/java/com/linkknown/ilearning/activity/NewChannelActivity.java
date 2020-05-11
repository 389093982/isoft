package com.linkknown.ilearning.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.section.ChannelMineSection;
import com.linkknown.ilearning.section.ChannelOtherSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class NewChannelActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        mContext = this;
        ButterKnife.bind(this);

        init();
    }

    private void init () {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        initToolBar(toolbar, true, "拖拽排序");

        final List<ChannelBean> enableItems = new ArrayList<>(Arrays.asList(
                new ChannelBean("测试","测试1",1,1),
                new ChannelBean("测试","测试2",1,1),
                new ChannelBean("测试","测试3",1,1),
                new ChannelBean("测试","测试4",1,1),
                new ChannelBean("测试","测试5",1,1),
                new ChannelBean("测试","测试6",1,1),
                new ChannelBean("测试","测试7",1,1)
        ));
        final List<ChannelBean> disableItems = new ArrayList<>(Arrays.asList(
                new ChannelBean("测试","测试8",1,1),
                new ChannelBean("测试","测试9",1,1),
                new ChannelBean("测试","测试10",1,1),
                new ChannelBean("测试","测试11",1,1),
                new ChannelBean("测试","测试12",1,1),
                new ChannelBean("测试","测试13",1,1),
                new ChannelBean("测试","测试14",1,1)
        ));

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);

        SectionedRecyclerViewAdapter adapter = new SectionedRecyclerViewAdapter();
        adapter.addSection(new ChannelMineSection(this, enableItems, disableItems, adapter));
        adapter.addSection(new ChannelOtherSection(this, enableItems, disableItems, adapter));
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getSectionItemViewType(position);
                return viewType == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER ? 4 : 1;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ChannelBean {
        private String channelId;
        private String channelName;
        private int isEnable;
        private int position;
    }
}
