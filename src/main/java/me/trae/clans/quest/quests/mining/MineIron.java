package me.trae.clans.quest.quests.mining;

import me.trae.clans.quest.Quest;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.quest.models.types.MiningQuest;
import me.trae.core.utility.UtilString;
import org.bukkit.Material;

public class MineIron extends Quest implements MiningQuest {

    public MineIron(final QuestManager manager) {
        super(manager);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "To complete this quest, you",
                UtilString.format("have to <gold>%s</gold> in <bold>Fields</bold>.", this.getMaxProgress())
        };
    }

    @Override
    public int getMaxProgress() {
        return 20;
    }

    @Override
    public int getCoins() {
        return 5000;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_ORE;
    }
}