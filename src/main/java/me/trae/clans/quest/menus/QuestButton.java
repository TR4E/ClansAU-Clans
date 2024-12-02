package me.trae.clans.quest.menus;

import me.trae.clans.quest.Quest;
import me.trae.clans.quest.data.QuestData;
import me.trae.clans.quest.menus.interfaces.IQuestButton;
import me.trae.core.menu.EmptyButton;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class QuestButton extends EmptyButton<QuestMenu> implements IQuestButton {

    public QuestButton(final QuestMenu menu, final int slot) {
        super(menu, slot, new ItemStack(Material.WOOL));

        final Quest quest = this.getQuest();
        final QuestData data = this.getData();

        if (data != null) {
            if (data.getProgress() == quest.getMaxProgress()) {
                this.getBuilder().getItemStack().setDurability((short) 5);
            } else {
                this.getBuilder().getItemStack().setDurability((short) 14);
            }
        } else {
            this.getBuilder().getItemStack().setDurability((short) 4);
        }
    }

    @Override
    public String getDisplayName() {
        ChatColor chatColor = ChatColor.YELLOW;

        final QuestData data = this.getData();
        if (data != null) {
            if (data.getProgress() == this.getQuest().getMaxProgress()) {
                chatColor = ChatColor.GREEN;
            } else {
                chatColor = ChatColor.RED;
            }
        }

        return chatColor + this.getQuest().getDisplayName();
    }

    @Override
    public String[] getLore() {
        final List<String> list = new ArrayList<>(Arrays.asList(this.getQuest().getDescription()));

        list.add(" ");

        list.add(UtilString.pair("<gray>Reward", this.getQuest().getRewardString()));

        final String progress = UtilJava.get(this.getData(), data -> {
            if (data != null) {
                if (data.getProgress() == this.getQuest().getMaxProgress()) {
                    return "<green>Completed";
                }

                return String.format("<red>%s/%s", data.getProgress(), this.getQuest().getMaxProgress());
            }

            return "<yellow>0";
        });

        list.add(UtilString.pair("<gray>Progress", progress));

        return list.toArray(new String[0]);
    }
}