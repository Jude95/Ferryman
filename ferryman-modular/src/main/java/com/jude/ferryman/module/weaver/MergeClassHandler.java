package com.jude.ferryman.module.weaver;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.framework.ClassHandler;
import com.jude.ferryman.module.framework.DirectoryWriter;
import com.jude.ferryman.module.framework.TransformContext;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

/**
 * Created by Jude on 2017/11/20.
 */

public class MergeClassHandler implements ClassHandler {
    private final TransformContext context;

    FerrymanInfo info;
    DirectoryWriter directoryWriter;

    public MergeClassHandler(TransformContext context, FerrymanInfo info) {
        this.context = context;
        this.info = info;
        directoryWriter = new DirectoryWriter();
    }

    @Override
    public boolean onStart(QualifiedContent content) throws IOException {
        return true;
    }

    @Override
    public void onClassFetch(QualifiedContent content, Status status, String relativePath, byte[] bytes) throws IOException {
        File relativeRoot = context.getRelativeFile(content);
        if (relativePath.endsWith(".class")&&relativePath.contains("com/jude/ferryman")) {
            ClassReader cr = new ClassReader(bytes);
            ClassWriter cw = new ClassWriter(COMPUTE_MAXS|COMPUTE_FRAMES);
            MergeClassVisitor cv = new MergeClassVisitor(cw,info);
            cr.accept(cv, 0);
            directoryWriter.write(relativeRoot,relativePath,cw.toByteArray());
        }else {
            directoryWriter.write(relativeRoot,relativePath,bytes);
        }
    }

    @Override
    public void onComplete(QualifiedContent content) throws IOException {

    }
}
