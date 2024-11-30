package me.trae.clans.quest.quests.mining;

import me.trae.clans.quest.Quest;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.quest.models.types.MiningQuest;
import org.bukkit.Material;

public class MineEmeralds extends Quest implements MiningQuest {

    public MineEmeralds(final QuestManager manager) {
        super(manager);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "To complete this quest, you",
                String.format("have to <gold>%s</gold> in <bold>Fields</bold>.", this.getDisplayName())
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
        return Material.EMERALD_ORE;
    }
}