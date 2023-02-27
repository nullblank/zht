// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public final class ZomboidApi
{
    public static final String VERSION = "1.3.3.7";
    public static final String DISPLAY_NAME = ".";
    public static final ZomboidApiCore core;
    
    private ZomboidApi() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        core = new ZomboidApiCore();
    }
}
