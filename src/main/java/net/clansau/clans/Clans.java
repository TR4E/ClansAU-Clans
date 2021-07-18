package net.clansau.clans;

import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.ClanRepository;
import net.clansau.clans.config.ConfigManager;
import net.clansau.clans.config.OptionsManager;
import net.clansau.clans.farming.FarmingManager;
import net.clansau.clans.fields.FieldsManager;
import net.clansau.clans.fields.FieldsRepository;
import net.clansau.clans.server.ServerManager;
import net.clansau.clans.server.events.ServerStartEvent;
import net.clansau.clans.server.events.ServerStopEvent;
import net.clansau.clans.weapon.WeaponManager;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.config.framework.IConfigManager;
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
        addManager(new FarmingManager(this));
        addManager(new FieldsRepository(this));
        addManager(new FieldsManager(this));
        addManager(new ServerManager(this));
        addManager(new WeaponManager(this));
        addManager(new WorldManager(this));
    }

    @Override
    public final IConfigManager getConfigManager() {
        return getManager(ConfigManager.class);
    }
}