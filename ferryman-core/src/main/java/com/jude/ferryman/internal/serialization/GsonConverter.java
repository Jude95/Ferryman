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
        return mGson.toJson(object,type);
    }

    @Override
    public Object decode(Type type, String string) {
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
