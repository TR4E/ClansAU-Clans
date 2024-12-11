package me.trae.clans.world.menus.buttons;

import me.trae.clans.world.menus.WarpButton;
import me.trae.clans.world.menus.WarpMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class FieldsWarpButton extends WarpButton {

    public FieldsWarpButton(final WarpMenu menu) {
        super(menu, 8, new ItemStack(Material.IRON_PICKAXE));
    }

    @Override
    public String getDisplayName() {
        return "<white>Fields";
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }
}