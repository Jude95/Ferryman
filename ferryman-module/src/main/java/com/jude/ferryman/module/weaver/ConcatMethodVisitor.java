package com.jude.ferryman.module.weaver;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by Jude on 2017/11/21.
 */

public class ConcatMethodVisitor extends MethodVisitor {
    MethodNode methodNode;

    public ConcatMethodVisitor( MethodVisitor mv, MethodNode methodNode) {
        super(Opcodes.ASM5, mv);
        this.methodNode = methodNode;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        methodNode.instructions.accept(mv);
    }
}
