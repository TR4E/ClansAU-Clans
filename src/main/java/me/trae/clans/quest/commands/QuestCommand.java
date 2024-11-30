package me.trae.clans.quest.commands;

import me.trae.clans.Clans;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.quest.menu.QuestMenu;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMenu;
import org.bukkit.entity.Player;

public class QuestCommand extends Command<Clans, QuestManager> implements PlayerCommandType {

    public QuestCommand(final QuestManager manager) {
        super(manager, "quest", new String[]{"quests", "daily", "dailyquests"}, Rank.DEFAULT);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        UtilMenu.open(new QuestMenu(this.getManager(), player));
    }
}