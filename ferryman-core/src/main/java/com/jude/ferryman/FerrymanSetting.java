package com.jude.ferryman;

import com.jude.ferryman.internal.router.ActivityRouterFactory;
import com.jude.ferryman.internal.router.Router;
import com.jude.ferryman.internal.router.RouterInterceptor;
import com.jude.ferryman.internal.serialization.Converter;
import com.jude.ferryman.internal.serialization.GsonConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zhuchenxi on 2017/1/17.
 */

public class FerrymanSetting {
    private static ArrayList<Router.Factory> sRouterFactories = new ArrayList<>();
    private static ArrayList<Converter.Factory> sConverterFactories = new ArrayList<>();
    private static ArrayList<RouterInterceptor> sUrlInterceptors = new ArrayList<>();
    private static ArrayList<RouterInterceptor> sAPIInterceptors = new ArrayList<>();
    static {
        sRouterFactories.add(new ActivityRouterFactory());
        sConverterFactories.add(new GsonConverter.GsonConverterFactory());
    }

    public static void addRouterFactory(Router.Factory factory){
        sRouterFactories.add(factory);
    }

    public static void addUrlInterceptors(RouterInterceptor interceptor){
        sUrlInterceptors.add(interceptor);
    }

    public static void addAPIInterceptors(RouterInterceptor interceptor){
        sAPIInterceptors.add(interceptor);
    }

    static String interceptUrl(String string){
        for (RouterInterceptor sUrlInterceptor : sUrlInterceptors) {
            string = sUrlInterceptor.intercept(string);
        }
        return string;
    }

    static String interceptAPI(String string){
        for (RouterInterceptor sUrlInterceptor : sAPIInterceptors) {
            string = sUrlInterceptor.intercept(string);
        }
        return string;
    }

    public static Router findRouter(String url){
        for (Router.Factory mRouterFactory : sRouterFactories) {
            Router router = mRouterFactory.createRouter(url);
            if (router != null){
                return router;
            }
        }
        throw new IllegalArgumentException("no Router find for this url: "+url);
    }

    public static Converter findConverter(Type type){
        for (Converter.Factory mConverterFactory : sConverterFactories) {
            Converter converter = mConverterFactory.createConverter(type);
            if (converter != null){
                return converter;
            }
        }
        throw new IllegalArgumentException("no Converter find for this type: "+type.toString());
    }

}
