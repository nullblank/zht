// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import com.google.gson.annotations.SerializedName;

public class AntiCheatRule
{
    @SerializedName("enabled")
    private boolean enabled;
    @SerializedName("action")
    private AntiCheatAction action;
    
    public AntiCheatRule(final boolean enabled) {
        this.enabled = enabled;
        this.action = AntiCheatAction.DISCONNECT;
    }
    
    public AntiCheatRule(final boolean enabled, final AntiCheatAction action) {
        this.enabled = enabled;
        this.action = action;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(ZLdev/zomboid/AntiCheatAction;)Ljava/lang/String;, this.enabled, this.action);
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public AntiCheatAction getAction() {
        return this.action;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setAction(final AntiCheatAction action) {
        this.action = action;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AntiCheatRule)) {
            return false;
        }
        final AntiCheatRule other = (AntiCheatRule)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isEnabled() != other.isEnabled()) {
            return false;
        }
        final Object this$action = this.getAction();
        final Object other$action = other.getAction();
        if (this$action == null) {
            if (other$action == null) {
                return true;
            }
        }
        else if (this$action.equals(other$action)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AntiCheatRule;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEnabled() ? 79 : 97);
        final Object $action = this.getAction();
        result = result * 59 + (($action == null) ? 43 : $action.hashCode());
        return result;
    }
}
