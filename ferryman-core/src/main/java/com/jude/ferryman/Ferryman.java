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

    public static void unboxingData(Activity activity){
        new Injector(activity).to(activity);
    }

    public static void boxingData(Activity activity){
        new Siphon(activity).to(activity);
    }

    public static Injector unboxingDataFrom(Context ctx){
        return new Injector(ctx);
    }

    public static Siphon boxingDataIn(Object object){
        return new Siphon(object);
    }

}
