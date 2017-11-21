package com.jude.ferryman.module.weaver;

import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.entry.MethodType;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by Jude on 2017/11/20.
 */

public class MergeClassVisitor extends ClassVisitor {
    FerrymanInfo info;
    private MethodType methodType;

    public MergeClassVisitor(ClassVisitor cv,FerrymanInfo info) {
        super(Opcodes.ASM5,cv);
        this.info = info;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        switch (name){
            case "com.jude.ferryman.Injector":
                methodType = MethodType.Injector;
                break;
            case "com.jude.ferryman.Siphon":
                methodType = MethodType.Siphon;
                break;
            case "com.jude.ferryman.RouterMap":
                methodType = MethodType.RouterMap;
                break;
            case "com.jude.ferryman.Boat":
                methodType = MethodType.Boat;
                break;
            default:
                methodType = MethodType.Other;
                break;
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor next = super.visitMethod(access, name, desc, signature, exceptions);
        switch (methodType){
            case RouterMap:
                if (name.equals("initTable")){
                    for (MethodNode methodNode : info.getMapMethods()) {
                        next =  new ConcatMethodVisitor(next,methodNode);
                    }
                }
                break;
            case Siphon:
                if (name.equals("to")) {
                    for (MethodNode methodNode : info.getSiphonMethods()) {
                        next =  new ConcatMethodVisitor(next,methodNode);
                    }
                }
                break;
            case Injector:
                if (name.equals("to")) {
                    for (MethodNode methodNode : info.getInjectorMethods()) {
                        next =  new ConcatMethodVisitor(next,methodNode);
                    }
                }
                break;
            case Boat:
                for (MethodNode methodNode : info.getBoatMethods()) {
                    methodNode.accept(cv);
                }
                break;
        }
        return next;
    }
}
