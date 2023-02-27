// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.admin;

import zombie.input.GameKeyboard;
import java.lang.reflect.Field;
import zombie.chat.ChatManager;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoGridSquare;
import zombie.network.PacketTypes;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.iso.objects.IsoMannequin;
import zombie.SoundManager;
import java.util.concurrent.ThreadLocalRandom;
import zombie.characters.skills.PerkFactory;
import java.util.Iterator;
import zombie.network.GameClient;
import zombie.ui.UITextBox2;
import zombie.ui.UIFont;
import java.util.HashMap;
import java.util.LinkedList;
import zombie.ui.UIElement;
import zombie.ui.UIEventHandler;
import dev.zomboid.extend.AbstractEventHandler;
import java.util.concurrent.atomic.AtomicInteger;
import zombie.characters.IsoPlayer;
import java.util.Map;
import java.util.List;
import zombie.ui.NewWindow;

public class AdminToolWindow extends NewWindow
{
    private final List<CheatPlayer> orderedElements;
    private final Map<IsoPlayer, CheatPlayer> playerMap;
    private long target;
    private long nextPacketTime;
    private boolean enablecheatwindow;
    private boolean targetFire;
    private boolean targetSmoke;
    private boolean targetKill;
    private int yShift;
    
    private boolean enablecheatwindow(final boolean val) {
        return !val;
    }
    
    private void pushButtonHorizontal(final AtomicInteger x, final int y, final String text, final String name, final AbstractEventHandler handler) {
        final AdminToolButton button = new AdminToolButton((UIEventHandler)handler, x.get(), y, text, name);
        this.AddChild((UIElement)button);
        x.addAndGet(button.getWidth().intValue());
        x.addAndGet(5);
    }
    
    private void pushCheckBoxHorizontal(final AtomicInteger x, final int y, final String text, final String name, final AbstractEventHandler handler) {
        final AdminToolCheckBox box = new AdminToolCheckBox((UIEventHandler)handler, x.get(), y, text, name);
        this.AddChild((UIElement)box);
        x.addAndGet(box.getWidth().intValue());
        x.addAndGet(5);
    }
    
    public AdminToolWindow() {
        super(15, 15, 450, 1300, false);
        this.orderedElements = new LinkedList<CheatPlayer>();
        this.playerMap = new HashMap<IsoPlayer, CheatPlayer>();
        this.nextPacketTime = 0L;
        this.enablecheatwindow = true;
        this.targetFire = false;
        this.targetSmoke = false;
        this.targetKill = false;
        this.yShift = 0;
        final UITextBox2 nameBox = new UITextBox2(UIFont.Small, 340, 20, 100, 15, "Base.", true);
        this.AddChild((UIElement)nameBox);
        nameBox.setEditable(true);
        final UITextBox2 titleBox = new UITextBox2(UIFont.Small, 0, 0, 80, 15, "PZ - Kybe", true);
        this.AddChild((UIElement)titleBox);
        titleBox.setEditable(false);
        final AtomicInteger x = new AtomicInteger(5);
        final AtomicInteger lmao = new AtomicInteger(280);
        this.pushCheckBoxHorizontal(x, 25, "God", "godmode_button", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                for (final IsoPlayer p : GameClient.instance.getPlayers()) {
                    if (p.isLocalPlayer()) {
                        p.setGodMod(toggled == 1);
                        p.setCanHearAll(toggled == 1);
                        p.setUnlimitedCarry(toggled == 1);
                        p.setUnlimitedEndurance(toggled == 1);
                        GameClient.sendPlayerExtraInfo(p);
                    }
                }
            }
        });
        this.pushCheckBoxHorizontal(x, 25, "Noclip", "noclip_button", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                for (final IsoPlayer p : GameClient.instance.getPlayers()) {
                    if (p.isLocalPlayer()) {
                        p.setNoClip(toggled == 1);
                    }
                }
            }
        });
        this.pushCheckBoxHorizontal(x, 25, "Ghost", "ghost_button", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                for (final IsoPlayer p : GameClient.instance.getPlayers()) {
                    if (p.isLocalPlayer()) {
                        p.setInvisible(toggled == 1);
                        p.setGhostMode(toggled == 1);
                    }
                }
            }
        });
        this.pushButtonHorizontal(x, 25, "XP", "xp_button", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                for (final IsoPlayer p : GameClient.instance.getPlayers()) {
                    if (p.isLocalPlayer()) {
                        p.getXp().AddXP(PerkFactory.Perks.Axe, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Blunt, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.SmallBlunt, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.LongBlade, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.SmallBlade, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Spear, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Maintenance, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Aiming, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Reloading, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Cooking, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Farming, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Doctor, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Electricity, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.MetalWelding, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Mechanics, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Tailoring, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Fishing, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Trapping, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Fitness, 1000000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Strength, 1000000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Sprinting, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Nimble, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Sneak, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Lightfoot, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.PlantScavenging, 100000.0f);
                        p.getXp().AddXP(PerkFactory.Perks.Woodwork, 100000.0f);
                    }
                }
            }
        });
        this.pushCheckBoxHorizontal(x, 25, "SetAdmin", "setadmin_button", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                for (final IsoPlayer p : GameClient.instance.getPlayers()) {
                    if (p.isLocalPlayer()) {
                        (p.accessLevel = "admin").equals("admin");
                        p.setShowAdminTag(toggled == 0);
                    }
                }
            }
        });
        x.set(5);
        this.pushButtonHorizontal(lmao, 25, "Give Item", "custom_item", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                for (final IsoPlayer p : GameClient.instance.getPlayers()) {
                    if (p.isLocalPlayer()) {
                        p.getInventory().AddItems(nameBox.Text, 1);
                    }
                }
            }
        });
        this.pushButtonHorizontal(x, 45, "Up", "up_button", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                if (AdminToolWindow.this.yShift > 0) {
                    final AdminToolWindow this$0 = AdminToolWindow.this;
                    --this$0.yShift;
                }
            }
        });
        this.pushButtonHorizontal(x, 45, "Down", "down_button", new AbstractEventHandler() {
            @Override
            public void Selected(final String s, final int toggled, final int i1) {
                if (AdminToolWindow.this.yShift < AdminToolWindow.this.orderedElements.size() - 1) {
                    final AdminToolWindow this$0 = AdminToolWindow.this;
                    ++this$0.yShift;
                }
            }
        });
    }
    
    private void updateName(final IsoPlayer p, final UITextBox2 box) {
        final StringBuilder name = new StringBuilder();
        name.append(p.getDisplayName());
        if (p.accessLevel.equalsIgnoreCase("admin")) {
            name.append("*");
        }
        if (p.isInvisible()) {
            name.append("!");
        }
        box.SetText(name.toString());
    }
    
    private void rebasePlayers() {
        final int count = Math.min(10, this.orderedElements.size() - this.yShift);
        this.setHeight((double)Math.max(72 + count * 25, 80));
        for (final CheatPlayer cp : this.orderedElements) {
            for (final UIElement e : cp.elements) {
                e.visible = false;
            }
        }
        if (count < 0) {
            return;
        }
        int y = 68;
        for (int i = this.yShift; i < this.yShift + count; ++i) {
            final CheatPlayer cp2 = this.orderedElements.get(i);
            this.updateName(cp2.player, (UITextBox2)cp2.elements.get(0));
            for (final UIElement e2 : cp2.elements) {
                e2.y = y;
                e2.visible = true;
            }
            y += 25;
        }
    }
    
    private void createNewPlayers() {
        for (final IsoPlayer p : GameClient.instance.getPlayers()) {
            if (!this.playerMap.containsKey(p)) {
                final CheatPlayer cp = new CheatPlayer(p);
                int x = 5;
                final UITextBox2 nameLabel = new UITextBox2(UIFont.Small, x, 0, 120, 20, "", true);
                cp.elements.add((UIElement)nameLabel);
                x += (int)(Object)nameLabel.getWidth();
                x += 5;
                final AdminToolButton killButton = new AdminToolButton((UIEventHandler)new AbstractEventHandler() {
                    @Override
                    public void Selected(final String s, final int i, final int i1) {
                        Cheats.kill(p, KillType.NORMAL_HIT);
                    }
                }, x, 0, "Kill", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, p.getDisplayName()));
                cp.elements.add(killButton);
                x += (int)(Object)killButton.getWidth();
                x += 5;
                final AdminToolButton lagButton = new AdminToolButton((UIEventHandler)new AbstractEventHandler() {
                    @Override
                    public void Selected(final String s, final int i, final int i1) {
                        final float scale = 1.0f;
                        final float offX = ThreadLocalRandom.current().nextFloat() * scale - scale;
                        final float offY = ThreadLocalRandom.current().nextFloat() * scale - scale;
                        final float offZ = ThreadLocalRandom.current().nextFloat() * scale - scale;
                        Cheats.safeTeleport(p, p.x + offX, p.y + offY, p.z + offZ);
                    }
                }, x, 0, "Lag", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, p.getDisplayName()));
                cp.elements.add(lagButton);
                x += (int)(Object)lagButton.getWidth();
                x += 5;
                final AdminToolButton teleportButton = new AdminToolButton((UIEventHandler)new AbstractEventHandler() {
                    @Override
                    public void Selected(final String s, final int i, final int i1) {
                        for (final IsoPlayer p2 : GameClient.instance.getPlayers()) {
                            if (p2.isLocalPlayer()) {
                                Cheats.safeTeleport(p2, p.x, p.y, p.z);
                            }
                        }
                    }
                }, x, 0, "Tele", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, p.getDisplayName()));
                cp.elements.add(teleportButton);
                x += (int)(Object)teleportButton.getWidth();
                x += 5;
                final AdminToolButton earRapeButton = new AdminToolButton((UIEventHandler)new AbstractEventHandler() {
                    @Override
                    public void Selected(final String s, final int i, final int i1) {
                        final IsoGridSquare sq = p.square;
                        if (sq != null) {
                            SoundManager.instance.PlayWorldSound("BurnedObjectExploded", sq, 100.0f, 100.0f, 100.0f, true);
                            final IsoMannequin manne = new IsoMannequin(p.getCell());
                            manne.square = sq;
                            manne.doNotSync = false;
                            manne.setOutlineOnMouseover(true);
                            manne.setSprite(IsoSpriteManager.instance.getSprite("furniture_tables_high_01_37"));
                            sq.getObjects().add((Object)manne);
                            final ByteBufferWriter byteBufferWriter = GameClient.connection.startPacket();
                            PacketTypes.PacketType.AddItemToMap.doPacket(byteBufferWriter);
                            byteBufferWriter.putByte((byte)24);
                            manne.writeToRemoteBuffer(byteBufferWriter);
                            PacketTypes.PacketType.AddItemToMap.send(GameClient.connection);
                            sq.getObjects().remove((Object)manne);
                        }
                    }
                }, x, 0, "Ear Rape", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, p.getDisplayName()));
                cp.elements.add(earRapeButton);
                x += (int)(Object)earRapeButton.getWidth();
                x += 5;
                final AdminToolButton targetButton = new AdminToolButton((UIEventHandler)new AbstractEventHandler() {
                    @Override
                    public void Selected(final String s, final int i, final int i1) {
                        AdminToolWindow.this.target = p.getSteamID();
                        try {
                            final Field f = ChatManager.class.getDeclaredField("player");
                            f.setAccessible(true);
                            f.set(ChatManager.getInstance(), p);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, x, 0, "Target", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, p.getDisplayName()));
                cp.elements.add(targetButton);
                x += (int)(Object)targetButton.getWidth();
                x += 5;
                this.AddChild((UIElement)nameLabel);
                this.AddChild((UIElement)killButton);
                this.AddChild((UIElement)lagButton);
                this.AddChild((UIElement)teleportButton);
                this.AddChild((UIElement)earRapeButton);
                this.AddChild((UIElement)targetButton);
                this.playerMap.put(p, cp);
                this.orderedElements.add(cp);
            }
        }
    }
    
    private void removeOldPlayers() {
        final Iterator<IsoPlayer> it = this.playerMap.keySet().iterator();
        while (it.hasNext()) {
            final IsoPlayer p = it.next();
            final CheatPlayer cp = this.playerMap.get(p);
            if (!GameClient.instance.getPlayers().contains(p)) {
                for (final UIElement e : cp.elements) {
                    this.RemoveChild(e);
                }
                this.orderedElements.remove(cp);
                it.remove();
            }
        }
    }
    
    public void render() {
        super.render();
    }
    
    private IsoPlayer findTarget() {
        for (final IsoPlayer p : GameClient.instance.getPlayers()) {
            if (p.getSteamID() == this.target) {
                return p;
            }
        }
        return null;
    }
    
    private void applyTargetEffects() {
        final IsoPlayer t = this.findTarget();
        if (t != null) {
            if (this.targetFire) {
                Cheats.startFire(t, true);
            }
            if (this.targetSmoke) {
                Cheats.startFire(t, false);
            }
            if (this.targetKill) {
                Cheats.kill(t, KillType.NORMAL_HIT);
            }
        }
    }
    
    private void timer() {
        final long t = System.currentTimeMillis();
        if (t > this.nextPacketTime) {
            this.applyTargetEffects();
            this.nextPacketTime = t + ThreadLocalRandom.current().nextInt(900, 1400);
        }
    }
    
    public void update() {
        if (GameKeyboard.isKeyDown(73)) {
            try {
                Thread.sleep(50L);
            }
            catch (InterruptedException ex) {}
            this.enablecheatwindow = !this.enablecheatwindow;
        }
        this.visible = this.enablecheatwindow;
        this.setAlwaysOnTop(this.enablecheatwindow);
        this.removeOldPlayers();
        this.createNewPlayers();
        this.rebasePlayers();
        this.timer();
        super.update();
    }
    
    private class CheatPlayer
    {
        private final IsoPlayer player;
        private final List<UIElement> elements;
        
        public CheatPlayer(final IsoPlayer player) {
            this.elements = new LinkedList<UIElement>();
            this.player = player;
        }
    }
}
