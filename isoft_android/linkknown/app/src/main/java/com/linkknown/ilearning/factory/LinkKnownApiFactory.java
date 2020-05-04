package com.linkknown.ilearning.factory;

import com.linkknown.ilearning.interceptor.HttpLogInterceptor;
import com.linkknown.ilearning.interceptor.TokenHeaderInterceptor;
import com.linkknown.ilearning.api.LinkKnownApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LinkKnownApiFactory {

    private static LinkKnownApi linkKnownApi;
    private static String BASE_URL = "http://192.168.1.2:6001";
//    private static String BASE_URL = "http://www.linkknown.com";

    public static void init (){
        // okhttp设置部分,此处还可再设置网络参数
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenHeaderInterceptor())
                .addInterceptor(new HttpLogInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 新的配置
                .baseUrl(BASE_URL)
                .client(client) //此 client 是为了打印信息
                .build();

        linkKnownApi = retrofit.create(LinkKnownApi.class);
    }

    public static synchronized LinkKnownApi getLinkKnownApi () {
        if (linkKnownApi == null) {
            init();
        }
        return linkKnownApi;
    }
}
