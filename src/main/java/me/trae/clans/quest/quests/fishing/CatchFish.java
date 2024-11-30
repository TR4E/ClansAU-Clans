package me.trae.clans.quest.quests.fishing;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.quest.Quest;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.quest.models.types.FishingQuest;
import org.bukkit.entity.Player;

public class CatchFish extends Quest implements FishingQuest {

    public CatchFish(final QuestManager manager) {
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
    public boolean canCatch(final Player player) {
        final Clan territoryClan = this.getInstance(Clans.class).getManagerByClass(ClanManager.class).getClanByLocation(player.getLocation());
        if (territoryClan == null || !(territoryClan.getName().equals("Fields"))) {
            return false;
        }

        return true;
    }
}