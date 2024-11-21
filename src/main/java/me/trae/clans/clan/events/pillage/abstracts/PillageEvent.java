package me.trae.clans.clan.events.pillage.abstracts;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.events.abstracts.ClanEvent;
import me.trae.clans.clan.events.pillage.abstracts.interfaces.IPillageEvent;

public class PillageEvent extends ClanEvent implements IPillageEvent {

    private final Clan target;
    private final Pillage pillage;

    public PillageEvent(final Clan clan, final Clan target) {
        super(clan);

        this.target = target;
        this.pillage = clan.getPillageByClan(target);
    }

    @Override
    public Clan getTarget() {
        return this.target;
    }

    @Override
    public Pillage getPillage() {
        return this.pillage;
    }
}