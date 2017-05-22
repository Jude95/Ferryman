package com.jude.ferryman.internal.router;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by zhuchenxi on 2017/1/17.
 */

public interface Router {

    /**
     * activity返回值的key
     */
    String RESPONSE_DATA = "response_data";
    String REQUEST_DATA = "request_data";

    /**
     * 开船！
     * 路由唯一的一个方法，解析url，启动目标Activity。
     * 全都使用startActivityForResult的形式。(不知道有没有副作用...暂时这么设计吧）
     *
     * @param context 出发港口
     * @param url 目标Activity地址及数据
     */
    Intent start(@NonNull Context context, @NonNull String url);

    /**
     * 根据Url创建对应Router来处理。
     */
    public abstract class Factory{
        public abstract Router createRouter(String url);
    }
}
