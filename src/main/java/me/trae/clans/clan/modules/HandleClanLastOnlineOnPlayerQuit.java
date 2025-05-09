package me.trae.clans.clan.modules;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandleClanLastOnlineOnPlayerQuit extends SpigotListener<Clans, ClanManager> {

    public HandleClanLastOnlineOnPlayerQuit(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final Clan clan = this.getManager().getClanByPlayer(player);
        if (clan == null) {
            return;
        }

        if (clan.getOnlineMembers().size() > 1) {
            return;
        }

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        clan.setLastOnline(System.currentTimeMillis());
        this.getManager().getRepository().updateData(clan, ClanProperty.LAST_ONLINE);
    }
}