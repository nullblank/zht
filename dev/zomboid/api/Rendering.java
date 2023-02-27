// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.api;

import zombie.ui.TextManager;

public final class Rendering
{
    public static void text(final String text, final float x, final float y, final float r, final float g, final float b, final float a) {
        TextManager.instance.DrawString((double)x, (double)y, text, (double)r, (double)g, (double)b, (double)a);
    }
    
    private Rendering() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
