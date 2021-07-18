package net.clansau.clans.fishing.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.fishing.FishingManager;
import net.clansau.clans.fishing.events.CustomFishingEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class OnlyFishAtLake extends CoreListener<FishingManager> {

    public OnlyFishAtLake(final FishingManager manager) {
        super(manager, "Only Fish At Lake");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomFishing(final CustomFishingEvent e) {
        final Entity caught = e.getCaught();
        if (caught == null) {
            return;
        }
        final Clan land = getInstance().getManager(ClanManager.class).getClan(caught.getLocation());
        if (land == null) {
            return;
        }
        if (!(land instanceof AdminClan) && !(land.getName().equalsIgnoreCase("Lake") || land.getName().equalsIgnoreCase("Fields"))) {
            return;
        }
        final Player player = e.getPlayer();
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        e.setCancelled(true);
        UtilMessage.message(player, "Fishing", "You can only fish at " + ChatColor.WHITE + "Lake" + ChatColor.GRAY + ".");
    }
}