package me.trae.clans.clan.menus.energy;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.menus.energy.buttons.BuyOneDayEnergyButton;
import me.trae.clans.clan.menus.energy.buttons.BuyOneHourEnergyButton;
import me.trae.clans.clan.menus.energy.buttons.BuyThreeDaysEnergyButton;
import me.trae.clans.clan.menus.energy.interfaces.IEnergyMenu;
import me.trae.core.menu.Menu;
import me.trae.core.utility.UtilColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class EnergyMenu extends Menu<Clans, ClanManager> implements IEnergyMenu {

    public EnergyMenu(final ClanManager manager, final Player player) {
        super(manager, player, 9, UtilColor.bold(ChatColor.GREEN) + "Clan Energy");
    }

    @Override
    public void fillPage(final Player player) {
        addButton(new BuyOneHourEnergyButton(this));
        addButton(new BuyOneDayEnergyButton(this));
        addButton(new BuyThreeDaysEnergyButton(this));
    }
}