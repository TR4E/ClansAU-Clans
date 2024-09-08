package me.trae.clans.clan.events.abstracts;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.interfaces.IClanEvent;
import me.trae.core.event.CustomCancellableEvent;

public class ClanCancellableEvent extends CustomCancellableEvent implements IClanEvent {

    private final Clan clan;

    public ClanCancellableEvent(final Clan clan) {
        this.clan = clan;
    }

    @Override
    public Clan getClan() {
        return this.clan;
    }
}