package com.jude.ferryman;

import com.jude.ferryman.internal.router.ActivityRouterFactory;
import com.jude.ferryman.internal.router.Router;
import com.jude.ferryman.internal.serialization.Converter;
import com.jude.ferryman.internal.serialization.GsonConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zhuchenxi on 2017/1/17.
 */

public class FerrymanSetting {
    private static ArrayList<Router.Factory> mRouterFactories = new ArrayList<>();
    private static ArrayList<Converter.Factory> mConverterFactories = new ArrayList<>();

    static {
        mRouterFactories.add(new ActivityRouterFactory());
        mConverterFactories.add(new GsonConverter.GsonConverterFactory());
    }

    public static void addRouterFactory(Router.Factory factory){
        mRouterFactories.add(factory);
    }

    public static void addConverterFactory(Converter.Factory factory){
        mConverterFactories.add(factory);
    }

    public static Router findRouter(String url){
        for (Router.Factory mRouterFactory : mRouterFactories) {
            Router router = mRouterFactory.createRouter(url);
            if (router != null){
                return router;
            }
        }
        throw new IllegalArgumentException("no Page find for this url: "+url);
    }

    public static Converter findConverter(Type type){
        for (Converter.Factory mConverterFactory : mConverterFactories) {
            Converter converter = mConverterFactory.createConverter(type);
            if (converter != null){
                return converter;
            }
        }
        throw new IllegalArgumentException("no Converter find for this type: "+type.toString());
    }

}
