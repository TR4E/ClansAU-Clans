package me.trae.clans.clan.modules.territory.interaction;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.AccessType;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class HandleClanTerritoryBlockBreak extends SpigotListener<Clans, ClanManager> {

    public HandleClanTerritoryBlockBreak(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
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

        player.updateInventory();

        if (this.isInform(block)) {
            UtilMessage.simpleMessage(player, "Clans", "You cannot break <green><var></green> in <var>.", Arrays.asList(UtilBlock.getDisplayName(block), this.getManager().getClanName(territoryClan, this.getManager().getClanRelationByClan(playerClan, territoryClan))));
        }
    }

    private boolean isInform(final Block block) {
        if (block.getType() == Material.FIRE) {
            return false;
        }

        return true;
    }
}