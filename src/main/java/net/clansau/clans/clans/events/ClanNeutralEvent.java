package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanNeutralEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Clan clan, target;
    private final boolean forced;
    private boolean cancelled;

    public ClanNeutralEvent(final Player player, final Clan clan, final Clan target, final boolean forced) {
        this.player = player;
        this.clan = clan;
        this.target = target;
        this.forced = forced;
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

    public final Clan getTarget() {
        return this.target;
    }

    public final boolean isForced() {
        return this.forced;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}