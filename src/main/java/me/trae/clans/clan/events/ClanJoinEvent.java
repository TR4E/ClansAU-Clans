package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.types.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import org.bukkit.entity.Player;

public class ClanJoinEvent extends ClanPlayerCancellableEvent {

    public ClanJoinEvent(final Clan clan, final Player player, final Client client) {
        super(clan, player, client);
    }
}