package com.linkknown.ilearning.service;

import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class ShowTypeDetailService extends ViewModel {

    public static void loadData() {
        List<HotClassify> hotClassifyData = getHotClassifyData();
        List<HotClassify2> hotClassifyData2 = getHotClassifyData2();
        List<BannerEntity> bannerEntities = getBannerEntitys();

        List<Object> lst = new ArrayList<>();
        lst.addAll(bannerEntities);
        lst.addAll(hotClassifyData);
        lst.addAll(hotClassifyData2);
        LiveEventBus.get("showTypeDetails", List.class).post(lst);
    }

    private static List<HotClassify2> getHotClassifyData2() {
        List<HotClassify2> lst = new ArrayList<>();
        HotClassify2 hotClassify = new HotClassify2();
        hotClassify.setClassifyName("美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆");
        hotClassify.setClassifyImage(R.drawable.icon_1);
        lst.add(hotClassify);
        hotClassify = new HotClassify2();
        hotClassify.setClassifyName("服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰服饰");
        hotClassify.setClassifyImage(R.drawable.icon_2);
        lst.add(hotClassify);
        return lst;
    }

    private static List<HotClassify> getHotClassifyData() {
        List<HotClassify> lst = new ArrayList<>();
        HotClassify hotClassify = new HotClassify();
        hotClassify.setClassifyName("美妆");
        hotClassify.setClassifyImage(R.drawable.icon_1);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("服饰");
        hotClassify.setClassifyImage(R.drawable.icon_2);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("资讯");
        hotClassify.setClassifyImage(R.drawable.icon_3);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("健身");
        hotClassify.setClassifyImage(R.drawable.icon_1);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("美妆");
        hotClassify.setClassifyImage(R.drawable.icon_1);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("服饰");
        hotClassify.setClassifyImage(R.drawable.icon_2);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("资讯");
        hotClassify.setClassifyImage(R.drawable.icon_3);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("健身");
        hotClassify.setClassifyImage(R.drawable.icon_1);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("资讯");
        hotClassify.setClassifyImage(R.drawable.icon_3);
        lst.add(hotClassify);
        hotClassify = new HotClassify();
        hotClassify.setClassifyName("健身");
        hotClassify.setClassifyImage(R.drawable.icon_1);
        lst.add(hotClassify);
        return lst;
    }

    private static List<BannerEntity> getBannerEntitys() {
        List<BannerEntity> bannerEntities = new ArrayList<>();
        BannerEntity bannerEntitie = new BannerEntity();
        bannerEntitie.setBannerImage("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setBannerTitle("测试11111");
        bannerEntitie.setBannerLink("1111111");
        bannerEntities.add(bannerEntitie);
        bannerEntitie = new BannerEntity();
        bannerEntitie.setBannerImage("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setBannerTitle("测试11111");
        bannerEntitie.setBannerLink("1111111");
        bannerEntities.add(bannerEntitie);
        bannerEntitie = new BannerEntity();
        bannerEntitie.setBannerImage("https://img-blog.csdn.net/20180420104431654");
        bannerEntitie.setBannerTitle("测试11111");
        bannerEntitie.setBannerLink("1111111");
        bannerEntities.add(bannerEntitie);
        return bannerEntities;
    }

    @Data
    public static class HotClassify2 {
        public String classifyName;
        public int classifyImage;
    }

    @Data
    public static class HotClassify {
        public String classifyName;
        public int classifyImage;
    }

    @Data
    public static class BannerEntity {
        public String bannerTitle;
        public String bannerImage;
        public String bannerLink;
    }
}
