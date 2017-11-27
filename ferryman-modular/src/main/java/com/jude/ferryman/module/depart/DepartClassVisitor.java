package com.jude.ferryman.module.depart;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by Jude on 2017/11/20.
 */

public class DepartClassVisitor extends ClassVisitor {
    String finalName;
    boolean bingo = false;

    public DepartClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5,cv);
    }

    public String getFinalName() {
        return finalName;
    }

    public boolean isBingo() {
        return bingo;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        finalName = name;
        switch (name){
            case "com/jude/ferryman/internal/Injector":
            case "com/jude/ferryman/internal/Siphon":
            case "com/jude/ferryman/internal/RouterMap":
            case "com/jude/ferryman/Boat":
                bingo = true;
                finalName = finalName.replace("ferryman","ferryman_"+FerrymanKey.getKEY());
                break;
        }
        super.visit(version, access, finalName, signature, superName, interfaces);
    }


}
