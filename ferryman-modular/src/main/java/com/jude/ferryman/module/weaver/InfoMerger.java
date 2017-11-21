package com.jude.ferryman.module.weaver;

import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.framework.ContextReader;
import com.jude.ferryman.module.framework.DirectoryContentProvider;
import com.jude.ferryman.module.framework.TransformContext;
import com.jude.ferryman.module.log.Log;

/**
 * Created by Jude on 2017/11/20.
 */

public class InfoMerger {
    TransformContext transformContext;

    public InfoMerger(TransformContext transformContext) {
        this.transformContext = transformContext;
    }

    public void mergeInfo(FerrymanInfo info) {
        ContextReader reader = new ContextReader(transformContext,new DirectoryContentProvider());
        MergeClassHandler handler = new MergeClassHandler(transformContext, info);
        try {
            reader.accept(handler);
        } catch (Exception e) {
            Log.e("collect library ferryman info error",e);
        }
    }


}
