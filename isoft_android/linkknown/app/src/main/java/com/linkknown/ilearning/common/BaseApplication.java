package com.linkknown.ilearning.common;

import android.app.Application;
import android.content.Context;

import com.chad.library.adapter.base.module.LoadMoreModuleConfig;

// 主要是为了在工具类中方便获取全局 application 级别的 Context
// 需要在清单文件中的 <application下加一个属性 android:name=".BaseApplication"
// 这样就可以在工具类中获取context了
public class BaseApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        // 在 Application 中配置全局自定义的 LoadMoreView, 全局设置后就不用各处单独设置了
        LoadMoreModuleConfig.setDefLoadMoreView(new CommonLoadMoreView());
    }

    public static Context getContext() {
        return mContext;
    }

}
