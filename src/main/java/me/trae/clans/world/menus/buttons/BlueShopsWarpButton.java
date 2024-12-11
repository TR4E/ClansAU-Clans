package me.trae.clans.world.menus.buttons;

import me.trae.clans.world.menus.WarpButton;
import me.trae.clans.world.menus.WarpMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class BlueShopsWarpButton extends WarpButton {

    public BlueShopsWarpButton(final WarpMenu menu) {
        super(menu, 3, new ItemStack(Material.WOOL, 1, (short) 11));
    }

    @Override
    public String getDisplayName() {
        return "<blue>Blue Shops";
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }
}