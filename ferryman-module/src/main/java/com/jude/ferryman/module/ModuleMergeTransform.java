package com.jude.ferryman.module;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.jude.ferryman.module.collect.InfoCollector;
import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.framework.TransformContext;
import com.jude.ferryman.module.weaver.InfoMerger;

import org.gradle.api.Project;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Jude on 2017/11/20.
 */

public class ModuleMergeTransform extends Transform{
    Project project;

    public ModuleMergeTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "ferryman-module";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        TransformContext context = new TransformContext(transformInvocation);
        FerrymanInfo info = new InfoCollector(context).readInfo();
        new InfoMerger(context).mergeInfo(info);
    }
}
