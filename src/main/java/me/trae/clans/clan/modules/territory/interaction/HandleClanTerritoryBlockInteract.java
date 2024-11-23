package me.trae.clans.clan.modules.territory.interaction;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.AccessType;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HandleClanTerritoryBlockInteract extends SpigotListener<Clans, ClanManager> {

    public HandleClanTerritoryBlockInteract(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (!(this.isValid(block))) {
            return;
        }

        final Player player = event.getPlayer();

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        final Clan territoryClan = this.getManager().getClanByLocation(block.getLocation());

        if (UtilBlock.isDoor(block.getType()) || UtilBlock.isGate(block.getType())) {
            if (this.getManager().hasAccess(player, playerClan, territoryClan, AccessType.DOOR_INTERACT)) {
                return;
            }
        }

        if (this.getManager().hasAccess(player, playerClan, territoryClan, AccessType.CONTAINER_INTERACT)) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(player, "Clans", "You cannot use <green><var></green> in <var>.", Arrays.asList(UtilItem.getDisplayName(new ItemStack(block.getType())), this.getManager().getClanName(territoryClan, this.getManager().getClanRelationByClan(playerClan, territoryClan))));
    }

    private boolean isValid(final Block block) {
        return UtilBlock.isUsable(block.getType()) || UtilBlock.isContainer(block.getType());
    }
}