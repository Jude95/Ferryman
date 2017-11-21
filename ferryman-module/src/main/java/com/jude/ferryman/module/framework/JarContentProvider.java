package com.jude.ferryman.module.framework;

import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.google.common.io.ByteStreams;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by gengwanpeng on 17/4/28.
 */
public class JarContentProvider extends TargetedQualifiedContentProvider {

    @Override
    public void forEach(QualifiedContent content, ClassHandler processor) throws IOException {
        forActualInput((JarInput) content, processor);
    }

    private void forActualInput(JarInput jarInput, ClassHandler processor) throws IOException {
        if (processor.onStart(jarInput)) {
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(jarInput.getFile())));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                try {
                    byte[] data = ByteStreams.toByteArray(zis);
                    processor.onClassFetch(jarInput, jarInput.getStatus(), entry.getName(), data);
                } catch (EOFException e){
                    break;
                }
            }
            IOUtils.closeQuietly(zis);
        }
        processor.onComplete(jarInput);
    }

    @Override
    public boolean accepted(QualifiedContent qualifiedContent) {
        return qualifiedContent instanceof JarInput;
    }
}
