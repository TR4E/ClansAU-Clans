package me.trae.clans.clan.events.pillage.abstracts.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.Pillage;
import me.trae.core.event.types.ITargetEvent;

public interface IPillageEvent extends ITargetEvent<Clan> {

    Pillage getPillage();
}