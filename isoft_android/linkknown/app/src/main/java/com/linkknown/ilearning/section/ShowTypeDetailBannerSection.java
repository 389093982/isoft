package com.linkknown.ilearning.section;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.service.ShowTypeDetailService;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class ShowTypeDetailBannerSection extends Section {

    List<ShowTypeDetailService.BannerEntityWrapper> bannerEntityWrappers;

    public ShowTypeDetailBannerSection(List<ShowTypeDetailService.BannerEntityWrapper> bannerEntityWrappers) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_banner)
//                .headerResourceId(R.layout.section_header)
                .build());
        this.bannerEntityWrappers = bannerEntityWrappers;
    }

    @Override
    public int getContentItemsTotal() {
        return bannerEntityWrappers.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShowTypeDetailBannerSection.ItemViewHolder itemHolder = (ShowTypeDetailBannerSection.ItemViewHolder) holder;
        Banner banner = itemHolder.banner;
        banner.clearAnimation();
        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        // 存放轮播图所有图片
        ArrayList<String> bannerImageList = new ArrayList<>();
        //清空旧数据
        bannerImageList.clear();
        ShowTypeDetailService.BannerEntityWrapper bannerEntityWrapper = bannerEntityWrappers.get(position);
        if (bannerEntityWrapper != null){
            for (int index = 0; index < bannerEntityWrapper.bannerEntities.size(); index++) {
                bannerImageList.add(bannerEntityWrapper.getBannerEntities().get(index).getImg());
            }
        }
        banner.setImages(bannerImageList);
        banner.start();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final Banner banner;

        public ItemViewHolder(View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner);
        }
    }
}
