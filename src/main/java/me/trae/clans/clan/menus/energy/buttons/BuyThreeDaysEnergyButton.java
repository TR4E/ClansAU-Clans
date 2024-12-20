package me.trae.clans.clan.menus.energy.buttons;

import me.trae.clans.clan.menus.energy.EnergyButton;
import me.trae.clans.clan.menus.energy.EnergyMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BuyThreeDaysEnergyButton extends EnergyButton {

    public BuyThreeDaysEnergyButton(final EnergyMenu menu) {
        super(menu, 6, new ItemStack(Material.EMERALD));
    }

    @Override
    public String getDurationText() {
        return "3 Days";
    }

    @Override
    public int getHourMultiplier() {
        return 72;
    }
}