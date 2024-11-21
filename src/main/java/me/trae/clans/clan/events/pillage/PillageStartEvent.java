package me.trae.clans.clan.events.pillage;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.pillage.abstracts.PillageCancellableEvent;

public class PillageStartEvent extends PillageCancellableEvent {

    public PillageStartEvent(final Clan clan, final Clan target) {
        super(clan, target);
    }
}