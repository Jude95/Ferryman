package com.jude.ferryman;

import android.app.Activity;
import android.content.Context;

import com.jude.ferryman.internal.Injector;
import com.jude.ferryman.internal.Siphon;

/**
 * Created by zhuchenxi on 2017/2/5.
 */

public class Ferryman {
    public static Boat from(Context ctx){
        return new Boat(ctx);
    }

    public static void inject(Activity activity){
        new Injector(activity).to(activity);
    }

    public static void save(Activity activity){
        new Siphon(activity).to(activity);
    }

    public static Injector injectFrom(Context ctx){
        return new Injector(ctx);
    }

    public static Siphon saveFrom(Object object){
        return new Siphon(object);
    }

}
