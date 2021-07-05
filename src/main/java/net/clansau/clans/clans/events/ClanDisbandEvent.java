package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanDisbandEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Clan clan;
    private final Cause cause;
    private boolean cancelled;

    public ClanDisbandEvent(final Player player, final Clan clan, final Cause cause) {
        this.player = player;
        this.clan = clan;
        this.cause = cause;
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

    public final Clan getClan() {
        return this.clan;
    }

    public final Cause getCause() {
        return this.cause;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public enum Cause {
        PLAYER, ENERGY, FORCE
    }
}