package com.jude.ferryman.record;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by Jude on 2018/1/11.
 */

public class ActivityRecorder {
    private String name;
    private long time;
    private WeakReference<Activity> instance;

    public ActivityRecorder(Activity activity) {
        this.instance = new WeakReference<Activity>(activity);
        this.name = activity.getClass().getCanonicalName();
        this.time = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public Activity getInstance() {
        return instance.get();
    }
}
