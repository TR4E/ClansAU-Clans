package net.clansau.clans;

import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.ClanRepository;
import net.clansau.clans.config.ConfigManager;
import net.clansau.clans.config.OptionsManager;
import net.clansau.clans.server.ServerManager;
import net.clansau.clans.server.events.ServerStartEvent;
import net.clansau.clans.server.events.ServerStopEvent;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.config.IOptionsManager;
import net.clansau.core.framework.Plugin;
import org.bukkit.Bukkit;

public class Clans extends Plugin {

    @Override
    protected void onPluginEnable() {
        Bukkit.getServer().getPluginManager().callEvent(new ServerStartEvent());
    }

    @Override
    protected void onPluginDisable() {
        Bukkit.getServer().getPluginManager().callEvent(new ServerStopEvent());
    }

    @Override
    protected void registerManagers() {
        addManager(new ConfigManager(this));
        addManager(new OptionsManager(this));
        addManager(new ClanRepository(this));
        addManager(new ClanManager(this));
        addManager(new ServerManager(this));
        addManager(new WorldManager(this));
    }

    @Override
    public IOptionsManager getOptionsManager() {
        return getManager(OptionsManager.class);
    }
}