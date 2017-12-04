package com.jude.ferryman.internal.serialization;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by zhuchenxi on 2017/1/17.
 */

public class GsonConverter implements Converter {
    Gson mGson = new Gson();

    @Override
    public String encode(Type type, Object object) {
        // 为了解决url方式携带的参数没有经过 json encode，导致解析出错。所以string 全都不 json encode
        if (type == String.class){
            return (String)object;
        }
        return mGson.toJson(object,type);
    }

    @Override
    public Object decode(Type type, String string) {
        // 为了解决url方式携带的参数没有经过 json encode，导致解析出错。所以string 全都不 json encode
        if (type == String.class){
            return string;
        }
        return mGson.fromJson(string,type);
    }

    public static class GsonConverterFactory extends Factory{
        GsonConverter instance;
        @Override
        public Converter createConverter(Type type) {
            if (instance == null){
                instance = new GsonConverter();
            }
            return instance;
        }
    }
}
