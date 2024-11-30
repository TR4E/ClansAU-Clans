package me.trae.clans.quest.interfaces;

import me.trae.clans.quest.data.QuestData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public interface IQuest {

    Map<UUID, QuestData> getUsers();

    void addUser(final QuestData questData);

    void removeUser(final Player player);

    QuestData getUserByPlayer(final Player player);

    boolean isUserByPlayer(final Player player);

    String getDisplayName();

    String[] getDescription();

    int getMaxProgress();

    void addProgress(final Player player);

    default String getRewardString() {
        return "";
    }

    void reward(final Player player);

    default int getCategoryID() {
        return 0;
    }

    default String getCategoryName() {
        return null;
    }

    default ItemStack getCategoryItemStack() {
        return null;
    }
}