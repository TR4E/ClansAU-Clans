package net.clansau.clans.server.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerStopEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public ServerStopEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public final HandlerList getHandlers() {
        return handlers;
    }
}