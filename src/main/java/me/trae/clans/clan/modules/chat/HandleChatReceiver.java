package me.trae.clans.clan.modules.chat;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.chat.events.ChatReceiveEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleChatReceiver extends SpigotListener<Clans, ClanManager> {

    public HandleChatReceiver(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatReceive(final ChatReceiveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        if (playerClan == null) {
            return;
        }

        final Clan targetClan = this.getManager().getClanByPlayer(event.getTarget());

        final ClanRelation clanRelation = this.getManager().getClanRelationByClan(targetClan, playerClan);

        event.setPlayerName(String.format("%s %s", clanRelation.getPrefix() + playerClan.getDisplayName(), clanRelation.getSuffix() + player.getName()));
    }
}