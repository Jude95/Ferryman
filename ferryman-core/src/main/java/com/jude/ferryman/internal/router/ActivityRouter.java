package com.jude.ferryman.internal.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jude.ferryman.internal.RouterMap;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * Activity之间跳转的Router船夫
 * Created by zane on 2017/1/17.
 */

public class ActivityRouter implements Router {

    //路由映射关系的缓存
    private final Map<String, Class<? extends Activity>> routers;
    //路由表
    private RouterMap mRouterMap;
    private Url mUrl;

    public ActivityRouter(Url url, RouterMap routerMap) {
        routers = new HashMap<>();
        mUrl = url;
        this.mRouterMap = routerMap;
    }

    @Override
    public Intent start(@NonNull Context context, @NonNull String url) {
        Intent intent = new Intent();
        String address = mUrl.getAddress();

        Class<? extends Activity> target = routers.get(address);
        if (target == null) {
            target = mRouterMap.queryTable(mUrl.getAddress());
            if (target == null) {
                throw new NoSuchElementException(address);
            }
            routers.put(address, target);
        }

        intent.setClass(context, target);
        return intent;
    }
}
