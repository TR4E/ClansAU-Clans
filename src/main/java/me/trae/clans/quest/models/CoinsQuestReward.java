package me.trae.clans.quest.models;

import me.trae.clans.quest.interfaces.IQuest;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;

public interface CoinsQuestReward extends IQuest {

    int getCoins();

    @Override
    default String getRewardString() {
        return ChatColor.GOLD + UtilString.toDollar(this.getCoins());
    }
}