package me.trae.clans.clan.events.pillage;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.pillage.abstracts.PillageEvent;

public class PillageUpdaterEvent extends PillageEvent {

    public PillageUpdaterEvent(final Clan clan, final Clan target) {
        super(clan, target);
    }
}