package me.trae.clans.weapon.modules;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.CurrencyItem;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DisableCurrencyDiscJukeboxInteraction extends SpigotListener<Clans, WeaponManager> {

    public DisableCurrencyDiscJukeboxInteraction(final WeaponManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractPreventJukebox(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (block.getType() != Material.JUKEBOX) {
            return;
        }

        final ItemStack itemStack = event.getItem();

        if (!(this.getInstanceByClass(Core.class).getManagerByClass(me.trae.core.weapon.WeaponManager.class).getWeaponByItemStack(itemStack) instanceof CurrencyItem)) {
            return;
        }

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(event.getPlayer()).isAdministrating()) {
            return;
        }

        event.setCancelled(true);
    }
}