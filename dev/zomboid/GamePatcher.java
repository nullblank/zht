// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import java.nio.file.CopyOption;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Paths;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import java.io.IOException;
import org.objectweb.asm.tree.MethodNode;
import dev.zomboid.util.Inject;
import org.objectweb.asm.tree.AnnotationNode;
import java.util.LinkedList;
import org.objectweb.asm.tree.ClassNode;

public class GamePatcher
{
    private final ZomboidClassPath cp;
    private static final String INJECTED_ANNOTATION = "Ldev/zomboid/Injected;";
    private static final String[] RESTORATION_FILES;
    
    private void markInjected(final ClassNode cl) {
        if (cl.visibleAnnotations == null) {
            cl.visibleAnnotations = new LinkedList<AnnotationNode>();
        }
        cl.visibleAnnotations.add(new AnnotationNode("Ldev/zomboid/Injected;"));
    }
    
    private boolean injectCore() throws IOException {
        final String name = "zombie/core/Core";
        final ClassNode cl = this.cp.readClass(name);
        this.markInjected(cl);
        final MethodNode mt = Inject.findMethod(cl, "EndFrameUI", "()V");
        Inject.injectVirtualCallsBegin(mt, "dev/zomboid/interp/RenderingStub", "endFrameUi", "(Lzombie/core/Core;)V");
        this.cp.replaceClass(name, cl);
        return true;
    }
    
    private boolean injectGameClient() throws IOException {
        final String name = "zombie/network/GameClient";
        final ClassNode cl = this.cp.readClass(name);
        this.markInjected(cl);
        final MethodNode addIncoming = Inject.findMethod(cl, "addIncoming", "(SLjava/nio/ByteBuffer;)V");
        addIncoming.instructions.insert(new MethodInsnNode(184, "dev/zomboid/interp/NetworkingStub", "addIncomingClient", "(SLjava/nio/ByteBuffer;)V"));
        addIncoming.instructions.insert(new VarInsnNode(25, 2));
        addIncoming.instructions.insert(new VarInsnNode(21, 1));
        this.cp.replaceClass(name, cl);
        return true;
    }
    
    private boolean injectUdpEngine() throws IOException {
        final String name = "zombie/core/raknet/UdpEngine";
        final ClassNode cl = this.cp.readClass(name);
        this.markInjected(cl);
        final MethodNode decode = Inject.findMethod(cl, "decode", "(Ljava/nio/ByteBuffer;)V");
        decode.instructions.insert(new MethodInsnNode(184, "dev/zomboid/interp/NetworkingStub", "decode", "(Lzombie/core/raknet/UdpEngine;Ljava/nio/ByteBuffer;)V"));
        decode.instructions.insert(new VarInsnNode(25, 1));
        decode.instructions.insert(new VarInsnNode(25, 0));
        this.cp.replaceClass(name, cl);
        return true;
    }
    
    private boolean injectGameServer() throws IOException {
        final String name = "zombie/network/GameServer";
        final ClassNode cl = this.cp.readClass(name);
        this.markInjected(cl);
        final MethodNode addIncoming = Inject.findMethod(cl, "addIncoming", "(SLjava/nio/ByteBuffer;Lzombie/core/raknet/UdpConnection;)V");
        final MethodNode main = Inject.findMethod(cl, "main", "([Ljava/lang/String;)V");
        addIncoming.instructions.insert(new MethodInsnNode(184, "dev/zomboid/interp/NetworkingStub", "addIncomingServer", "(SLjava/nio/ByteBuffer;Lzombie/core/raknet/UdpConnection;)V"));
        addIncoming.instructions.insert(new VarInsnNode(25, 2));
        addIncoming.instructions.insert(new VarInsnNode(25, 1));
        addIncoming.instructions.insert(new VarInsnNode(21, 0));
        Inject.injectStaticCallsBegin(main, "dev/zomboid/interp/CoreStub", "serverMain", "()V");
        this.cp.replaceClass(name, cl);
        return true;
    }
    
    private void extractSelf(final String path) throws IOException {
        final ZipInputStream zis = new ZipInputStream(new FileInputStream(path));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final byte[] tmp = new byte[4096];
            int read;
            while ((read = zis.read(tmp)) != -1) {
                bos.write(tmp, 0, read);
            }
            final Path p = Paths.get(entry.getName(), new String[0]);
            if (entry.isDirectory()) {
                Files.createDirectories(p, (FileAttribute<?>[])new FileAttribute[0]);
            }
            else {
                if (p.getParent() != null) {
                    Files.createDirectories(p.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
                }
                Files.write(Paths.get(entry.getName(), new String[0]), bos.toByteArray(), new OpenOption[0]);
            }
        }
    }
    
    public void install(final GamePatcherCfg cfg) throws IOException {
        System.out.println("Removing old installation");
        this.uninstall();
        System.out.println("Injecting into Core");
        this.injectCore();
        System.out.println("Injecting into UdpEngine");
        this.injectUdpEngine();
        if (cfg.isClient()) {
            System.out.println("Injecting into GameClient");
            this.injectGameClient();
        }
        if (cfg.isServer()) {
            System.out.println("Injecting into GameServer");
            this.injectGameServer();
        }
        System.out.println("Extracting dependencies from self");
        this.extractSelf(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.println("Done!");
    }
    
    private void removeBackup(final String name) throws IOException {
        final Path modified = Paths.get(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, name), new String[0]);
        final Path backup = Paths.get(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, name), new String[0]);
        if (Files.exists(modified, new LinkOption[0]) && Files.exists(backup, new LinkOption[0])) {
            Files.delete(modified);
            Files.move(backup, modified, new CopyOption[0]);
        }
    }
    
    public void uninstall() throws IOException {
        for (final String s : GamePatcher.RESTORATION_FILES) {
            System.out.println(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, s));
            this.removeBackup(s);
        }
    }
    
    public GamePatcher(final ZomboidClassPath cp) {
        this.cp = cp;
    }
    
    static {
        RESTORATION_FILES = new String[] { "zombie/core/Core", "zombie/network/GameClient", "zombie/network/GameServer", "zombie/core/raknet/UdpEngine" };
    }
}
