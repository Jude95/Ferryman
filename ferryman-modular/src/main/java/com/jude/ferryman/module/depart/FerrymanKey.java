package com.jude.ferryman.module.depart;

import org.gradle.api.Project;

/**
 * Created by Jude on 2017/11/24.
 */

public class FerrymanKey {
    public static String KEY;

    public static void init(Project project){
        KEY = project.getGroup()+"_"+project.getName();
    }

    public static String getKEY(){
        return KEY;
    }

}
