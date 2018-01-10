package com.jude.ferryman.internal.router.result;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by zane on 2017/1/17.
 */

public class ActivityResultHooker {

    private static final String TAG = "FerrymanActivityResultHooker";

    /**
     * 适用于startActivityForResult()的两个重写的方法的hook启动
     * @param activity
     */
    public static void startHookFragment(Activity activity, Intent rawIntent, OnActivityResultListener listener){
        HookFragment fragment = getValidFragment((FragmentActivity) activity);
        fragment.startActivityForResult(rawIntent, listener);
    }

    /**
     * 获取activity里面可能存在的hookfragment，不存在的话就新实例一个
     * @param activity
     * @return
     */
    private static HookFragment getValidFragment(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        HookFragment mHookFragment = (HookFragment) manager.findFragmentByTag(TAG);
        boolean isNewInstance = mHookFragment == null;

        if (isNewInstance) {
            mHookFragment = new HookFragment();
            manager.beginTransaction()
                    .add(mHookFragment, TAG)
                    .commitAllowingStateLoss();
            manager.executePendingTransactions();
        }

        return mHookFragment;
    }

}
