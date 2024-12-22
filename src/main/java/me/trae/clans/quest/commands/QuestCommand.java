package me.trae.clans.quest.commands;

import me.trae.clans.Clans;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.quest.menus.QuestMenu;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilCommand;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class QuestCommand extends Command<Clans, QuestManager> implements PlayerCommandType {

    public QuestCommand(final QuestManager manager) {
        super(manager, "quest", new String[]{"quests", "daily", "dailies", "dailyquests"}, Rank.DEFAULT);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new ResetCommand(this));
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        if (args.length == 0) {
            UtilMenu.open(new QuestMenu(this.getManager(), player));
            return;
        }

        UtilCommand.processSubCommand(this, player, args);
    }

    private static class ResetCommand extends SubCommand<Clans, QuestCommand> implements PlayerCommandType {

        public ResetCommand(final QuestCommand module) {
            super(module, "reset", Rank.ADMIN);
        }

        @Override
        public String getDescription() {
            return "Reset all Quests for Gamers";
        }

        @Override
        public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
            this.getModule().getManager().resetQuests();

            UtilMessage.message(player, "Quest", "You have reset all quests for everyone!");
        }
    }
}