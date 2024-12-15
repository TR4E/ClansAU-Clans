package me.trae.clans.world.menus.buttons;

import me.trae.clans.world.menus.WarpButton;
import me.trae.clans.world.menus.WarpMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class LakeWarpButton extends WarpButton {

    public LakeWarpButton(final WarpMenu menu) {
        super(menu, 6, new ItemStack(Material.FISHING_ROD));
    }

    @Override
    public String getDisplayName() {
        return "<white>Lake";
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }
}