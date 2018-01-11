package com.jude.ferryman.record;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by Jude on 2017/8/17.
 */

public class ApplicationStateListener implements Application.ActivityLifecycleCallbacks   {


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        PageManager.onOpenPage(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        PageManager.onClosePage(activity);
    }
}
