package me.trae.clans.quest.models;

import me.trae.clans.quest.interfaces.IQuest;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilString;
import org.bukkit.inventory.ItemStack;

public interface ItemQuestReward extends IQuest {

    ItemStack getItemStack();

    @Override
    default String getRewardString() {
        return UtilString.format("<yellow>%sx <gray>of %s", this.getItemStack().getAmount(), UtilItem.getDisplayName(this.getItemStack(), false));
    }
}