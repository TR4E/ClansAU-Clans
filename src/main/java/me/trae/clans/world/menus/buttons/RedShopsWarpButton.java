package me.trae.clans.world.menus.buttons;

import me.trae.clans.world.menus.WarpButton;
import me.trae.clans.world.menus.WarpMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class RedShopsWarpButton extends WarpButton {

    public RedShopsWarpButton(final WarpMenu menu) {
        super(menu, 5, new ItemStack(Material.WOOL, 1, (short) 14));
    }

    @Override
    public String getDisplayName() {
        return "<red>Red Shops";
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }
}