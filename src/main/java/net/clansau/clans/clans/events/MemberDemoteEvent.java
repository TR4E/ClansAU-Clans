package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.client.Client;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Player;

public class MemberDemoteEvent extends CustomCancellableEvent {

    private final Player player;
    private final Client client, target;
    private final Clan clan;

    public MemberDemoteEvent(final Player player, final Client client, final Client target, final Clan clan) {
        this.player = player;
        this.client = client;
        this.target = target;
        this.clan = clan;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Client getClient() {
        return this.client;
    }

    public final Client getTarget() {
        return this.target;
    }

    public final Clan getClan() {
        return this.clan;
    }

    public final ClanRole getClanRole() {
        return this.getClan().getClanRole(this.getTarget().getUUID());
    }
}