package me.trae.clans.crate.modules;

import me.trae.clans.Clans;
import me.trae.clans.crate.Crate;
import me.trae.clans.crate.CrateManager;
import me.trae.clans.crate.menu.CratePreviewMenu;
import me.trae.core.Core;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.enums.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HandleCratePreview extends SpigotListener<Clans, CrateManager> {

    public HandleCratePreview(final CrateManager manager) {
        super(manager);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!(ActionType.LEFT_CLICK.isAction(event.getAction()))) {
            return;
        }

        final Player player = event.getPlayer();

        final ItemStack itemStack = player.getEquipment().getItemInHand();

        final Crate crate = this.getManager().getCrateByItemStack(itemStack);
        if (crate == null) {
            return;
        }

        if (!(crate.isEnabled())) {
            return;
        }

        if (!(this.getInstance(Core.class).getManagerByClass(RechargeManager.class).add(player, "Preview Crate", 250L, false))) {
            return;
        }

        UtilMenu.open(new CratePreviewMenu(this.getManager(), player, crate) {
            @Override
            public Crate getCrate() {
                return crate;
            }
        });
    }
}