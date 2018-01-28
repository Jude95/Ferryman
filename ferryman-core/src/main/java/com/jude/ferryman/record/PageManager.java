package com.jude.ferryman.record;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by Jude on 2018/1/11.
 */

public class PageManager {
    private static Stack<ActivityRecorder> mActivityStack = new Stack<>();
    private static volatile boolean INIT = false;

    /**
     * 需要在第一个activity启动前注册
     * @param context
     */
    public static void init(Context context){
        if (!INIT){
            INIT = true;
            Application application = (Application) context.getApplicationContext();
            application.registerActivityLifecycleCallbacks(new ApplicationStateListener());
        }
    }

    static void  onOpenPage(Activity activity){
        mActivityStack.push(new ActivityRecorder(activity));
    }

    static void onClosePage(Activity activity){
        Iterator<ActivityRecorder> iterable = mActivityStack.iterator();
        while (iterable.hasNext()){
            ActivityRecorder activityRecorder = iterable.next();
            if (activity == activityRecorder.getInstance()){
                iterable.remove();
            }
        }
    }

    /**
     * 获取顶部Activity
     * @return
     */
    @Nullable
    public static Activity getTopActivity(){
        ActivityRecorder activityRecorder = null;
        if (!mActivityStack.isEmpty()){
            activityRecorder = mActivityStack.peek();
        }
        return activityRecorder == null ? null : activityRecorder.getInstance();
    }

    /**
     * 取最近的指定class 的Activity
     * @param activityClass
     * @return
     */
    @Nullable
    public static Activity getTopActivity(Class<? extends Activity> activityClass){
        return getTopActivity(activityClass.getName());
    }

    @Nullable
    public static Activity getTopActivity(String activityName) {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            ActivityRecorder recorder = mActivityStack.get(i);
            if (recorder.getName().equals(activityName)){
                return recorder.getInstance();
            }
        }
        return null;
    }

    /**
     * 取指定 class 的 Activity 深度.
     * 不存在则返回 -1
     * @param activityClass
     * @return
     */
    public static int getDeep(Class<? extends Activity> activityClass){
        return getDeep(activityClass.getName());
    }

    public static int getDeep(String activityName){
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            ActivityRecorder recorder = mActivityStack.get(i);
            if (recorder.getName().equals(activityName)){
                return mActivityStack.size() - 1 - i;
            }
        }
        return -1;
    }

    /**
     * 关闭所有的Activity 直到指定 class 的Activity
     * @param activityClass
     * @return
     */
    @Nullable
    public static Activity closeToLastActivity(Class<? extends Activity> activityClass){
        return closeToLastActivity(activityClass.getName());
    }

    @Nullable
    public static Activity closeToLastActivity(String activityName){
        ActivityRecorder activityRecorder = null;
        while (!mActivityStack.empty() && !(activityRecorder = mActivityStack.peek()).getName().equals(activityName)){
            if (activityRecorder.getInstance()!=null){
                activityRecorder.getInstance().finish();
            }
            mActivityStack.pop();
        }
        return activityRecorder == null ? null : activityRecorder.getInstance();
    }

    /**
     * 关闭所有Activity
     */
    public static void clearAllStack(){
        while (!mActivityStack.empty()){
            Activity activity = mActivityStack.pop().getInstance();
            if (activity!=null){
                activity.finish();
            }
        }
    }

    /**
     * 打印当前Activity堆栈，暂未对不同栈的Activity分开计算
     * @return
     */
    public static String printPageStack(){
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss.SSS", Locale.CHINA);

        for (int i = mActivityStack.size()-1; i >= 0; i--) {
            ActivityRecorder activityRecorder = mActivityStack.get(i);
            stringBuilder.append(format.format(new Date(activityRecorder.getTime()))).append(": ").append(activityRecorder.getName()).append("\n");
        }

        return stringBuilder.toString();
    }

}
