// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid;

import zombie.ui.UIManager;
import java.nio.file.Path;
import java.io.IOException;
import zombie.debug.DebugLog;
import com.google.gson.Gson;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import dev.zomboid.admin.AdminToolWindow;

public class ZomboidApiCore
{
    public AntiCheatCfg antiCheatCfg;
    public AntiCheat antiCheat;
    private AdminToolWindow window;
    
    public void initServer() {
        this.antiCheatCfg = new AntiCheatCfg();
        this.antiCheat = new AntiCheat();
        final Path cfgPath = Paths.get("anticheat.json", new String[0]);
        if (Files.exists(cfgPath, new LinkOption[0])) {
            try {
                this.antiCheatCfg = new Gson().fromJson(Files.readString(cfgPath), AntiCheatCfg.class);
                this.antiCheat.setCfg(this.antiCheatCfg);
                DebugLog.log("Anti cheat config loaded");
                DebugLog.log(this.antiCheatCfg.toString());
            }
            catch (IOException e) {
                DebugLog.log("[initServer] Failed to parse anticheat configuration file 'anticheat.json'");
            }
        }
        else {
            DebugLog.log("[initServer] Failed to find anticheat configuration file 'anticheat.json'");
        }
    }
    
    public void initClient() {
        this.window = new AdminToolWindow();
        this.window.ResizeToFitY = false;
    }
    
    public void update() {
        if (!UIManager.UI.contains(this.window)) {
            UIManager.UI.add(this.window);
        }
        this.window.visible = true;
        this.window.setEnabled(true);
    }
}
