package me.trae.clans.world.menus.buttons;

import me.trae.clans.world.menus.WarpButton;
import me.trae.clans.world.menus.WarpMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class RedSpawnWarpButton extends WarpButton {

    public RedSpawnWarpButton(final WarpMenu menu) {
        super(menu, 7, new ItemStack(Material.STAINED_CLAY, 1, (short) 14));
    }

    @Override
    public String getDisplayName() {
        return "<red>Red Spawn";
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }
}