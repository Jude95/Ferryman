package com.jude.ferryman.compiler.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

/**
 * Created by zhuchenxi on 2017/1/19.
 */

public class FieldInfo {
    private String name;
    private String key;
    private TypeName clazz;
    private List<AnnotationSpec> annotations;

    public FieldInfo(String name,String key, TypeName clazz, List<AnnotationSpec> annotations) {
        this.name = name;
        this.clazz = clazz;
        this.key = key;
        this.annotations = annotations;
        if (key == null || key.length()==0){
            this.key = name;
        }
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeName getClazz() {
        return clazz;
    }


    public void setClazz(TypeName clazz) {
        this.clazz = clazz;
    }


    public List<AnnotationSpec> getAnnotations() {
        return annotations;
    }
}
