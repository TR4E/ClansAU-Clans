package me.trae.clans.quest.modules;

import me.trae.clans.Clans;
import me.trae.clans.quest.Quest;
import me.trae.clans.quest.QuestManager;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class HandleQuestUpdater extends SpigotUpdater<Clans, QuestManager> {

    public HandleQuestUpdater(final QuestManager manager) {
        super(manager);
    }

    @Update(delay = 86_400_000L)
    public void onUpdater() {
        final Set<Player> players = new HashSet<>();

        for (final Quest quest : this.getManager().getModulesByClass(Quest.class)) {
            quest.getUsers().values().removeIf(data -> {
                final Player player = Bukkit.getPlayer(data.getUUID());
                if (player != null) {
                    players.add(player);
                }

                this.getManager().getRepository().deleteData(data);

                return true;
            });
        }

        for (final Player player : players) {
            UtilMessage.message(player, "Quest", "Your daily quests have been reset.");
        }
    }
}