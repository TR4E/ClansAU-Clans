package me.trae.clans.world.menus.buttons;

import me.trae.clans.world.menus.WarpButton;
import me.trae.clans.world.menus.WarpMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class ClanHomeWarpButton extends WarpButton {

    public ClanHomeWarpButton(final WarpMenu menu) {
        super(menu, 4, new ItemStack(Material.BED));
    }

    @Override
    public String getDisplayName() {
        return "<aqua>Clan Home";
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }
}