// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import com.google.gson.annotations.SerializedName;

public class AntiCheatRateLimit
{
    @SerializedName("type")
    private String type;
    @SerializedName("delay")
    private long delay;
    
    public String getType() {
        return this.type;
    }
    
    public long getDelay() {
        return this.delay;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setDelay(final long delay) {
        this.delay = delay;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AntiCheatRateLimit)) {
            return false;
        }
        final AntiCheatRateLimit other = (AntiCheatRateLimit)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getDelay() != other.getDelay()) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null) {
            if (other$type == null) {
                return true;
            }
        }
        else if (this$type.equals(other$type)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AntiCheatRateLimit;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $delay = this.getDelay();
        result = result * 59 + (int)($delay >>> 32 ^ $delay);
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;J)Ljava/lang/String;, this.getType(), this.getDelay());
    }
}
