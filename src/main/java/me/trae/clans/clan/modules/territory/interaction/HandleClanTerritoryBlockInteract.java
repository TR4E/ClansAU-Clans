package me.trae.clans.clan.modules.territory.interaction;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.AccessType;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.enums.ActionType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

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

        final Block block = event.getClickedBlock();

        final ActionType actionType = ActionType.getByAction(event.getAction());
        if (actionType == null) {
            return;
        }

        if (!(this.isValid(block, actionType))) {
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

        player.updateInventory();

        if (this.isInform(block)) {
            UtilMessage.simpleMessage(player, "Clans", "You cannot use <green><var></green> in <var>.", Arrays.asList(UtilBlock.getDisplayName(block), this.getManager().getClanName(territoryClan, this.getManager().getClanRelationByClan(playerClan, territoryClan))));
        }
    }

    private boolean isValid(final Block block, final ActionType actionType) {
        if (UtilBlock.isFence(block.getType())) {
            return true;
        }

        if (actionType == ActionType.RIGHT_CLICK) {
            if (UtilBlock.isContainer(block.getType())) {
                return true;
            }
        }

        return UtilBlock.isUsableByActionType(block.getType(), actionType);
    }

    private boolean isInform(final Block block) {
        return !(UtilBlock.isFence(block.getType()));
    }
}