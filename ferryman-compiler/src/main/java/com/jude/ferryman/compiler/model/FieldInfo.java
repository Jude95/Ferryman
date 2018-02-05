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
    private boolean wrapper;
    private String[] group;
    private boolean ignore;
    private TypeName clazz;
    private List<AnnotationSpec> annotations;

    public FieldInfo(String name,String key, TypeName clazz, boolean wrapper, List<AnnotationSpec> annotations) {
        this.name = name;
        this.clazz = clazz;
        this.key = key;
        this.annotations = annotations;
        this.wrapper = wrapper;
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

    public Boolean hasWrapper(){
        return wrapper;
    }

    public void setClazz(TypeName clazz) {
        this.clazz = clazz;
    }

    public String[] getGroup() {
        return group;
    }

    public void setGroup(String[] group) {
        this.group = group;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public List<AnnotationSpec> getAnnotations() {
        return annotations;
    }
}
