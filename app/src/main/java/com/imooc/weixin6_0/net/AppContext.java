package com.imooc.weixin6_0.net;

import android.app.Application;

/**
 * Created by apple on 15/8/14.
 */
public class AppContext extends Application {

    private static AppContext instance;

    public
    static AppContext getInstance() {

        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance  = this;
    }

}
