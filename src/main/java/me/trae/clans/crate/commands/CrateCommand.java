package me.trae.clans.crate.commands;

import me.trae.clans.Clans;
import me.trae.clans.crate.Crate;
import me.trae.clans.crate.CrateManager;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.AnyCommandType;
import me.trae.core.utility.UtilCommand;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.constants.CoreArgumentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CrateCommand extends Command<Clans, CrateManager> implements AnyCommandType {

    public CrateCommand(final CrateManager manager) {
        super(manager, "crate", new String[]{"crates"}, Rank.ADMIN);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new GiveCommand(this));
    }

    @Override
    public String getDescription() {
        return "Crate management";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        UtilCommand.processSubCommand(this, sender, args);
    }

    private static class GiveCommand extends SubCommand<Clans, CrateCommand> implements AnyCommandType {

        public GiveCommand(final CrateCommand module) {
            super(module, "give");
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <crate>";
        }

        @Override
        public String getDescription() {
            return "Give a Crate";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Crate", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Crate", "You did not input a Crate.");
                return;
            }

            final Crate crate = this.getModule().getManager().searchCrate(sender, args[1], true);
            if (crate == null) {
                return;
            }

            crate.give(targetPlayer);

            if (sender == targetPlayer) {
                UtilMessage.simpleMessage(sender, "Crate", "You gave yourself a <var>.", Collections.singletonList(crate.getDisplayName()));
            } else {
                UtilMessage.simpleMessage(sender, "Crate", "You gave <yellow><var></yellow> a <var>.", Arrays.asList(targetPlayer.getName(), crate.getDisplayName()));

                UtilMessage.simpleMessage(targetPlayer, "Crate", "<yellow><var></yellow> gave you a <var>.", Arrays.asList(sender.getName(), crate.getDisplayName()));
            }
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            if (args.length == 2) {
                return CoreArgumentType.CUSTOM.apply(this.getModule().getManager().getModulesByClass(Crate.class).stream().map(Crate::getName).collect(Collectors.toList()), args[1]);
            }

            return Collections.emptyList();
        }
    }
}