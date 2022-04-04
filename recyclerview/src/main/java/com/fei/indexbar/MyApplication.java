package com.fei.indexbar;

import android.content.Context;

import com.fei.indexbar.util.CommonUtils;


/**
 * Created by Administrator on 2018/1/17.
 */

public class MyApplication extends android.app.Application {
    private static android.app.Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        CommonUtils.init(this);
        sApplication = this;
    }

    public static Context getContext() {
        return sApplication;
    }
}
