package com.jude.ferryman.module.collect;

import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.framework.ContextReader;
import com.jude.ferryman.module.framework.JarContentProvider;
import com.jude.ferryman.module.framework.TransformContext;
import com.jude.ferryman.module.log.Log;

/**
 * Created by Jude on 2017/11/20.
 */

public class InfoCollector {
    TransformContext transformContext;

    public InfoCollector(TransformContext transformContext) {
        this.transformContext = transformContext;
    }

    public FerrymanInfo readInfo() {
        ContextReader reader = new ContextReader(transformContext,new JarContentProvider());
        CollectClassHandler handler = new CollectClassHandler();
        try {
            reader.accept(handler);
        } catch (Exception e) {
            Log.e("collect library ferryman info error",e);
        }
        return handler.getInfo();
    }


}
