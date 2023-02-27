// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import java.util.LinkedList;
import java.util.Iterator;
import zombie.network.PacketTypes;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AntiCheatCfg
{
    @SerializedName("discordApi")
    public String discordApi;
    @SerializedName("syncPerksRule")
    private AntiCheatSyncPerksRule syncPerksRule;
    @SerializedName("teleportRule")
    private AntiCheatRule teleportRule;
    @SerializedName("extraInfoRule")
    private AntiCheatRule extraInfoRule;
    @SerializedName("playerDeathsRule")
    private AntiCheatRule playerDeathsRule;
    @SerializedName("additionalPainRule")
    private AntiCheatRule additionalPainRule;
    @SerializedName("syncClothingRule")
    private AntiCheatRule syncClothingRule;
    @SerializedName("distanceRule")
    private AntiCheatDistanceRule distanceRule;
    @SerializedName("chatRule")
    private AntiCheatRule chatRule;
    @SerializedName("rateLimits")
    private List<AntiCheatRateLimit> rateLimits;
    
    public boolean isRateLimited(final short type) {
        for (final AntiCheatRateLimit rl : this.rateLimits) {
            final PacketTypes.PacketType t = PacketTypes.PacketType.valueOf(rl.getType());
            if (t.getId() == type) {
                return true;
            }
        }
        return false;
    }
    
    public long getRateLimit(final short type) {
        for (final AntiCheatRateLimit rl : this.rateLimits) {
            final PacketTypes.PacketType t = PacketTypes.PacketType.valueOf(rl.getType());
            if (t.getId() == type) {
                return rl.getDelay();
            }
        }
        return 0L;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ldev/zomboid/AntiCheatSyncPerksRule;Ldev/zomboid/AntiCheatRule;Ldev/zomboid/AntiCheatRule;Ldev/zomboid/AntiCheatRule;Ldev/zomboid/AntiCheatRule;Ldev/zomboid/AntiCheatRule;Ldev/zomboid/AntiCheatDistanceRule;Ldev/zomboid/AntiCheatRule;Ljava/util/List;)Ljava/lang/String;, this.syncPerksRule, this.teleportRule, this.extraInfoRule, this.playerDeathsRule, this.additionalPainRule, this.syncClothingRule, this.distanceRule, this.chatRule, this.rateLimits);
    }
    
    public AntiCheatCfg() {
        this.discordApi = "";
        this.syncPerksRule = new AntiCheatSyncPerksRule(false);
        this.teleportRule = new AntiCheatRule(false);
        this.extraInfoRule = new AntiCheatRule(true);
        this.playerDeathsRule = new AntiCheatRule(false);
        this.additionalPainRule = new AntiCheatRule(false);
        this.syncClothingRule = new AntiCheatRule(false);
        this.distanceRule = new AntiCheatDistanceRule(false);
        this.chatRule = new AntiCheatRule(false);
        this.rateLimits = new LinkedList<AntiCheatRateLimit>();
    }
    
    public String getDiscordApi() {
        return this.discordApi;
    }
    
    public AntiCheatSyncPerksRule getSyncPerksRule() {
        return this.syncPerksRule;
    }
    
    public AntiCheatRule getTeleportRule() {
        return this.teleportRule;
    }
    
    public AntiCheatRule getExtraInfoRule() {
        return this.extraInfoRule;
    }
    
    public AntiCheatRule getPlayerDeathsRule() {
        return this.playerDeathsRule;
    }
    
    public AntiCheatRule getAdditionalPainRule() {
        return this.additionalPainRule;
    }
    
    public AntiCheatRule getSyncClothingRule() {
        return this.syncClothingRule;
    }
    
    public AntiCheatDistanceRule getDistanceRule() {
        return this.distanceRule;
    }
    
    public AntiCheatRule getChatRule() {
        return this.chatRule;
    }
    
    public List<AntiCheatRateLimit> getRateLimits() {
        return this.rateLimits;
    }
    
    public void setDiscordApi(final String discordApi) {
        this.discordApi = discordApi;
    }
    
    public void setSyncPerksRule(final AntiCheatSyncPerksRule syncPerksRule) {
        this.syncPerksRule = syncPerksRule;
    }
    
    public void setTeleportRule(final AntiCheatRule teleportRule) {
        this.teleportRule = teleportRule;
    }
    
    public void setExtraInfoRule(final AntiCheatRule extraInfoRule) {
        this.extraInfoRule = extraInfoRule;
    }
    
    public void setPlayerDeathsRule(final AntiCheatRule playerDeathsRule) {
        this.playerDeathsRule = playerDeathsRule;
    }
    
    public void setAdditionalPainRule(final AntiCheatRule additionalPainRule) {
        this.additionalPainRule = additionalPainRule;
    }
    
    public void setSyncClothingRule(final AntiCheatRule syncClothingRule) {
        this.syncClothingRule = syncClothingRule;
    }
    
    public void setDistanceRule(final AntiCheatDistanceRule distanceRule) {
        this.distanceRule = distanceRule;
    }
    
    public void setChatRule(final AntiCheatRule chatRule) {
        this.chatRule = chatRule;
    }
    
    public void setRateLimits(final List<AntiCheatRateLimit> rateLimits) {
        this.rateLimits = rateLimits;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AntiCheatCfg)) {
            return false;
        }
        final AntiCheatCfg other = (AntiCheatCfg)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$discordApi = this.getDiscordApi();
        final Object other$discordApi = other.getDiscordApi();
        Label_0065: {
            if (this$discordApi == null) {
                if (other$discordApi == null) {
                    break Label_0065;
                }
            }
            else if (this$discordApi.equals(other$discordApi)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$syncPerksRule = this.getSyncPerksRule();
        final Object other$syncPerksRule = other.getSyncPerksRule();
        Label_0102: {
            if (this$syncPerksRule == null) {
                if (other$syncPerksRule == null) {
                    break Label_0102;
                }
            }
            else if (this$syncPerksRule.equals(other$syncPerksRule)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$teleportRule = this.getTeleportRule();
        final Object other$teleportRule = other.getTeleportRule();
        Label_0139: {
            if (this$teleportRule == null) {
                if (other$teleportRule == null) {
                    break Label_0139;
                }
            }
            else if (this$teleportRule.equals(other$teleportRule)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$extraInfoRule = this.getExtraInfoRule();
        final Object other$extraInfoRule = other.getExtraInfoRule();
        Label_0176: {
            if (this$extraInfoRule == null) {
                if (other$extraInfoRule == null) {
                    break Label_0176;
                }
            }
            else if (this$extraInfoRule.equals(other$extraInfoRule)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$playerDeathsRule = this.getPlayerDeathsRule();
        final Object other$playerDeathsRule = other.getPlayerDeathsRule();
        Label_0213: {
            if (this$playerDeathsRule == null) {
                if (other$playerDeathsRule == null) {
                    break Label_0213;
                }
            }
            else if (this$playerDeathsRule.equals(other$playerDeathsRule)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$additionalPainRule = this.getAdditionalPainRule();
        final Object other$additionalPainRule = other.getAdditionalPainRule();
        Label_0250: {
            if (this$additionalPainRule == null) {
                if (other$additionalPainRule == null) {
                    break Label_0250;
                }
            }
            else if (this$additionalPainRule.equals(other$additionalPainRule)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$syncClothingRule = this.getSyncClothingRule();
        final Object other$syncClothingRule = other.getSyncClothingRule();
        Label_0287: {
            if (this$syncClothingRule == null) {
                if (other$syncClothingRule == null) {
                    break Label_0287;
                }
            }
            else if (this$syncClothingRule.equals(other$syncClothingRule)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$distanceRule = this.getDistanceRule();
        final Object other$distanceRule = other.getDistanceRule();
        Label_0324: {
            if (this$distanceRule == null) {
                if (other$distanceRule == null) {
                    break Label_0324;
                }
            }
            else if (this$distanceRule.equals(other$distanceRule)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$chatRule = this.getChatRule();
        final Object other$chatRule = other.getChatRule();
        Label_0361: {
            if (this$chatRule == null) {
                if (other$chatRule == null) {
                    break Label_0361;
                }
            }
            else if (this$chatRule.equals(other$chatRule)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$rateLimits = this.getRateLimits();
        final Object other$rateLimits = other.getRateLimits();
        if (this$rateLimits == null) {
            if (other$rateLimits == null) {
                return true;
            }
        }
        else if (this$rateLimits.equals(other$rateLimits)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AntiCheatCfg;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $discordApi = this.getDiscordApi();
        result = result * 59 + (($discordApi == null) ? 43 : $discordApi.hashCode());
        final Object $syncPerksRule = this.getSyncPerksRule();
        result = result * 59 + (($syncPerksRule == null) ? 43 : $syncPerksRule.hashCode());
        final Object $teleportRule = this.getTeleportRule();
        result = result * 59 + (($teleportRule == null) ? 43 : $teleportRule.hashCode());
        final Object $extraInfoRule = this.getExtraInfoRule();
        result = result * 59 + (($extraInfoRule == null) ? 43 : $extraInfoRule.hashCode());
        final Object $playerDeathsRule = this.getPlayerDeathsRule();
        result = result * 59 + (($playerDeathsRule == null) ? 43 : $playerDeathsRule.hashCode());
        final Object $additionalPainRule = this.getAdditionalPainRule();
        result = result * 59 + (($additionalPainRule == null) ? 43 : $additionalPainRule.hashCode());
        final Object $syncClothingRule = this.getSyncClothingRule();
        result = result * 59 + (($syncClothingRule == null) ? 43 : $syncClothingRule.hashCode());
        final Object $distanceRule = this.getDistanceRule();
        result = result * 59 + (($distanceRule == null) ? 43 : $distanceRule.hashCode());
        final Object $chatRule = this.getChatRule();
        result = result * 59 + (($chatRule == null) ? 43 : $chatRule.hashCode());
        final Object $rateLimits = this.getRateLimits();
        result = result * 59 + (($rateLimits == null) ? 43 : $rateLimits.hashCode());
        return result;
    }
}
