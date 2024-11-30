package me.trae.clans.quest.modules.data;

import me.trae.clans.Clans;
import me.trae.clans.quest.Quest;
import me.trae.clans.quest.QuestManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandleRemoveQuestDataOnPlayerQuit extends SpigotListener<Clans, QuestManager> {

    public HandleRemoveQuestDataOnPlayerQuit(final QuestManager manager) {
        super(manager);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.getManager().getModulesByClass(Quest.class).forEach(quest -> quest.removeUser(event.getPlayer()));
    }
}