package com.jude.ferryman.module.depart;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.jude.ferryman.module.framework.ClassHandler;
import com.jude.ferryman.module.framework.DirectoryWriter;
import com.jude.ferryman.module.framework.TransformContext;
import com.jude.ferryman.module.log.Log;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jude on 2017/11/20.
 */

public class DepartClassHandler implements ClassHandler {
    private final TransformContext context;

    DirectoryWriter directoryWriter;

    public DepartClassHandler(TransformContext context) {
        this.context = context;
        directoryWriter = new DirectoryWriter();
    }

    @Override
    public boolean onStart(QualifiedContent content) throws IOException {
        return true;
    }

    @Override
    public void onClassFetch(QualifiedContent content, Status status, String relativePath, byte[] bytes) throws IOException {
        Log.i("relativePath: " + relativePath);
        File relativeRoot = context.getRelativeFile(content);
        if (relativePath.endsWith(".class")&&relativePath.contains("com/jude/ferryman")){
            ClassReader cr = new ClassReader(bytes);
            ClassWriter cw = new ClassWriter(0);
            DepartClassVisitor cv = new DepartClassVisitor(cw);
            cr.accept(cv, 0);
            if (cv.isBingo()){
                relativePath = cv.getFinalName()+".class";
            }
            directoryWriter.write(relativeRoot, relativePath, cw.toByteArray());
        }else {
            directoryWriter.write(relativeRoot, relativePath, bytes);
        }
    }

    @Override
    public void onComplete(QualifiedContent content) throws IOException {

    }
}
