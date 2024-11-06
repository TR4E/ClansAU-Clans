package me.trae.clans.clan.modules;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HandleClansPlayerDisplayNameFormat extends SpigotListener<Clans, ClanManager> {

    public HandleClansPlayerDisplayNameFormat(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onPlayerDisplayName(final PlayerDisplayNameEvent event) {
        final Player player = event.getPlayer();
        final Player target = event.getTarget();

        final ClanRelation clanRelation = this.getManager().getClanRelationByPlayer(target, player);

        event.setPlayerName(clanRelation + player.getName());
    }
}