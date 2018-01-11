package com.jude.ferrymandemo;

import android.app.Application;

import com.jude.ferryman.record.PageManager;

/**
 * Created by Jude on 2018/1/11.
 */

public class SimpleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PageManager.init(this);
    }
}
