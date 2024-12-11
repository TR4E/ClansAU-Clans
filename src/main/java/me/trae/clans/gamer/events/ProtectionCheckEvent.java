package me.trae.clans.gamer.events;

import me.trae.clans.gamer.Gamer;
import me.trae.core.gamer.events.GamerCancellableEvent;

public class ProtectionCheckEvent extends GamerCancellableEvent<Gamer> {

    public ProtectionCheckEvent(final Gamer gamer) {
        super(gamer);
    }
}