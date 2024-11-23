package me.trae.clans.clan.modules.territory.interaction;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.AccessType;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HandleClanTerritoryBlockPlace extends SpigotListener<Clans, ClanManager> {

    public HandleClanTerritoryBlockPlace(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        final Clan territoryClan = this.getManager().getClanByLocation(block.getLocation());

        if (this.getManager().hasAccess(player, playerClan, territoryClan, AccessType.BLOCK_INTERACT)) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(player, "Clans", "You cannot place <green><var></green> in <var>.", Arrays.asList(UtilItem.getDisplayName(new ItemStack(block.getType())), this.getManager().getClanName(territoryClan, this.getManager().getClanRelationByClan(playerClan, territoryClan))));
    }
}