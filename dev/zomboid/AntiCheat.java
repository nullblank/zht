// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import dev.zomboid.util.RateLimiter;
import zombie.network.PacketTypes;
import zombie.chat.ChatMessage;
import zombie.network.chat.ChatServer;
import zombie.GameWindow;
import zombie.network.packets.hit.HitCharacterPacket;
import zombie.network.GameServer;
import zombie.characters.IsoZombie;
import zombie.network.ServerMap;
import zombie.network.packets.DeadPlayerPacket;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import zombie.network.ServerWorldDatabase;
import zombie.network.ZomboidNetData;
import java.io.IOException;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.Iterator;
import zombie.core.raknet.UdpConnection;
import zombie.characters.IsoPlayer;
import zombie.debug.DebugLog;
import zombie.network.packets.hit.Square;
import zombie.network.packets.hit.PlayerHitSquarePacket;
import zombie.network.packets.hit.Zombie;
import zombie.network.packets.hit.PlayerHitZombiePacket;
import zombie.network.packets.hit.Player;
import zombie.network.packets.hit.PlayerHitPlayerPacket;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.Map;

public class AntiCheat
{
    private final Map<String, CustomNetworkData> customNetworkDataMap;
    private Field playerHitZombiePacketTarget;
    private Field zombieZombie;
    private Field playerHitPlayerPacketTarget;
    private Field playerHitPlayerPacketHit;
    private Field playerPlayer;
    private Field playerHitSquarePacketSquare;
    private Field squareX;
    private Field squareY;
    private Field squareZ;
    private AntiCheatCfg cfg;
    
    public AntiCheat() {
        this.customNetworkDataMap = new HashMap<String, CustomNetworkData>();
        this.cfg = new AntiCheatCfg();
        try {
            (this.playerHitPlayerPacketTarget = PlayerHitPlayerPacket.class.getDeclaredField("target")).setAccessible(true);
            (this.playerHitPlayerPacketHit = PlayerHitPlayerPacket.class.getDeclaredField("hit")).setAccessible(true);
            (this.playerPlayer = Player.class.getDeclaredField("player")).setAccessible(true);
            (this.playerHitZombiePacketTarget = PlayerHitZombiePacket.class.getDeclaredField("target")).setAccessible(true);
            (this.zombieZombie = Zombie.class.getDeclaredField("zombie")).setAccessible(true);
            (this.playerHitSquarePacketSquare = PlayerHitSquarePacket.class.getDeclaredField("square")).setAccessible(true);
            (this.squareX = Square.class.getDeclaredField("positionX")).setAccessible(true);
            (this.squareY = Square.class.getDeclaredField("positionY")).setAccessible(true);
            (this.squareZ = Square.class.getDeclaredField("positionZ")).setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            DebugLog.log("Failed to initialized hidden field access");
        }
    }
    
    private boolean distanceCheck(final IsoPlayer a, final IsoPlayer b, final float distance) {
        return Math.sqrt(Math.pow(a.x - b.x, 2.0) + Math.pow(a.y - b.y, 2.0) + Math.pow(a.z - b.z, 2.0)) < distance;
    }
    
    private boolean distanceCheck(final UdpConnection a, final IsoPlayer b, final float distance) {
        for (final IsoPlayer a2 : a.players) {
            if (a2 != null && this.distanceCheck(a2, b, distance)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean distanceCheck(final IsoPlayer a, final float x, final float y, final float z, final float distance) {
        return Math.sqrt(Math.pow(a.x - x, 2.0) + Math.pow(a.y - y, 2.0) + Math.pow(a.z - z, 2.0)) < distance;
    }
    
    private boolean distanceCheck(final UdpConnection a, final float x, final float y, final float z, final float distance) {
        for (final IsoPlayer a2 : a.players) {
            if (a2 != null && this.distanceCheck(a2, x, y, z, distance)) {
                return true;
            }
        }
        return false;
    }
    
    private CustomNetworkData getCustomNetworkData(final UdpConnection con, final String name) {
        final CustomNetworkData data = this.customNetworkDataMap.computeIfAbsent(name, n -> new CustomNetworkData());
        data.connection = con;
        return data;
    }
    
    private CustomNetworkData getCustomNetworkData(final UdpConnection con) {
        for (final CustomNetworkData data : this.customNetworkDataMap.values()) {
            if (data.connection == con) {
                return data;
            }
        }
        return null;
    }
    
    private CustomNetworkData getCustomNetworkDataAny(final UdpConnection con) {
        for (final IsoPlayer p : con.players) {
            if (p != null) {
                return this.getCustomNetworkData(con, p.getUsername());
            }
        }
        return null;
    }
    
    private IsoPlayer getPlayerFromConnection(final UdpConnection con, final int index) {
        if (index < 0) {
            return null;
        }
        if (index >= 4) {
            return null;
        }
        return con.players[index];
    }
    
    private IsoPlayer anyPlayerFromConnection(final UdpConnection con) {
        for (final IsoPlayer p : con.players) {
            if (p != null) {
                return p;
            }
        }
        return null;
    }
    
    private boolean playerBelongsToConnection(final UdpConnection con, final IsoPlayer p) {
        for (final IsoPlayer p2 : con.players) {
            if (p2 == p) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canCheat(final UdpConnection con) {
        return con.accessLevel.equalsIgnoreCase("admin") || con.accessLevel.equalsIgnoreCase("moderator");
    }
    
    private void reportToDiscord(final String msg) {
        final DiscordWebhook hook = new DiscordWebhook(this.cfg.getDiscordApi());
        hook.setUsername(".");
        hook.setContent(StringEscapeUtils.escapeJava(msg));
        hook.setTts(false);
        try {
            hook.execute();
        }
        catch (IOException e) {
            DebugLog.log("Failed to execute discord hook");
        }
    }
    
    private void handleMalformedPacket(final UdpConnection con, final ZomboidNetData packet, final String reason) {
        if (this.canCheat(con)) {
            return;
        }
        String name = "No associated player";
        final IsoPlayer p = this.anyPlayerFromConnection(con);
        if (p != null) {
            name = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)Ljava/lang/String;, p.getUsername(), p.getSurname(), p.getForname(), p.getSteamID());
        }
        this.reportToDiscord(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, name, reason));
        DebugLog.log(invokedynamic(makeConcatWithConstants:(Lzombie/core/raknet/UdpConnection;)Ljava/lang/String;, con));
    }
    
    private void enforceActionBanSteamId(final UdpConnection con, final String reason) throws SQLException {
        for (final IsoPlayer p : con.players) {
            if (p != null) {
                ServerWorldDatabase.instance.banSteamID(Long.toString(p.getSteamID()), reason, true);
            }
        }
    }
    
    private void enforceActionBanIp(final UdpConnection con, final String reason) throws SQLException {
        for (final IsoPlayer p : con.players) {
            if (p != null) {
                ServerWorldDatabase.instance.banIp(con.ip, p.getUsername(), reason, true);
            }
        }
    }
    
    private void enforceAction(final UdpConnection con, final String reason, final AntiCheatAction action) {
        try {
            switch (action) {
                case BAN_STEAM_ID: {
                    this.enforceActionBanSteamId(con, reason);
                    break;
                }
                case BAN_IP: {
                    this.enforceActionBanIp(con, reason);
                    break;
                }
                case BAN_ALL: {
                    this.enforceActionBanSteamId(con, reason);
                    this.enforceActionBanIp(con, reason);
                    break;
                }
                case DISCONNECT: {
                    con.forceDisconnect();
                    break;
                }
            }
        }
        catch (Throwable e) {
            DebugLog.log("[enforceAction] Exception occurred during action.");
            con.forceDisconnect();
        }
    }
    
    private void handleViolation(final UdpConnection con, final String reason, final AntiCheatAction action) {
        if (this.canCheat(con)) {
            return;
        }
        DebugLog.log(invokedynamic(makeConcatWithConstants:(Lzombie/core/raknet/UdpConnection;Ljava/lang/String;)Ljava/lang/String;, con, reason));
        String name = "No associated player";
        String steam = "No associated steam";
        final IsoPlayer p = this.anyPlayerFromConnection(con);
        if (p != null) {
            name = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, p.getUsername(), p.getSurname(), p.getForname());
            steam = Long.toString(p.getSteamID());
        }
        this.reportToDiscord(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, name, steam, reason));
        this.enforceAction(con, reason, action);
    }
    
    private boolean validateSyncPerk(final CustomNetworkData data, final Perk perk, final int v) {
        if (data.lastKnownPerks.containsKey(perk)) {
            final int diff = Math.abs(data.lastKnownPerks.get(perk) - v);
            return diff <= this.cfg.getSyncPerksRule().getThreshold();
        }
        return true;
    }
    
    private void enforceSyncPerks(final UdpConnection con, final ZomboidNetData packet) {
        final ByteBuffer b = packet.buffer;
        final byte index = b.get();
        final int sneak = b.getInt();
        final int str = b.getInt();
        final int fit = b.getInt();
        final IsoPlayer player = this.getPlayerFromConnection(con, index);
        if (player == null) {
            return;
        }
        final CustomNetworkData data = this.getCustomNetworkData(con, player.getUsername());
        if (this.cfg.getSyncPerksRule().isEnabled()) {
            if (!data.validatePlayer(player)) {
                this.handleViolation(con, "[enforceSyncPerks] Failed to validate player", this.cfg.getSyncPerksRule().getAction());
                return;
            }
            if (!this.validateSyncPerk(data, Perk.SNEAK, sneak)) {
                this.handleViolation(con, "[enforceSyncPerks] Failed to validate sneak", this.cfg.getSyncPerksRule().getAction());
                return;
            }
            if (!this.validateSyncPerk(data, Perk.STR, str)) {
                this.handleViolation(con, "[enforceSyncPerks] Failed to validate str", this.cfg.getSyncPerksRule().getAction());
                return;
            }
            if (!this.validateSyncPerk(data, Perk.FIT, fit)) {
                this.handleViolation(con, "[enforceSyncPerks] Failed to validate fit", this.cfg.getSyncPerksRule().getAction());
                return;
            }
        }
        data.lastKnownPerks.put(Perk.SNEAK, sneak);
        data.lastKnownPerks.put(Perk.STR, str);
        data.lastKnownPerks.put(Perk.FIT, fit);
    }
    
    private void enforceTeleport(final UdpConnection con, final ZomboidNetData packet) {
        if (this.cfg.getTeleportRule().isEnabled()) {
            this.handleViolation(con, "[enforceTeleport] Not allowed to teleport", this.cfg.getTeleportRule().getAction());
        }
    }
    
    private void enforceExtraInfo(final UdpConnection con, final ZomboidNetData packet) {
        if (this.cfg.getExtraInfoRule().isEnabled()) {
            this.handleViolation(con, "[enforceExtraInfo] Not allowed to send extra info", this.cfg.getExtraInfoRule().getAction());
        }
    }
    
    private void enforceSendPlayerDeath(final UdpConnection con, final ZomboidNetData packet) {
        final DeadPlayerPacket dp = new DeadPlayerPacket();
        dp.parse(packet.buffer);
        final IsoPlayer target = dp.getPlayer();
        if (this.cfg.getPlayerDeathsRule().isEnabled() && !this.playerBelongsToConnection(con, target)) {
            this.handleViolation(con, "[enforceSendPlayerDeath] Sending player death to other player", this.cfg.getPlayerDeathsRule().getAction());
            return;
        }
        if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, target.x, target.y, target.z, 100.0f)) {
            this.handleViolation(con, "[enforceSendPlayerDeath] Player too far from death", this.cfg.getDistanceRule().getAction());
        }
    }
    
    private void enforceKillZombie(final UdpConnection con, final ZomboidNetData packet) {
        final short index = packet.buffer.getShort();
        final boolean fall = packet.buffer.get() != 0;
        final IsoZombie z = ServerMap.instance.ZombieMap.get(index);
        if (z == null) {
            return;
        }
        if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, z.x, z.y, z.z, 100.0f)) {
            this.handleViolation(con, "[enforceKillZombie] Player too far from death", this.cfg.getDistanceRule().getAction());
        }
    }
    
    private void enforceAdditionalPain(final UdpConnection con, final ZomboidNetData packet) {
        final short id = packet.buffer.getShort();
        final IsoPlayer target = GameServer.IDToPlayerMap.get(id);
        if (this.cfg.getAdditionalPainRule().isEnabled()) {
            this.handleViolation(con, "[enforceAdditionalPain] Sending additional pain packet", this.cfg.getAdditionalPainRule().getAction());
            return;
        }
        if (target == null) {
            this.handleMalformedPacket(con, packet, "[enforceAdditionalPain] Attempting to inflict additional pain to non-existent player");
            return;
        }
        if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, target.x, target.y, target.z, this.cfg.getDistanceRule().getThreshold())) {
            this.handleViolation(con, "[enforceAdditionalPain] Player too far away", this.cfg.getDistanceRule().getAction());
        }
    }
    
    private void enforceRemoveGlass(final UdpConnection con, final ZomboidNetData packet) {
        final short id = packet.buffer.getShort();
        final IsoPlayer target = GameServer.IDToPlayerMap.get(id);
        if (target == null) {
            this.handleMalformedPacket(con, packet, "[enforceRemoveGlass] Attempting to remove glass from non-existent player");
            return;
        }
        if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, target.x, target.y, target.z, this.cfg.getDistanceRule().getThreshold())) {
            this.handleViolation(con, "[enforceRemoveGlass] Player too far away", this.cfg.getDistanceRule().getAction());
        }
    }
    
    private void enforceRemoveBullet(final UdpConnection con, final ZomboidNetData packet) {
        final short id = packet.buffer.getShort();
        final IsoPlayer target = GameServer.IDToPlayerMap.get(id);
        if (target == null) {
            this.handleMalformedPacket(con, packet, "[enforceRemoveBullet] Attempting to remove bullet from non-existent player");
            return;
        }
        if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, target.x, target.y, target.z, this.cfg.getDistanceRule().getThreshold())) {
            this.handleViolation(con, "[enforceRemoveBullet] Player too far away", this.cfg.getDistanceRule().getAction());
        }
    }
    
    private void enforceCleanBurn(final UdpConnection con, final ZomboidNetData packet) {
        final short id = packet.buffer.getShort();
        final IsoPlayer target = GameServer.IDToPlayerMap.get(id);
        if (target == null) {
            this.handleMalformedPacket(con, packet, "[enforceCleanBurn] Attempting to clean burn of non-existent player");
            return;
        }
        if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, target.x, target.y, target.z, 100.0f)) {
            this.handleViolation(con, "[enforceCleanBurn] Player too far away", this.cfg.getDistanceRule().getAction());
        }
    }
    
    private void enforceSyncClothing(final UdpConnection con, final ZomboidNetData packet) {
        final short id = packet.buffer.getShort();
        final IsoPlayer target = GameServer.IDToPlayerMap.get(id);
        if (target == null) {
            this.handleMalformedPacket(con, packet, "[enforceSyncClothing] Attempting to sync clothing of non-existent player");
            return;
        }
        if (this.cfg.getSyncClothingRule().isEnabled() && !this.playerBelongsToConnection(con, target)) {
            this.handleViolation(con, "[enforceSyncClothing] Sending clothing change to other player", this.cfg.getSyncClothingRule().getAction());
        }
    }
    
    private void enforcePlayerHitSquarePacket(final UdpConnection con, final ZomboidNetData packet, final HitCharacterPacket hcp) {
        final PlayerHitSquarePacket hp = (PlayerHitSquarePacket)hcp;
        try {
            final Square sq = (Square)this.playerHitSquarePacketSquare.get(hp);
            final float x = (float)this.squareX.get(sq);
            final float y = (float)this.squareY.get(sq);
            final float z = (float)this.squareZ.get(sq);
            if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, x, y, z, 100.0f)) {
                this.handleViolation(con, "[enforcePlayerHitSquarePacket] Player too far from hit", this.cfg.getDistanceRule().getAction());
            }
        }
        catch (IllegalAccessException e) {
            DebugLog.log("Failed to read square info");
        }
    }
    
    private void enforcePlayerHitPlayerPacket(final UdpConnection con, final ZomboidNetData packet, final HitCharacterPacket hcp) {
        final PlayerHitPlayerPacket hp = (PlayerHitPlayerPacket)hcp;
        try {
            final Player plr = (Player)this.playerHitPlayerPacketTarget.get(hp);
            final IsoPlayer pl = (IsoPlayer)this.playerPlayer.get(plr);
            if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, pl.x, pl.y, pl.z, 100.0f)) {
                this.handleViolation(con, "[enforcePlayerHitPlayerPacket] Player too far from hit", this.cfg.getDistanceRule().getAction());
            }
        }
        catch (IllegalAccessException e) {
            DebugLog.log("Failed to read player info");
        }
    }
    
    private void enforcePlayerHitZombiePacket(final UdpConnection con, final ZomboidNetData packet, final HitCharacterPacket hcp) {
        final PlayerHitZombiePacket hp = (PlayerHitZombiePacket)hcp;
        try {
            final Zombie zombie = (Zombie)this.playerHitZombiePacketTarget.get(hp);
            final IsoZombie zm = (IsoZombie)this.zombieZombie.get(zombie);
            if (this.cfg.getDistanceRule().isEnabled() && !this.distanceCheck(con, zm.x, zm.y, zm.z, 100.0f)) {
                this.handleViolation(con, "[enforcePlayerHitZombiePacket] Player too far from hit", this.cfg.getDistanceRule().getAction());
            }
        }
        catch (IllegalAccessException e) {
            DebugLog.log("Failed to read zombie info");
        }
    }
    
    private void enforceWorldMessage(final UdpConnection con, final ZomboidNetData packet) {
        final String author = GameWindow.ReadStringUTF(packet.buffer);
        final String msg = GameWindow.ReadString(packet.buffer);
        if (this.cfg.getChatRule().isEnabled()) {
            boolean valid = false;
            for (final IsoPlayer p : con.players) {
                if (p != null && p.getUsername().equals(author)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                this.handleViolation(con, "[enforceWorldMessage] Player sent message with wrong username", this.cfg.getChatRule().getAction());
            }
        }
    }
    
    private void enforceChatMessageFromPlayer(final UdpConnection con, final ZomboidNetData packet) {
        final ChatMessage chatMessage = ChatServer.getInstance().unpackChatMessage(packet.buffer);
        final String author = chatMessage.getAuthor();
        if (this.cfg.getChatRule().isEnabled()) {
            boolean valid = false;
            for (final IsoPlayer p : con.players) {
                if (p != null && p.getUsername().equals(author)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                this.handleViolation(con, "[enforceChatMessageFromPlayer] Player sent message with wrong username", this.cfg.getChatRule().getAction());
            }
        }
    }
    
    public void enforce(final UdpConnection con, final ZomboidNetData packet) {
        final short id = packet.type;
        if (this.cfg.isRateLimited(id)) {
            final CustomNetworkData data = this.getCustomNetworkDataAny(con);
            if (data != null) {
                final RateLimiter limiter = data.createRateLimiter(id, this.cfg.getRateLimit(id));
                if (!limiter.check()) {
                    this.handleViolation(con, "Rate limiting", AntiCheatAction.DISCONNECT);
                    return;
                }
            }
        }
        if (id == PacketTypes.PacketType.SyncPerks.getId()) {
            this.enforceSyncPerks(con, packet);
        }
        else if (id == PacketTypes.PacketType.Teleport.getId()) {
            this.enforceTeleport(con, packet);
        }
        else if (id == PacketTypes.PacketType.ExtraInfo.getId()) {
            this.enforceExtraInfo(con, packet);
        }
        else if (id == PacketTypes.PacketType.PlayerDeath.getId()) {
            this.enforceSendPlayerDeath(con, packet);
        }
        else if (id == PacketTypes.PacketType.KillZombie.getId()) {
            this.enforceKillZombie(con, packet);
        }
        else if (id == PacketTypes.PacketType.AdditionalPain.getId()) {
            this.enforceAdditionalPain(con, packet);
        }
        else if (id == PacketTypes.PacketType.RemoveGlass.getId()) {
            this.enforceRemoveGlass(con, packet);
        }
        else if (id == PacketTypes.PacketType.RemoveBullet.getId()) {
            this.enforceRemoveBullet(con, packet);
        }
        else if (id == PacketTypes.PacketType.CleanBurn.getId()) {
            this.enforceCleanBurn(con, packet);
        }
        else if (id == PacketTypes.PacketType.SyncClothing.getId()) {
            this.enforceSyncClothing(con, packet);
        }
        else if (id == PacketTypes.PacketType.WorldMessage.getId()) {
            this.enforceWorldMessage(con, packet);
        }
        else if (id == PacketTypes.PacketType.ChatMessageFromPlayer.getId()) {
            this.enforceChatMessageFromPlayer(con, packet);
        }
        else if (id == PacketTypes.PacketType.HitCharacter.getId()) {
            final HitCharacterPacket hcp = HitCharacterPacket.process(packet.buffer);
            hcp.parse(packet.buffer);
            if (hcp instanceof PlayerHitSquarePacket) {
                this.enforcePlayerHitSquarePacket(con, packet, hcp);
            }
            else if (hcp instanceof PlayerHitPlayerPacket) {
                this.enforcePlayerHitPlayerPacket(con, packet, hcp);
            }
            else if (hcp instanceof PlayerHitZombiePacket) {
                this.enforcePlayerHitZombiePacket(con, packet, hcp);
            }
        }
    }
    
    public AntiCheatCfg getCfg() {
        return this.cfg;
    }
    
    public void setCfg(final AntiCheatCfg cfg) {
        this.cfg = cfg;
    }
    
    private enum Perk
    {
        SNEAK, 
        STR, 
        FIT;
        
        private static /* synthetic */ Perk[] $values() {
            return new Perk[] { Perk.SNEAK, Perk.STR, Perk.FIT };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private class CustomNetworkData
    {
        private final Map<Perk, Integer> lastKnownPerks;
        private final Map<Short, RateLimiter> rateLimiters;
        private UdpConnection connection;
        private IsoPlayer player;
        
        private CustomNetworkData() {
            this.lastKnownPerks = new HashMap<Perk, Integer>();
            this.rateLimiters = new HashMap<Short, RateLimiter>();
        }
        
        public RateLimiter createRateLimiter(final short type, final long delay) {
            return this.rateLimiters.computeIfAbsent(type, t -> new RateLimiter(delay));
        }
        
        public boolean validatePlayer(final IsoPlayer p) {
            if (this.player == null) {
                this.player = p;
            }
            return this.player.getUsername().equals(p.getUsername());
        }
    }
}
