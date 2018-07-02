package com.jude.ferryman.compiler.model;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;

/**
 * Created by zhuchenxi on 2017/1/18.
 */

public class ActivityInfo {
    ClassName name;
    String[] url;
    boolean noResult;
    ArrayList<FieldInfo> params = new ArrayList<>();
    ArrayList<FieldInfo> result = new ArrayList<>();
    ArrayList<InjectClassInfo> injectClassInfos = new ArrayList<>();

    public ActivityInfo(ClassName name,String[] url,boolean noResult) {
        this.name = name;
        this.url = url;
        this.noResult = noResult;
    }

    public void addParams(FieldInfo info){
        for (FieldInfo param : params) {
            if(param.getKey().equals(info.getKey())){
                return;
            }
        }
        params.add(info);
    }

    public void addResult(FieldInfo info){
        result.add(info);
    }

    public void addInjectClassInfos(InjectClassInfo injectClassInfo){
        injectClassInfos.add(injectClassInfo);
    }

    public boolean isNoResult() {
        return noResult;
    }

    public ClassName getName() {
        return name;
    }

    public String[] getUrl() {
        return url;
    }

    public ArrayList<FieldInfo> getParams() {
        return params;
    }

    public ArrayList<FieldInfo> getResult() {
        return result;
    }

    public ArrayList<InjectClassInfo> getInjectClassInfos() {
        return injectClassInfos;
    }
}
