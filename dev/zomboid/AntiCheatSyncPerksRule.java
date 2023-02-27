// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public class AntiCheatSyncPerksRule extends AntiCheatRule
{
    private int threshold;
    
    public AntiCheatSyncPerksRule(final boolean enabled) {
        super(enabled);
        this.threshold = 10;
    }
    
    public AntiCheatSyncPerksRule(final boolean enabled, final AntiCheatAction action) {
        super(enabled, action);
        this.threshold = 10;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(ZLdev/zomboid/AntiCheatAction;I)Ljava/lang/String;, this.isEnabled(), this.getAction(), this.threshold);
    }
    
    public int getThreshold() {
        return this.threshold;
    }
    
    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }
}
