package com.jude.ferryman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jude.ferryman.internal.router.Url;
import com.jude.ferryman.internal.router.result.ActivityResultHooker;
import com.jude.ferryman.internal.router.result.OnActivityResultListener;

import static com.jude.ferryman.internal.router.Router.REQUEST_DATA;

/**
 * Created by zhuchenxi on 2017/1/18.
 */

public class RouterDriver {

    public static void startActivity(Context ctx, String url){
        Intent intent = getIntent(ctx, url);
        ActivityResultHooker.startHookFragment((Activity) ctx, intent, null);
    }

    public static void startActivity(Context ctx, String url, OnActivityResultListener listener){
        Intent intent = getIntent(ctx, url);
        ActivityResultHooker.startHookFragment((Activity) ctx, intent, listener);
    }

    private static Intent getIntent(Context context, String url) {
        Intent intent = FerrymanSetting.findRouter(url).start(context, url);
        intent.putExtra(REQUEST_DATA, Url.parse(url));
        return intent;
    }
}
