package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.types.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.event.types.ITargetEvent;
import org.bukkit.entity.Player;

public class MemberDemoteEvent extends ClanPlayerCancellableEvent implements ITargetEvent<Client> {

    private final Client target;

    public MemberDemoteEvent(final Clan clan, final Player player, final Client client, final Client target) {
        super(clan, player, client);

        this.target = target;
    }

    @Override
    public Client getTarget() {
        return this.target;
    }
}