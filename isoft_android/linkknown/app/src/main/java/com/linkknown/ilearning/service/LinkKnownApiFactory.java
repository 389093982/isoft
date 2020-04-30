package com.linkknown.ilearning.service;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LinkKnownApiFactory {

    private static LinkKnownService linkKnownService;
    private static String BASE_URL = "http://www.linkknown.com";

    public static void init (){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置
                .baseUrl(BASE_URL)
                .build();
        linkKnownService = retrofit.create(LinkKnownService.class);
    }

    public static synchronized LinkKnownService getLinkKnownService () {
        if (linkKnownService == null) {
            init();
        }
        return linkKnownService;
    }

}
