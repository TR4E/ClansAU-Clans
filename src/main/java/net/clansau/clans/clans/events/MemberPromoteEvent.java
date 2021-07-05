package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.client.Client;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MemberPromoteEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Client client, target;
    private final Clan clan;
    private boolean cancelled;

    public MemberPromoteEvent(final Player player, final Client client, final Client target, final Clan clan) {
        this.player = player;
        this.client = client;
        this.target = target;
        this.clan = clan;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public final HandlerList getHandlers() {
        return handlers;
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

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}