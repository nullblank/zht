// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.util;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import dev.zomboid.FailedFindMethodException;
import java.util.Iterator;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;

public final class Inject
{
    public static MethodNode findMethodSafe(final ClassNode node, final String name, final String desc) {
        for (final MethodNode method : node.methods) {
            if (method.name.equals(name) && method.desc.equals(desc)) {
                return method;
            }
        }
        return null;
    }
    
    public static MethodNode findMethod(final ClassNode node, final String name, final String desc) {
        for (final MethodNode method : node.methods) {
            if (method.name.equals(name) && method.desc.equals(desc)) {
                return method;
            }
        }
        throw new FailedFindMethodException(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, name, desc, node.name));
    }
    
    public static void injectVirtualCallsBeforeReturns(final MethodNode node, final String owner, final String name, final String desc) {
        for (final AbstractInsnNode ain : node.instructions.toArray()) {
            if (ain.getOpcode() >= 172 && ain.getOpcode() <= 177) {
                node.instructions.insertBefore(ain, new VarInsnNode(25, 0));
                node.instructions.insertBefore(ain, new MethodInsnNode(184, owner, name, desc));
            }
        }
    }
    
    public static void injectStaticCallsBeforeReturns(final MethodNode node, final String owner, final String name, final String desc) {
        for (final AbstractInsnNode ain : node.instructions.toArray()) {
            if (ain.getOpcode() >= 172 && ain.getOpcode() <= 177) {
                node.instructions.insertBefore(ain, new MethodInsnNode(184, owner, name, desc));
            }
        }
    }
    
    public static void injectVirtualCallsBegin(final MethodNode node, final String owner, final String name, final String desc) {
        node.instructions.insert(new MethodInsnNode(184, owner, name, desc));
        node.instructions.insert(new VarInsnNode(25, 0));
    }
    
    public static void injectStaticCallsBegin(final MethodNode node, final String owner, final String name, final String desc) {
        node.instructions.insert(new MethodInsnNode(184, owner, name, desc));
    }
    
    private Inject() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
