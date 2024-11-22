package me.trae.clans.clan.modules.territory.interaction;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.AccessType;
import me.trae.clans.world.events.DoorToggleEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleClanTerritoryDoorInteract extends SpigotListener<Clans, ClanManager> {

    public HandleClanTerritoryDoorInteract(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDoorToggle(final DoorToggleEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();
        final Player player = event.getPlayer();

        if (this.getManager().hasAccess(player, this.getManager().getClanByPlayer(player), this.getManager().getClanByLocation(block.getLocation()), AccessType.DOOR_INTERACT)) {
            return;
        }

        event.setCancelled(true);
    }
}