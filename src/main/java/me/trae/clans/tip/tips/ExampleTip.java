package me.trae.clans.tip.tips;

import me.trae.clans.Clans;
import me.trae.clans.tip.TipManager;
import me.trae.core.tip.abstracts.Tip;
import org.bukkit.entity.Player;

public class ExampleTip extends Tip<Clans, TipManager> {

    public ExampleTip(final TipManager manager) {
        super(manager);
    }

    @Override
    public String getText(final Player player) {
        return "This is an Example Tip!";
    }

    @Override
    public boolean canReceive(final Player player) {
        return super.canReceive(player);
    }
}