// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public class FailedFindMethodException extends ZomboidRuntimeException
{
    public FailedFindMethodException() {
    }
    
    public FailedFindMethodException(final String message) {
        super(message);
    }
    
    public FailedFindMethodException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public FailedFindMethodException(final Throwable cause) {
        super(cause);
    }
    
    public FailedFindMethodException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
