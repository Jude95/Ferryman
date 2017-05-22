package com.jude.ferryman.internal.router;

import com.jude.ferryman.internal.RouterMap;

/**
 * Created by zhuchenxi on 2017/1/17.
 */

public class ActivityRouterFactory extends Router.Factory {

    //路由表
    private RouterMap mRouterMap;

    public ActivityRouterFactory() {
        mRouterMap = new RouterMap();
    }

    @Override
    public Router createRouter(String url) {
        //查询开发者定义的url的scheme是否在路由表中，存在的话就表示可以处理
        Url mUrl = Url.parse(url);
        if (mRouterMap.queryTable(mUrl.getAddress()) != null) {
            return new ActivityRouter(mUrl, mRouterMap);
        }
        return null;
    }
}
