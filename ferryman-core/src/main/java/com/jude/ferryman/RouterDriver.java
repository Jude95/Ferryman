package com.jude.ferryman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.jude.ferryman.internal.router.Url;
import com.jude.ferryman.internal.router.result.ActivityResultHooker;
import com.jude.ferryman.internal.router.result.OnActivityResultListener;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.jude.ferryman.internal.router.Router.REQUEST_DATA;

/**
 * Created by zhuchenxi on 2017/1/18.
 */

public class RouterDriver {

    public static void startActivity(Context ctx, String url) {
        Intent intent = getIntent(ctx, FerrymanSetting.interceptUrl(url));
        startIntent(ctx, intent, null);
    }

    public static void startActivity(Context ctx, String url, OnActivityResultListener listener) {
        Intent intent = getIntent(ctx, FerrymanSetting.interceptUrl(url));
        startIntent(ctx, intent, listener);
    }

    static void startActivityFromAPI(Context ctx, String url) {
        Intent intent = getIntent(ctx, FerrymanSetting.interceptAPI(url));
        startIntent(ctx, intent, null);
    }

    static void startActivityFromAPI(Context ctx, String url, OnActivityResultListener listener) {
        Intent intent = getIntent(ctx, FerrymanSetting.interceptAPI(url));
        startIntent(ctx, intent, listener);
    }

    private static Intent getIntent(Context context, String url) {
        Intent intent = FerrymanSetting.findRouter(url).start(context, url);
        if (intent != null) {
            intent.putExtra(REQUEST_DATA, Url.parse(url));
            if (!(context instanceof Activity)) {
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            }
        }
        return intent;
    }

    private static void startIntent(Context context, Intent intent, OnActivityResultListener listener) {
        if (intent == null) {
            return;
        }
        if (context instanceof FragmentActivity && listener != null && !((FragmentActivity) context).getSupportFragmentManager().isDestroyed()) {
            ActivityResultHooker.startHookFragment((Activity) context, intent, listener);
        } else {
            context.startActivity(intent);
        }
    }
}
