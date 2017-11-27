package com.jude.ferryman.module;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.collect.Sets;
import com.jude.ferryman.module.depart.InfoDepart;
import com.jude.ferryman.module.framework.TransformContext;
import com.jude.ferryman.module.log.Log;

import org.gradle.api.Project;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Jude on 2017/11/24.
 */

public class ModuleDepartTransform extends Transform{
    Project project;

    public ModuleDepartTransform(Project project) {
        this.project = project;
    }


    @Override
    public String getName() {
        return "ferryman-modular-library";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT);
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Log.i("start transform "+(transformInvocation.isIncremental()?"Incremental":"No Incremental"));
        TransformContext context = new TransformContext(transformInvocation);
        InfoDepart depart = new InfoDepart(context);
        depart.departInfo(project);
    }
}
