package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Clan clan;
    private boolean cancelled;

    public ClanCreateEvent(final Player player, final Clan clan) {
        this.player = player;
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

    public final Clan getClan() {
        return this.clan;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}