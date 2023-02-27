// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

public class GamePatcherCfg
{
    private boolean client;
    private boolean server;
    
    public boolean isClient() {
        return this.client;
    }
    
    public boolean isServer() {
        return this.server;
    }
    
    public void setClient(final boolean client) {
        this.client = client;
    }
    
    public void setServer(final boolean server) {
        this.server = server;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GamePatcherCfg)) {
            return false;
        }
        final GamePatcherCfg other = (GamePatcherCfg)o;
        return other.canEqual(this) && this.isClient() == other.isClient() && this.isServer() == other.isServer();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GamePatcherCfg;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isClient() ? 79 : 97);
        result = result * 59 + (this.isServer() ? 79 : 97);
        return result;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(ZZ)Ljava/lang/String;, this.isClient(), this.isServer());
    }
}
