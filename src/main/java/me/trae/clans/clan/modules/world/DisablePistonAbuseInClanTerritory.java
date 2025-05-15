package me.trae.clans.clan.modules.world;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class DisablePistonAbuseInClanTerritory extends SpigotListener<Clans, ClanManager> {

    public DisablePistonAbuseInClanTerritory(final ClanManager manager) {
        super(manager);
    }


    private void handleEvent(final BlockPistonEvent event, final List<Block> blocks) {
        final Clan pistonTerritoryClan = this.getManager().getClanByLocation(event.getBlock().getLocation());

        for (final Block block : blocks) {
            final Clan blockTerritoryClan = this.getManager().getClanByLocation(block.getLocation());

            if (blockTerritoryClan == null) {
                continue;
            }

            if (pistonTerritoryClan != null && pistonTerritoryClan.equals(blockTerritoryClan)) {
                continue;
            }

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        this.handleEvent(event, event.getBlocks());
    }

    @EventHandler
    public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        this.handleEvent(event, event.getBlocks());
    }
}