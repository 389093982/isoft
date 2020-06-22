package com.linkknown.ilearning.common;

import android.app.Application;
import android.content.Context;

import com.chad.library.adapter.base.module.LoadMoreModuleConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

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

        // 初始化全局日志配置
        initGlobalLoggerConfig();
    }

    private void initGlobalLoggerConfig() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)   // 是否显示线程信息，默认为ture
                .methodCount(3)          // 显示的方法行数，默认为2
                .methodOffset(7)        // 隐藏内部方法调用到偏移量，默认为5
//                .logStrategy(customLog) // 更改要打印的日志策略
                .tag("linkknown")       // 每个日志的全局标记。默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    public static Context getContext() {
        return mContext;
    }

}
