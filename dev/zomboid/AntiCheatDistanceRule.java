// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public class AntiCheatDistanceRule extends AntiCheatRule
{
    private float threshold;
    
    public AntiCheatDistanceRule(final boolean enabled) {
        super(enabled);
        this.threshold = 10.0f;
    }
    
    public AntiCheatDistanceRule(final boolean enabled, final AntiCheatAction action) {
        super(enabled, action);
        this.threshold = 10.0f;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(ZLdev/zomboid/AntiCheatAction;F)Ljava/lang/String;, this.isEnabled(), this.getAction(), this.threshold);
    }
    
    public float getThreshold() {
        return this.threshold;
    }
    
    public void setThreshold(final float threshold) {
        this.threshold = threshold;
    }
}
