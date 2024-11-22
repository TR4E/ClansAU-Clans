package me.trae.clans.clan.modules.tnt;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisablePlacingTntWhileSOTW extends SpigotListener<Clans, ClanManager> {

    public DisablePlacingTntWhileSOTW(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(this.getManager().sotw)) {
            return;
        }

        final Block block = event.getBlock();

        if (block.getType() != Material.TNT) {
            return;
        }

        final Player player = event.getPlayer();

        if (this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(player, "Clans", "<gold>You cannot place TNT on Start Of The World!");
    }
}