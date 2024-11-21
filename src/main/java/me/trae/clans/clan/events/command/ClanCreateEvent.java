package me.trae.clans.clan.events.command;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import org.bukkit.entity.Player;

public class ClanCreateEvent extends ClanPlayerCancellableEvent {

    public ClanCreateEvent(final Clan clan, final Player player, final Client client) {
        super(clan, player, client);
    }
}