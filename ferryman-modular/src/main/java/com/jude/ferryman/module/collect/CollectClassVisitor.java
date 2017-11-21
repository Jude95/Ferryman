package com.jude.ferryman.module.collect;

import com.jude.ferryman.module.entry.FerrymanInfo;
import com.jude.ferryman.module.entry.MethodType;
import com.jude.ferryman.module.log.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by gengwanpeng on 17/4/27.
 */
public class CollectClassVisitor extends ClassVisitor {

    FerrymanInfo info;
    private boolean intercepted;

    private MethodType methodType;

    CollectClassVisitor() {
        super(Opcodes.ASM5);
        this.info = new FerrymanInfo();
    }

    public boolean isIntercepted() {
        return intercepted;
    }

    public FerrymanInfo getInfo() {
        return info;
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Log.i("collect class "+name);
        switch (name){
            case "com/jude/ferryman/internal/Injector":
                methodType = MethodType.Injector;
                intercepted = true;
                break;
            case "com/jude/ferryman/internal/Siphon":
                methodType = MethodType.Siphon;
                intercepted = true;
                break;
            case "com/jude/ferryman/internal/RouterMap":
                methodType = MethodType.RouterMap;
                intercepted = true;
                break;
            case "com/jude/ferryman/Boat":
                methodType = MethodType.Boat;
                intercepted = true;
                break;
            default:
                methodType = MethodType.Other;
                break;
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodNode methodNode = new MethodNode(access, name, desc, signature, exceptions);
        if (isIntercepted()){
            Log.i("collect method "+name);
        }
        switch (methodType){
            case RouterMap:
                if (name.equals("initTable")){
                    info.addMapMethods(methodNode);
                    return methodNode;
                }
            case Siphon:
                if (name.equals("to")) {
                    info.addSiphonMethods(methodNode);
                    return methodNode;
                }
            case Injector:
                if (name.equals("to")) {
                    info.addInjectorMethods(methodNode);
                    return methodNode;
                }
            case Boat:
                if (name.startsWith("goto")) {
                    info.addBoatMethods(methodNode);
                    return methodNode;
                }
            default:
                return null;
        }
    }

}
