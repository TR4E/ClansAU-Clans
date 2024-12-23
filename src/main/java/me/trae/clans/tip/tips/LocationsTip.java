package me.trae.clans.tip.tips;

import me.trae.clans.Clans;
import me.trae.clans.tip.TipManager;
import me.trae.core.tip.abstracts.Tip;
import org.bukkit.entity.Player;

public class LocationsTip extends Tip<Clans, TipManager> {

    public LocationsTip(final TipManager manager) {
        super(manager);
    }

    @Override
    public String getText(final Player player) {
        return "Not sure where <white>Fields</white> & <white>Shops</white> are located? (<green>/coords</green>)";
    }
}