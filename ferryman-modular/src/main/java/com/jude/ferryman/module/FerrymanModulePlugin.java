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
//        try {
//            initLog(project);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        BaseExtension baseExtension = (BaseExtension) project.getExtensions().getByName("android");
        if (project.getPlugins().findPlugin("com.android.application") != null){
            Log.i("apply application with ModuleMergeTransform");
            baseExtension.registerTransform(new ModuleMergeTransform(project));
        }else if (project.getPlugins().findPlugin("com.android.library") != null){
            Log.i("apply library with ModuleDepartTransform");
            baseExtension.registerTransform(new ModuleDepartTransform(project));
        }else {
            throw new ProjectConfigurationException("Need android application/library plugin to be applied first", null);
        }
    }

    private void initLog(Project project) throws IOException {
        File logFile = new File(new File(project.getBuildDir(),"ferryman-modular"), "log");
        Files.createParentDirs(logFile);
        Log.setImpl(FileLoggerImpl.of(logFile.getAbsolutePath()));
    }

}
