package com.jude.ferryman.module.entry;

import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jude on 2017/11/20.
 */

public class FerrymanInfo {
    public List<MethodNode> mBoatMethods = Collections.synchronizedList(new ArrayList<>());
    public List<MethodNode> mInjectorMethods = Collections.synchronizedList(new ArrayList<>());
    public List<MethodNode> mSiphonMethods = Collections.synchronizedList(new ArrayList<>());
    public List<MethodNode> mMapMethods = Collections.synchronizedList(new ArrayList<>());

    public List<MethodNode> getBoatMethods() {
        return mBoatMethods;
    }

    public void addBoatMethods(MethodNode mWardenMethod) {
        this.mBoatMethods.add(mWardenMethod);
    }

    public List<MethodNode> getInjectorMethods() {
        return mInjectorMethods;
    }

    public void addInjectorMethods(MethodNode mInjectorMethod) {
        this.mInjectorMethods.add(mInjectorMethod);
    }

    public List<MethodNode> getSiphonMethods() {
        return mSiphonMethods;
    }

    public void addSiphonMethods(MethodNode mSiphonMethod) {
        this.mSiphonMethods.add(mSiphonMethod);
    }

    public List<MethodNode> getMapMethods() {
        return mMapMethods;
    }

    public void addMapMethods(MethodNode mMapMethod) {
        this.mMapMethods.add(mMapMethod);
    }

    public void combine(FerrymanInfo info) {
        mBoatMethods.addAll(info.mBoatMethods);
        mInjectorMethods.addAll(info.mInjectorMethods);
        mSiphonMethods.addAll(info.mSiphonMethods);
        mMapMethods.addAll(info.mMapMethods);
    }

    @Override
    public String toString() {
        return "mBoatMethods" + mBoatMethods.size() + "\n"
                + "mInjectorMethods" + mInjectorMethods.size() + "\n"
                + "mSiphonMethods" + mSiphonMethods.size() + "\n"
                + "mMapMethods" + mMapMethods.size() + "\n";
    }
}
