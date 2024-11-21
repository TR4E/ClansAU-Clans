package me.trae.clans.clan.events;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.ClanEvent;

public class ClanUpdaterEvent extends ClanEvent {

    public ClanUpdaterEvent(final Clan clan) {
        super(clan);
    }
}