package me.trae.clans.tip.tips;

import me.trae.clans.Clans;
import me.trae.clans.tip.TipManager;
import me.trae.core.tip.abstracts.Tip;
import org.bukkit.entity.Player;

public class VotingTip extends Tip<Clans, TipManager> {

    public VotingTip(final TipManager manager) {
        super(manager);
    }

    @Override
    public String getText(final Player player) {
        return "Vote daily to receive a <yellow>Voting Crate</yellow>! (<green>/vote</green>)";
    }
}