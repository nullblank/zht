// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public class ZomboidException extends Exception
{
    public ZomboidException() {
    }
    
    public ZomboidException(final String message) {
        super(message);
    }
    
    public ZomboidException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ZomboidException(final Throwable cause) {
        super(cause);
    }
    
    public ZomboidException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
