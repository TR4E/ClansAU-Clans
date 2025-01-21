package me.trae.clans.clan.modules.chat;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleClansPlayerDisplayNameFormat extends SpigotListener<Clans, ClanManager> {

    public HandleClansPlayerDisplayNameFormat(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDisplayName(final PlayerDisplayNameEvent event) {
        final OfflinePlayer player = event.getOfflinePlayer();
        final Player target = event.getTarget();

        final Clan playerClan = this.getManager().getClanByUUID(player.getUniqueId());
        final Clan targetClan = this.getManager().getClanByPlayer(target);

        final ClanRelation clanRelation = this.getManager().getClanRelationByClan(targetClan, playerClan);

        event.setPlayerName(clanRelation.getSuffix() + player.getName());
    }
}