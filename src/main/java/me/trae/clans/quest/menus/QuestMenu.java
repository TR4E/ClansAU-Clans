package me.trae.clans.quest.menus;

import me.trae.clans.Clans;
import me.trae.clans.quest.Quest;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.quest.data.QuestData;
import me.trae.core.menu.EmptyButton;
import me.trae.core.menu.Menu;
import me.trae.core.utility.UtilColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuestMenu extends Menu<Clans, QuestManager> {

    public QuestMenu(final QuestManager manager, final Player player) {
        super(manager, player, 36, "Quest Menu");
    }

    @Override
    public void fillPage(final Player player) {
        int slot = 0;
        int category = -1;

        for (final Quest quest : this.getManager().getModulesByClass(Quest.class)) {
            if (quest.getCategoryItemStack() == null) {
                continue;
            }

            if (quest.getCategoryID() != category) {
                category = quest.getCategoryID();

                if (slot % 9 != 0) {
                    slot += 9 - (slot % 9);
                }

                this.addButton(new EmptyButton<QuestMenu>(this, slot, quest.getCategoryItemStack()) {
                    @Override
                    public String getDisplayName() {
                        return UtilColor.bold(ChatColor.YELLOW) + quest.getCategoryName();
                    }
                });

                slot++;
            }

            this.addButton(new QuestButton(this, slot) {
                @Override
                public Quest getQuest() {
                    return quest;
                }

                @Override
                public QuestData getData() {
                    return quest.getUserByPlayer(player);
                }
            });

            slot++;
        }
    }
}