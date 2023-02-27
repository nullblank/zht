// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.util;

public class RateLimiter
{
    private final long delay;
    private long nextAllowedTime;
    
    public RateLimiter(final long delay) {
        this.nextAllowedTime = 0L;
        this.delay = delay;
    }
    
    public boolean check() {
        final long t = System.currentTimeMillis();
        final boolean allowed = t > this.nextAllowedTime;
        if (allowed) {
            this.nextAllowedTime = t + this.delay;
        }
        return allowed;
    }
    
    public long getDelay() {
        return this.delay;
    }
    
    public long getNextAllowedTime() {
        return this.nextAllowedTime;
    }
}
