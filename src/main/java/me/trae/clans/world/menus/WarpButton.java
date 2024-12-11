package me.trae.clans.world.menus;

import me.trae.clans.world.menus.interfaces.IWarpButton;
import me.trae.core.Core;
import me.trae.core.countdown.CountdownManager;
import me.trae.core.menu.Button;
import me.trae.core.teleport.Teleport;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class WarpButton extends Button<WarpMenu> implements IWarpButton {

    public WarpButton(final WarpMenu menu, final int slot, final ItemStack itemStack) {
        super(menu, slot, itemStack);
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        if (clickType != ClickType.LEFT) {
            return;
        }

        final Teleport teleport = new Teleport(0L, player, this.getClan().getHome()) {
            @Override
            public void onTeleport(final Player player) {
            }
        };

        this.getMenu().getManager().getInstance(Core.class).getManagerByClass(CountdownManager.class).addCountdown(teleport);
    }
}