package com.jude.ferryman.module.weaver;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

class DirectoryWriter {

    void write(File relativeRoot, String relativePath, byte[] bytes) throws IOException {
        if (bytes != null) {
            File target = toSystemDependentFile(relativeRoot, relativePath);
            Files.createParentDirs(target);
            Files.write(bytes, target);
        }

    }

    public static File toSystemDependentFile(File parent, String relativePath) {
        return new File(parent, relativePath.replace('/', File.separatorChar));
    }
}