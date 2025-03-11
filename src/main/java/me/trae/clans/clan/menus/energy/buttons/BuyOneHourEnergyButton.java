package me.trae.clans.clan.menus.energy.buttons;

import me.trae.clans.clan.menus.energy.EnergyButton;
import me.trae.clans.clan.menus.energy.EnergyMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BuyOneHourEnergyButton extends EnergyButton {

    public BuyOneHourEnergyButton(final EnergyMenu menu) {
        super(menu, 2, new ItemStack(Material.EMERALD_ORE));
    }

    @Override
    public String getDurationText() {
        return "1 Hour";
    }

    @Override
    public int getHourMultiplier() {
        return 1;
    }
}