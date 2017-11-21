package com.jude.ferryman.module.framework;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.URI;


/**
 * Created by gengwanpeng on 17/4/28.
 */
public class DirectoryContentProvider extends TargetedQualifiedContentProvider {


    public DirectoryContentProvider() {
    }

    @Override
    public void forEach(QualifiedContent content, ClassHandler processor) throws IOException {
        if (processor.onStart(content)) {
            File root = content.getFile();
            URI base = root.toURI();
            for (File f : Files.fileTreeTraverser().preOrderTraversal(root)) {
                if (f.isFile() && f.getName().endsWith(".class")) {
                    byte[] data = Files.toByteArray(f);
                    String relativePath = base.relativize(f.toURI()).toString();
                    processor.onClassFetch(content, Status.ADDED, relativePath, data);
                }
            }
        }
        processor.onComplete(content);
    }

    @Override
    public boolean accepted(QualifiedContent qualifiedContent) {
        return qualifiedContent instanceof DirectoryInput;
    }
}
