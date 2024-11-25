package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class HandleAgilityHelmetOnRespawn extends SpigotListener<Clans, PerkManager> {

    private final AgilityHelmet PERK;

    public HandleAgilityHelmetOnRespawn(final PerkManager manager) {
        super(manager);

        this.PERK = manager.getModuleByClass(AgilityHelmet.class);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        if (!(this.PERK.isUserByPlayer(player))) {
            return;
        }

        if (this.PERK.isEquipped(player)) {
            return;
        }

        final ItemStack itemStack = new ItemStack(this.PERK.getMaterial());

        if (UtilItem.contains(player, itemStack, 1)) {
            return;
        }

        UtilItem.insert(player, itemStack);
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        event.getDrops().removeIf(itemStack -> itemStack != null && itemStack.getType() == this.PERK.getMaterial());
    }
}