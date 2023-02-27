// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public class ZomboidRuntimeException extends RuntimeException
{
    public ZomboidRuntimeException() {
    }
    
    public ZomboidRuntimeException(final String message) {
        super(message);
    }
    
    public ZomboidRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ZomboidRuntimeException(final Throwable cause) {
        super(cause);
    }
    
    public ZomboidRuntimeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
