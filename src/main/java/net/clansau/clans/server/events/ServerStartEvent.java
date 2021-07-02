package net.clansau.clans.server.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public ServerStartEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public final HandlerList getHandlers() {
        return handlers;
    }
}