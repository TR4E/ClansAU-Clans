package me.trae.clans.world.menus.buttons;

import me.trae.clans.world.menus.WarpButton;
import me.trae.clans.world.menus.WarpMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class BlueSpawnWarpButton extends WarpButton {

    public BlueSpawnWarpButton(final WarpMenu menu) {
        super(menu, 1, new ItemStack(Material.STAINED_CLAY, 1, (short) 11));
    }

    @Override
    public String getDisplayName() {
        return "<blue>Blue Spawn";
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }
}