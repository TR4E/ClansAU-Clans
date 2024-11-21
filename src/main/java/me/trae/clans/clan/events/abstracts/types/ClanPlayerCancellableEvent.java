package me.trae.clans.clan.events.abstracts.types;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.ClanCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.client.events.abstracts.interfaces.IClientEvent;
import me.trae.core.event.types.IPlayerEvent;
import org.bukkit.entity.Player;

public class ClanPlayerCancellableEvent extends ClanCancellableEvent implements IPlayerEvent, IClientEvent {

    private final Player player;
    private final Client client;

    public ClanPlayerCancellableEvent(final Clan clan, final Player player, final Client client) {
        super(clan);

        this.player = player;
        this.client = client;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Client getClient() {
        return this.client;
    }
}