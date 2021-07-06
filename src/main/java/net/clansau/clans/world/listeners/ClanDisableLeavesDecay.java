package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;

public class ClanDisableLeavesDecay extends CoreListener<WorldManager> {

    public ClanDisableLeavesDecay(final WorldManager manager) {
        super(manager, "Clan Disable Leaves Decay");
    }

    @EventHandler
    public void onLeavesDecay(final LeavesDecayEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Block block = e.getBlock();
        if (block == null) {
            return;
        }
        final Clan clan = getManager().getClanManager().getClan(block.getLocation());
        if (clan == null) {
            return;
        }
        e.setCancelled(true);
    }
}