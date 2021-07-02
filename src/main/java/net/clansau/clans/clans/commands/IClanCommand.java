package net.clansau.clans.clans.commands;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class IClanCommand {

    private final ClanManager manager;

    public IClanCommand(final ClanManager manager, final Player player, final String[] args) {
        this.manager = manager;
        if (this instanceof Listener) {
            Bukkit.getServer().getPluginManager().registerEvents((Listener) this, getManager().getInstance());
        }
        this.run(player, args);
    }

    protected final ClanManager getManager() {
        return this.manager;
    }

    protected final Clans getInstance() {
        return (Clans) this.getManager().getInstance();
    }

    protected abstract void run(final Player player, final String[] args);
}