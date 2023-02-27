// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.interp;

import dev.zomboid.ZomboidApi;
import dev.zomboid.api.Rendering;
import zombie.core.Core;
import zombie.ui.UIManager;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RenderingStub
{
    private static final AtomicBoolean initialized;
    
    private static boolean readyToInit() {
        return !UIManager.UI.isEmpty();
    }
    
    public static void endFrameUi(final Core self) {
        Rendering.text(".", 15.0f, 15.0f, 1.0f, 0.0f, 0.0f, 1.0f);
        if (readyToInit()) {
            if (RenderingStub.initialized.compareAndSet(false, true)) {
                ZomboidApi.core.initClient();
            }
            ZomboidApi.core.update();
        }
    }
    
    private RenderingStub() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        initialized = new AtomicBoolean(false);
    }
}
