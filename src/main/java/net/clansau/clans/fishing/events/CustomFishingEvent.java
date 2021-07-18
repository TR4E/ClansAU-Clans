package net.clansau.clans.fishing.events;

import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CustomFishingEvent extends CustomCancellableEvent {

    private final Player player;
    private final Entity caught;

    public CustomFishingEvent(final Player player, final Entity caught) {
        this.player = player;
        this.caught = caught;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Entity getCaught() {
        return this.caught;
    }
}