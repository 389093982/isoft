package com.linkknown.ilearning.service;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.ShowTypeDetailFragment;
import com.linkknown.ilearning.model.RegistResponse;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.Data;

public class ShowTypeDetailService extends ViewModel {

    public static void loadData () {
        List<HotClassify> hotClassifyData = getHotClassifyData();
        List<HotClassify2> hotClassifyData2 = getHotClassifyData2();
        List<BannerEntityWrapper> bannerEntityWrapperData = getBannerEntityWrapperData();

        List<Object> lst = new ArrayList<>();
        lst.addAll(bannerEntityWrapperData);
        lst.addAll(hotClassifyData);
        lst.addAll(hotClassifyData2);
        LiveEventBus.get("showTypeDetails", List.class).post(lst);
    }

    private static List<HotClassify2> getHotClassifyData2 () {
        List<HotClassify2> lst = new ArrayList<>();
        HotClassify2 hotClassify = new HotClassify2();
        hotClassify.setClassifyName("美妆");
        hotClassify.setClassifyImage(R.drawable.icon_1);
        lst.add(hotClassify);
        hotClassify = new HotClassify2();
        hotClassify.setClassifyName("服饰");
        hotClassify.setClassifyImage(R.drawable.icon_2);
        lst.add(hotClassify);
        return lst;
    }

    private static List<HotClassify> getHotClassifyData () {
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
        return Arrays.asList(wrapper);
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
