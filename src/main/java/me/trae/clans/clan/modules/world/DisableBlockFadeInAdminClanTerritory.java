package me.trae.clans.clan.modules.world;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFadeEvent;

public class DisableBlockFadeInAdminClanTerritory extends SpigotListener<Clans, ClanManager> {

    public DisableBlockFadeInAdminClanTerritory(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockFade(final BlockFadeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();

        if (block.getType() != Material.GRASS) {
            return;
        }

        if (event.getNewState().getBlock().getType() != Material.DIRT) {
            return;
        }

        final Clan territoryClan = this.getManager().getClanByLocation(block.getLocation());
        if (territoryClan == null || !(territoryClan.isAdmin())) {
            return;
        }

        event.setCancelled(true);
    }
}