package me.trae.clans;

import me.trae.clans.clan.ClanManager;
import me.trae.clans.config.ConfigManager;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.item.ItemManager;
import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.plugin.MiniPlugin;

public class Clans extends MiniPlugin {

    @Override
    public void registerManagers() {
        addManager(new ClanManager(this));
        addManager(new ConfigManager(this));
        addManager(new GamerManager(this));
        addManager(new ItemManager(this));
        addManager(new WeaponManager(this));
        addManager(new WorldManager(this));
    }
}