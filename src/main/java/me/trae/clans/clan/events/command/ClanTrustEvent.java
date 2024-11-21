package me.trae.clans.clan.events.command;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.event.types.ITargetEvent;
import org.bukkit.entity.Player;

public class ClanTrustEvent extends ClanPlayerCancellableEvent implements ITargetEvent<Clan> {

    private final Clan target;

    public ClanTrustEvent(final Clan clan, final Player player, final Client client, final Clan target) {
        super(clan, player, client);

        this.target = target;
    }

    @Override
    public Clan getTarget() {
        return this.target;
    }
}