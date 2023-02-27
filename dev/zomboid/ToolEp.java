// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import java.io.IOException;

public final class ToolEp
{
    public static void main(final String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Please specify either -install or -uninstall.");
            return;
        }
        final ZomboidClassPath cp = new ZomboidClassPath(".");
        final GamePatcher patcher = new GamePatcher(cp);
        final String s2 = args[0];
        switch (s2) {
            case "-install": {
                final GamePatcherCfg cfg = new GamePatcherCfg();
                for (final String s : args) {
                    if (s.equals("-client")) {
                        cfg.setClient(true);
                    }
                    else if (s.equals("-server")) {
                        cfg.setServer(true);
                    }
                }
                if (!cfg.isClient() && !cfg.isServer()) {
                    System.out.println("Please specify either -client or -server to enable all features.");
                }
                System.out.println("Installing...");
                patcher.install(cfg);
                break;
            }
            case "-uninstall": {
                System.out.println("Uninstalling...");
                patcher.uninstall();
                break;
            }
            default: {
                System.out.println(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, args[0]));
                break;
            }
        }
    }
    
    private ToolEp() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
