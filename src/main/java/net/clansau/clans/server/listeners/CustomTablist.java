package net.clansau.clans.server.listeners;

import net.clansau.clans.server.ServerManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.server.events.PlayerTablistEvent;
import net.clansau.core.utility.UtilPlayer;
import net.clansau.core.utility.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CustomTablist extends CoreListener<ServerManager> {

    public CustomTablist(final ServerManager manager) {
        super(manager, "Custom Tablist");
    }

    @EventHandler
    public void onPlayerTablist(final PlayerTablistEvent e) {
        if (e.isCancelled()) {
            return;
        }
        e.setHeader(ChatColor.GOLD.toString() + ChatColor.BOLD + "ClansAU Clans\n");
        e.setFooter(ChatColor.GOLD.toString() + ChatColor.BOLD + "\nOnline: " + ChatColor.YELLOW + Bukkit.getOnlinePlayers().size() + ChatColor.GOLD + ChatColor.BOLD + " TPS: " + ChatColor.YELLOW + UtilServer.getTPS() + ChatColor.GOLD + ChatColor.BOLD + " Ping: " + ChatColor.YELLOW + UtilPlayer.getPing(e.getPlayer()));
        e.updateTab();
    }

    @Override
    public void shutdownModule() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            UtilPlayer.setPlayerTablist(player, "", "");
        }
    }
}