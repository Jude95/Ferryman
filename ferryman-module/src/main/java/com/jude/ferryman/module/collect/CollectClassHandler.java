package com.jude.ferryman.module.collect;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.framework.ClassHandler;
import com.jude.ferryman.module.framework.JarWriter;

import org.objectweb.asm.ClassReader;

import java.io.IOException;

class CollectClassHandler implements ClassHandler {

    private FerrymanInfo info;

    public FerrymanInfo getInfo() {
        return info;
    }

    @Override
    public boolean onStart(QualifiedContent content) {
        info = new FerrymanInfo();
        return true;
    }

    @Override
    public void onClassFetch(QualifiedContent content, Status status, String relativePath, byte[] bytes) throws IOException {
        if (relativePath.endsWith(".class")) {
            ClassReader cr = new ClassReader(bytes);
            CollectClassVisitor cv = new CollectClassVisitor();
            cr.accept(cv, 0);
            if (!cv.isIntercepted()){
                info.combine(cv.getInfo());
                new JarWriter(content.getFile()).write(relativePath,bytes);
            }
        }
    }

    @Override
    public void onComplete(QualifiedContent content) {
    }


}