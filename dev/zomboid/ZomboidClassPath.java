// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import java.nio.file.OpenOption;
import org.objectweb.asm.ClassWriter;
import java.nio.file.CopyOption;
import java.nio.file.LinkOption;
import java.io.IOException;
import java.util.Iterator;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public final class ZomboidClassPath
{
    private final Path root;
    
    public ZomboidClassPath(final String root) {
        this.root = Paths.get(root, new String[0]);
    }
    
    private String normalize(String dir) {
        dir = dir.replace('/', '\\');
        dir = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, dir);
        return dir;
    }
    
    private boolean isInjected(final String dir) throws IOException {
        final ClassReader cr = new ClassReader(Files.readAllBytes(this.root.resolve(dir)));
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        for (final AnnotationNode node : cn.visibleAnnotations) {
            if (node.desc.equals("Ldev/zomboid/Injected;")) {
                return true;
            }
        }
        return false;
    }
    
    public ClassNode readClass(String dir) throws IOException {
        dir = this.normalize(dir);
        final Path bkup = this.root.resolve(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, dir));
        if (Files.exists(bkup, new LinkOption[0]) && this.isInjected(dir)) {
            dir = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, dir);
        }
        final ClassReader cr = new ClassReader(Files.readAllBytes(this.root.resolve(dir)));
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        return cn;
    }
    
    public void replaceClass(String dir, final ClassNode node) throws IOException {
        dir = this.normalize(dir);
        final Path orig = this.root.resolve(dir);
        final Path bkup = this.root.resolve(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, dir));
        if (!Files.exists(bkup, new LinkOption[0]) || !this.isInjected(dir)) {
            Files.copy(orig, bkup, new CopyOption[0]);
        }
        final ClassWriter cw = new ClassWriter(0);
        node.accept(cw);
        Files.write(orig, cw.toByteArray(), new OpenOption[0]);
    }
}
