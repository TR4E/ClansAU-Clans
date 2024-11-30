package me.trae.clans.quest.modules.data;

import me.trae.clans.Clans;
import me.trae.clans.quest.QuestManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class HandleLoadQuestDataOnPlayerJoin extends SpigotListener<Clans, QuestManager> {

    public HandleLoadQuestDataOnPlayerJoin(final QuestManager manager) {
        super(manager);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.getManager().getRepository().loadData(event.getPlayer().getUniqueId());
    }
}