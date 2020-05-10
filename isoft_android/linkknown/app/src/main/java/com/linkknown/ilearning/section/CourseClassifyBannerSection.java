package com.linkknown.ilearning.section;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.service.CourseClassifyService;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class CourseClassifyBannerSection extends Section {

    List<CourseClassifyService.BannerEntity> bannerEntities;

    public CourseClassifyBannerSection(List<CourseClassifyService.BannerEntity> bannerEntities) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_banner)
//                .headerResourceId(R.layout.section_header)
                .build());
        this.bannerEntities = bannerEntities;
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
        CourseClassifyBannerSection.ItemViewHolder itemHolder = (CourseClassifyBannerSection.ItemViewHolder) holder;
        Banner banner = itemHolder.banner;
        banner.clearAnimation();
        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        // 存放轮播图所有图片
        ArrayList<String> bannerImageList = new ArrayList<>();
        ArrayList<String> bannerTitleList = new ArrayList<>();
        //清空旧数据
        bannerImageList.clear();
        bannerTitleList.clear();
        if (bannerEntities != null){
            for (int index = 0; index < bannerEntities.size(); index++) {
                bannerImageList.add(bannerEntities.get(index).getBannerImage());
                bannerTitleList.add(bannerEntities.get(index).getBannerTitle());
            }
        }
        banner.setImages(bannerImageList);
        banner.setBannerTitles(bannerTitleList);
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