package me.trae.clans.gamer.events;

import me.trae.clans.gamer.Gamer;
import me.trae.core.gamer.events.GamerCancellableEvent;

public class ProtectionRemoveEvent extends GamerCancellableEvent<Gamer> {

    public ProtectionRemoveEvent(final Gamer gamer) {
        super(gamer);
    }
}