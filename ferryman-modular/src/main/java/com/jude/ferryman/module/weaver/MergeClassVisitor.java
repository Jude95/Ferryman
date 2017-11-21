package com.jude.ferryman.module.weaver;

import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.entry.MethodType;
import com.jude.ferryman.module.log.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
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
        Log.i("collect class "+name);
        switch (name){
            case "com/jude/ferryman/internal/Injector":
                methodType = MethodType.Injector;
                break;
            case "com/jude/ferryman/internal/Siphon":
                methodType = MethodType.Siphon;
                break;
            case "com/jude/ferryman/internal/RouterMap":
                methodType = MethodType.RouterMap;
                break;
            case "com/jude/ferryman/Boat":
                methodType = MethodType.Boat;
                for (MethodNode methodNode : info.getBoatMethods()) {
                    methodNode.accept(cv);
                }
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
                        insertMethod(methodNode,next);
                    }
                }
                break;
            case Siphon:
                if (name.equals("to")) {
                    for (MethodNode methodNode : info.getSiphonMethods()) {
                        insertMethod(methodNode,next);
                    }
                }
                break;
            case Injector:
                if (name.equals("to")) {
                    for (MethodNode methodNode : info.getInjectorMethods()) {
                        insertMethod(methodNode,next);
                    }
                }
                break;
        }
        return next;
    }

    private void insertMethod(MethodNode methodNode,MethodVisitor mv){
        AbstractInsnNode insnNode = methodNode.instructions.getFirst();
        while (insnNode!=null&&insnNode.getOpcode()!=Opcodes.RETURN){
            insnNode.accept(mv);
            insnNode = insnNode.getNext();
        }
    }
}
