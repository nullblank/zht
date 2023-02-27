// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public enum AntiCheatAction
{
    ALERT, 
    DISCONNECT, 
    BAN_STEAM_ID, 
    BAN_IP, 
    BAN_ALL;
    
    private static /* synthetic */ AntiCheatAction[] $values() {
        return new AntiCheatAction[] { AntiCheatAction.ALERT, AntiCheatAction.DISCONNECT, AntiCheatAction.BAN_STEAM_ID, AntiCheatAction.BAN_IP, AntiCheatAction.BAN_ALL };
    }
    
    static {
        $VALUES = $values();
    }
}
