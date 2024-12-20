package me.trae.clans.clan.menus.energy.buttons;

import me.trae.clans.clan.menus.energy.EnergyButton;
import me.trae.clans.clan.menus.energy.EnergyMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BuyOneDayEnergyButton extends EnergyButton {

    public BuyOneDayEnergyButton(final EnergyMenu menu) {
        super(menu, 4, new ItemStack(Material.EMERALD));
    }

    @Override
    public String getDurationText() {
        return "1 Day";
    }

    @Override
    public int getHourMultiplier() {
        return 24;
    }
}