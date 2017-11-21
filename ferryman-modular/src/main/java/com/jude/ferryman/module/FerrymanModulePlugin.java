package com.jude.ferryman.module;

import com.android.build.gradle.BaseExtension;
import com.google.common.io.Files;
import com.jude.ferryman.module.log.Impl.FileLoggerImpl;
import com.jude.ferryman.module.log.Log;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;

import java.io.File;
import java.io.IOException;

public class FerrymanModulePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        if (project.getPlugins().findPlugin("com.android.application") == null
                && project.getPlugins().findPlugin("com.android.library") == null) {
            throw new ProjectConfigurationException("Need android application/library plugin to be applied first", null);
        }
        try {
            initLog(project);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaseExtension baseExtension = (BaseExtension) project.getExtensions().getByName("android");
        baseExtension.registerTransform(new ModuleMergeTransform(project));
    }

    private void initLog(Project project) throws IOException {
        File logFile = new File(new File(project.getBuildDir(),"ferryman-modular"), "log");
        Files.createParentDirs(logFile);
        Log.setImpl(FileLoggerImpl.of(logFile.getAbsolutePath()));
    }

}
