package com.jude.ferryman.compiler.model;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;

/**
 * Created by zhuchenxi on 2017/1/19.
 */

public class InjectClassInfo {
    private ClassName name;
    private ArrayList<FieldInfo> params = new ArrayList<>();
    private ArrayList<FieldInfo> result = new ArrayList<>();

    public InjectClassInfo(ClassName name) {
        this.name = name;
    }

    public ClassName getName() {
        return name;
    }

    public void setName(ClassName name) {
        this.name = name;
    }

    public void addParams(FieldInfo info){
        params.add(info);
    }

    public void addResult(FieldInfo info){
        result.add(info);
    }

    public ArrayList<FieldInfo> getParams() {
        return params;
    }

    public ArrayList<FieldInfo> getResult() {
        return result;
    }
}
