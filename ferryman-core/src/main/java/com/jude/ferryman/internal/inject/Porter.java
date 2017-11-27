package com.jude.ferryman.internal.inject;

import android.app.Activity;
import android.content.Intent;

import com.jude.ferryman.FerrymanSetting;
import com.jude.ferryman.internal.router.Router;
import com.jude.ferryman.internal.router.Url;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * Created by zhuchenxi on 2017/1/17.
 */

public abstract class Porter {

    public static Map<String,String> readParams(Activity activity){
        return  ((Url)activity.getIntent().getParcelableExtra(Router.REQUEST_DATA)).getParams();
    }

    public static void writeResult(Map<String,String> params, Activity activity){
        // 拿到上次保存的
        Url url = activity.getIntent().getParcelableExtra(Router.RESPONSE_DATA);
        Url.Builder builder = url == null?new Url.Builder():url.newBuilder();

        //添加新参数,Key相同会覆盖
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addParam(entry.getKey(),entry.getValue());
        }

        //保存并应用
        url = builder.build();
        activity.getIntent().putExtra(Router.RESPONSE_DATA,url);

        Intent intent = new Intent();
        intent.putExtra(Router.RESPONSE_DATA,url.toString());
        activity.setResult(Activity.RESULT_OK,intent);
    }

    public static <T> T toObject(Type type, String object){
        return (T) FerrymanSetting.findConverter(type).decode(type,object);
    }

    public static String fromObject(Type type, Object object){
        return FerrymanSetting.findConverter(type).encode(type,object);
    }
}
