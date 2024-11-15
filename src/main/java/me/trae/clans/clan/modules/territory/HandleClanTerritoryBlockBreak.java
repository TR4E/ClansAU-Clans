package me.trae.clans.clan.modules.territory;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.AccessType;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
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

        UtilMessage.simpleMessage(player, "Clans", "You cannot break <green><var></green> in <var>.", Arrays.asList(UtilString.clean(block.getType().name()), this.getManager().getClanFullName(territoryClan, this.getManager().getClanRelationByClan(playerClan, territoryClan))));
    }
}