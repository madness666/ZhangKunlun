package com.kunlun.breadtrip.base;

import android.app.Application;
import android.content.Context;


import android.app.Application;
import android.content.Context;

/**
 * Created by dllo on 16/4/14.
 * 我们自己的Application
 * 在使用的时候,需要在清单文件中注册
 */
public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
