package com.jude.ferryman.module.collect;

import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.android.utils.FileUtils;
import com.google.common.io.Files;
import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.framework.ClassHandler;
import com.jude.ferryman.module.framework.JarWriter;
import com.jude.ferryman.module.framework.TransformContext;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CollectClassHandler implements ClassHandler {

    private FerrymanInfo info;
    private final TransformContext context;

    public FerrymanInfo getInfo() {
        return info;
    }
    private Map<QualifiedContent, JarWriter> map = new ConcurrentHashMap<>();

    public CollectClassHandler(TransformContext context) {
        this.context = context;
        info = new FerrymanInfo();
    }

    @Override
    public boolean onStart(QualifiedContent content) throws IOException {
        if (content instanceof JarInput) {
            JarInput jarInput = (JarInput) content;
            File targetFile = context.getRelativeFile(content);
            switch (jarInput.getStatus()) {
                case REMOVED:
                    FileUtils.deleteIfExists(targetFile);
                    return false;
                case CHANGED:
                    FileUtils.deleteIfExists(targetFile);
                default:
                    Files.createParentDirs(targetFile);
                    map.put(content, new JarWriter(targetFile));
            }
        }
        return true;
    }

    @Override
    public void onClassFetch(QualifiedContent content, Status status, String relativePath, byte[] bytes) throws IOException {
        if (relativePath.endsWith(".class")) {
            ClassReader cr = new ClassReader(bytes);
            CollectClassVisitor cv = new CollectClassVisitor();
            cr.accept(cv, 0);
            if (!cv.isIntercepted()){
                JarWriter jarWriter =  map.get(content);
                jarWriter.write(relativePath,bytes);
            }else {
                info.combine(cv.getInfo());
            }
        }else {
            JarWriter jarWriter =  map.get(content);
            jarWriter.write(relativePath,bytes);
        }
    }

    @Override
    public void onComplete(QualifiedContent content) throws IOException {
        if (content instanceof JarInput && ((JarInput) content).getStatus() != Status.REMOVED) {
            map.get(content).close();
        }
    }


}