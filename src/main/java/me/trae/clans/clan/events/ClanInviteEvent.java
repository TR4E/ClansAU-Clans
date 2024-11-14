package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.types.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.event.types.ITargetEvent;
import org.bukkit.entity.Player;

public class ClanInviteEvent extends ClanPlayerCancellableEvent implements ITargetEvent<Player> {

    private final Player target;

    public ClanInviteEvent(final Clan clan, final Player player, final Client client, final Player target) {
        super(clan, player, client);

        this.target = target;
    }

    @Override
    public Player getTarget() {
        return this.target;
    }
}