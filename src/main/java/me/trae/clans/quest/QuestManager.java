package me.trae.clans.quest;

import me.trae.clans.Clans;
import me.trae.clans.quest.commands.QuestCommand;
import me.trae.clans.quest.modules.HandleQuestUpdater;
import me.trae.clans.quest.modules.data.HandleLoadQuestDataOnPlayerJoin;
import me.trae.clans.quest.modules.data.HandleRemoveQuestDataOnPlayerQuit;
import me.trae.clans.quest.quests.fishing.CatchFish;
import me.trae.clans.quest.quests.kill.*;
import me.trae.clans.quest.quests.mining.MineDiamonds;
import me.trae.clans.quest.quests.mining.MineEmeralds;
import me.trae.clans.quest.quests.mining.MineGold;
import me.trae.clans.quest.quests.mining.MineIron;
import me.trae.core.database.repository.containers.RepositoryContainer;
import me.trae.core.framework.SpigotManager;
import me.trae.core.utility.UtilPlugin;

public class QuestManager extends SpigotManager<Clans> implements RepositoryContainer<QuestRepository> {

    public QuestManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new QuestCommand(this));

        // Modules
        addModule(new HandleLoadQuestDataOnPlayerJoin(this));
        addModule(new HandleRemoveQuestDataOnPlayerQuit(this));
        addModule(new HandleQuestUpdater(this));

        // Quests
        if (UtilPlugin.isInstanceByName("Champions")) {
            addModule(new KillAssassins(this));
            addModule(new KillBrutes(this));
            addModule(new KillKnights(this));
            addModule(new KillMages(this));
            addModule(new KillRangers(this));
        }

        addModule(new MineDiamonds(this));
        addModule(new MineEmeralds(this));
        addModule(new MineGold(this));
        addModule(new MineIron(this));

        addModule(new CatchFish(this));
    }

    @Override
    public Class<QuestRepository> getClassOfRepository() {
        return QuestRepository.class;
    }
}