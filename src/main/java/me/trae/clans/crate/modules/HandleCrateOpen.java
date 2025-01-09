package me.trae.clans.crate.modules;

import me.trae.clans.Clans;
import me.trae.clans.crate.Crate;
import me.trae.clans.crate.CrateManager;
import me.trae.clans.crate.menu.CrateOpenMenu;
import me.trae.core.Core;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilLogger;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.enums.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HandleCrateOpen extends SpigotListener<Clans, CrateManager> {

    public HandleCrateOpen(final CrateManager manager) {
        super(manager);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!(ActionType.RIGHT_CLICK.isAction(event.getAction()))) {
            return;
        }

        final Player player = event.getPlayer();

        final ItemStack itemStack = player.getEquipment().getItemInHand();

        final Crate crate = this.getManager().getCrateByItemStack(itemStack);
        if (crate == null) {
            return;
        }

        event.setCancelled(true);

        if (!(crate.isEnabled())) {
            return;
        }

        if (!(this.getInstanceByClass(Core.class).getManagerByClass(RechargeManager.class).add(player, "Open Crate", 250L, false))) {
            return;
        }

        UtilItem.remove(player, player.getEquipment().getItemInHand(), 1);

        final Crate finalCrate = crate;

        UtilMenu.open(new CrateOpenMenu(this.getManager(), player, finalCrate) {
            @Override
            public Crate getCrate() {
                return finalCrate;
            }
        });

        UtilLogger.log(Clans.class, "Crates", "Opened", UtilString.format("%s opened a %s", player.getName(), crate.getName()));
    }
}