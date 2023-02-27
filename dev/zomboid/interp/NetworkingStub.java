// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.interp;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import zombie.core.raknet.UdpEngine;
import zombie.debug.DebugLog;
import zombie.GameWindow;
import zombie.network.PacketTypes;
import zombie.network.GameClient;
import zombie.network.ZomboidNetData;
import dev.zomboid.ZomboidApi;
import zombie.network.ZomboidNetDataPool;
import zombie.core.raknet.UdpConnection;
import java.nio.ByteBuffer;

public final class NetworkingStub
{
    public static void addIncomingServer(final short opcode, final ByteBuffer data, final UdpConnection connection) {
        final ZomboidNetData packet = ZomboidNetDataPool.instance.getLong(data.limit());
        data.mark();
        packet.read(opcode, data, connection);
        data.reset();
        ZomboidApi.core.antiCheat.enforce(connection, packet);
    }
    
    public static void addIncomingClient(final short opcode, final ByteBuffer data) {
        final ZomboidNetData packet = ZomboidNetDataPool.instance.getLong(data.limit());
        data.mark();
        packet.read(opcode, data, GameClient.connection);
        data.reset();
        if (packet.type == PacketTypes.PacketType.GetTableResult.getId()) {
            final int id = packet.buffer.getInt();
            final String table = GameWindow.ReadString(packet.buffer);
            DebugLog.log(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, table));
            for (int rows = packet.buffer.getInt(), i = 0; i < rows; ++i) {
                for (int columns = packet.buffer.getInt(), j = 0; j < columns; ++j) {
                    final String key = GameWindow.ReadString(packet.buffer);
                    final String val = GameWindow.ReadString(packet.buffer);
                    DebugLog.log(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, key, val));
                }
            }
        }
    }
    
    public static void decode(final UdpEngine engine, final ByteBuffer b) {
        b.mark();
        final int type = b.get() & 0xFF;
        System.out.println(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, type));
        if (type == 16) {
            final int index = b.get() & 0xFF;
            final long guid = engine.getPeer().getGuidOfPacket();
            GameClient.connection = new UdpConnection(engine, guid, index);
            final ThreadLocalRandom r = ThreadLocalRandom.current();
            final List<Integer> rnd = new LinkedList<Integer>();
            for (int i = 0; i < 255; ++i) {
                rnd.add(i);
            }
            for (int i = 0; i < 1000; ++i) {
                final int xi = r.nextInt(rnd.size());
                final int x = rnd.get(xi);
                final int yi = r.nextInt(rnd.size());
                final int y = rnd.get(yi);
                rnd.set(xi, y);
                rnd.set(yi, x);
            }
            final Iterator<Integer> iterator = rnd.iterator();
            while (iterator.hasNext()) {
                iterator.next();
            }
        }
        b.reset();
    }
    
    private NetworkingStub() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
