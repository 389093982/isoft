package com.linkknown.ilearning.service;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.fragment.ShowTypeDetailFragment;
import com.linkknown.ilearning.model.RegistResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

public class ShowTypeDetailService extends ViewModel {

    public static void loadBanner () {
        LiveEventBus.get("listBannerEntityWrapper", List.class).post(getBannerEntityWrapperData());
    }

    private static List<BannerEntityWrapper> getBannerEntityWrapperData () {
        BannerEntityWrapper wrapper = new BannerEntityWrapper();
        List<BannerEntity> bannerEntities = new ArrayList<>();
        BannerEntity bannerEntitie = new BannerEntity();
        bannerEntitie.setImg("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setTitle("测试11111");
        bannerEntitie.setLink("1111111");
        bannerEntities.add(bannerEntitie);
        bannerEntitie = new BannerEntity();
        bannerEntitie.setImg("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setTitle("测试11111");
        bannerEntitie.setLink("1111111");
        bannerEntities.add(bannerEntitie);
        bannerEntitie = new BannerEntity();
        bannerEntitie.setImg("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setTitle("测试11111");
        bannerEntitie.setLink("1111111");
        bannerEntities.add(bannerEntitie);
        wrapper.setBannerEntities(bannerEntities);
        return Arrays.asList(wrapper, wrapper, wrapper);
    }

    @Data
    public static class BannerEntityWrapper {
        public List<BannerEntity> bannerEntities;
    }

    @Data
    public static class BannerEntity {
        public String title;
        public String img;
        public String link;
    }
}
