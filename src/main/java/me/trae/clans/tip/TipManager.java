package me.trae.clans.tip;

import me.trae.clans.Clans;
import me.trae.clans.tip.tips.DailyQuestTip;
import me.trae.clans.tip.tips.FarmingTip;
import me.trae.clans.tip.tips.LocationsTip;
import me.trae.clans.tip.tips.VotingTip;
import me.trae.core.tip.abstracts.AbstractTipManager;

public class TipManager extends AbstractTipManager<Clans> {

    public TipManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Tips
        addModule(new DailyQuestTip(this));
        addModule(new FarmingTip(this));
        addModule(new LocationsTip(this));
        addModule(new VotingTip(this));
    }
}