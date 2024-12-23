package me.trae.clans.tip.tips;

import me.trae.clans.Clans;
import me.trae.clans.tip.TipManager;
import me.trae.core.tip.abstracts.Tip;
import org.bukkit.entity.Player;

public class DailyQuestTip extends Tip<Clans, TipManager> {

    public DailyQuestTip(final TipManager manager) {
        super(manager);
    }

    @Override
    public String getText(final Player player) {
        return "Don't forget to complete your daily quests! (<green>/quests</green>)";
    }
}