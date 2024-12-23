package me.trae.clans.tip.tips;

import me.trae.clans.Clans;
import me.trae.clans.farming.FarmingManager;
import me.trae.clans.farming.modules.HandleFarmingZones;
import me.trae.clans.tip.TipManager;
import me.trae.core.tip.abstracts.Tip;
import org.bukkit.entity.Player;

public class FarmingTip extends Tip<Clans, TipManager> {

    public FarmingTip(final TipManager manager) {
        super(manager);
    }

    @Override
    public String getText(final Player player) {
        final HandleFarmingZones module = this.getInstance().getManagerByClass(FarmingManager.class).getModuleByClass(HandleFarmingZones.class);

        return String.format("The farming levels are between <yellow>%s</yellow> and <yellow>%s</yellow> Y.", module.minY, module.maxY);
    }
}