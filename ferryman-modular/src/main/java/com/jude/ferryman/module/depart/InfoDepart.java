package com.jude.ferryman.module.depart;

import com.jude.ferryman.module.framework.ContextReader;
import com.jude.ferryman.module.framework.DirectoryContentProvider;
import com.jude.ferryman.module.framework.TransformContext;
import com.jude.ferryman.module.log.Log;

import org.gradle.api.Project;

/**
 * Created by Jude on 2017/11/24.
 */

public class InfoDepart {
    TransformContext transformContext;

    public InfoDepart(TransformContext transformContext) {
        this.transformContext = transformContext;
    }

    public void departInfo(Project project) {
        FerrymanKey.init(project);
        ContextReader reader = new ContextReader(transformContext,new DirectoryContentProvider());
        DepartClassHandler handler = new DepartClassHandler(transformContext);
        try {
            reader.accept(handler);
        } catch (Exception e) {
            Log.e("depart ferryman library error",e);
        }
    }


}
