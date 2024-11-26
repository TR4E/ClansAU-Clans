package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.clans.Clans;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotSubListener;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.enums.ActionType;
import me.trae.core.world.events.PlayerItemInteractEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HandleAgilityHelmetEquip extends SpigotSubListener<Clans, AgilityHelmet> {

    public HandleAgilityHelmetEquip(final AgilityHelmet module) {
        super(module);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemInteract(final PlayerItemInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(ActionType.RIGHT_CLICK.isAction(event.getAction()))) {
            return;
        }

        final ItemStack itemStack = event.getItemStack();
        if (itemStack == null || itemStack.getType() != this.getModule().getMaterial()) {
            return;
        }

        final Player player = event.getPlayer();

        event.setOriginalCancelled(true);
        player.updateInventory();

        if (!(this.getModule().isUserByPlayer(player))) {
            return;
        }

        if (this.getModule().isEquipped(player)) {
            UtilMessage.message(player, this.getModule().getName(), "You already have it equipped!");
            return;
        }

        if (Arrays.stream(player.getEquipment().getArmorContents()).anyMatch(armourItemStack -> armourItemStack != null && armourItemStack.getType() != Material.AIR)) {
            UtilMessage.message(player, this.getModule().getName(), "You cannot have armour on when equipping!");
            return;
        }

        UtilItem.remove(player, itemStack, 1);

        player.getEquipment().setHelmet(itemStack);
    }
}