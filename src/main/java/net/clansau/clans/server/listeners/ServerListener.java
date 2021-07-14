package net.clansau.clans.server.listeners;

import net.clansau.clans.server.ServerManager;
import net.clansau.clans.server.events.ServerStartEvent;
import net.clansau.clans.server.events.ServerStopEvent;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class ServerListener extends CoreListener<ServerManager> {

    public ServerListener(final ServerManager manager) {
        super(manager, "Server Listener");
    }

    @EventHandler
    public void onServerStart(final ServerStartEvent e) {
        getInstance().getConfigManager().updateModules();
        UtilMessage.log("ClansAU-Clans", "Plugin Status: " + ChatColor.GREEN + "Enabled");
    }

    @EventHandler
    public void onServerStop(final ServerStopEvent e) {
        UtilMessage.log("ClansAU-Clans", "Plugin Status: " + ChatColor.RED + "Disabled");
    }
}