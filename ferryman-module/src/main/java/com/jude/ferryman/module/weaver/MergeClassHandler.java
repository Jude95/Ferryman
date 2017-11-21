package com.jude.ferryman.module.weaver;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.framework.ClassHandler;
import com.jude.ferryman.module.framework.TransformContext;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jude on 2017/11/20.
 */

public class MergeClassHandler implements ClassHandler {
    private final TransformContext context;

    FerrymanInfo info;

    public MergeClassHandler(TransformContext context, FerrymanInfo info) {
        this.context = context;
        this.info = info;
    }

    @Override
    public boolean onStart(QualifiedContent content) throws IOException {
        return false;
    }

    @Override
    public void onClassFetch(QualifiedContent content, Status status, String relativePath, byte[] bytes) throws IOException {
        if (relativePath.endsWith(".class")) {
            ClassReader cr = new ClassReader(bytes);
            ClassWriter cw = new ClassWriter(0);
            MergeClassVisitor cv = new MergeClassVisitor(cw,info);
            cr.accept(cv, 0);
            File relativeRoot = context.getRelativeFile(content);
            new DirectoryWriter().write(relativeRoot,relativePath,cw.toByteArray());
        }
    }

    @Override
    public void onComplete(QualifiedContent content) throws IOException {

    }
}
