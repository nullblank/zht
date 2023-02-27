// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.admin;

public enum KillType
{
    LOCAL_DEATH, 
    NORMAL_HIT;
    
    private static /* synthetic */ KillType[] $values() {
        return new KillType[] { KillType.LOCAL_DEATH, KillType.NORMAL_HIT };
    }
    
    static {
        $VALUES = $values();
    }
}
