package me.trae.clans.gamer.commands;

import me.trae.clans.Clans;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.AnyCommandType;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.utility.*;
import me.trae.core.utility.constants.CoreArgumentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProtectionCommand extends Command<Clans, GamerManager> implements AnyCommandType {

    public ProtectionCommand(final GamerManager manager) {
        super(manager, "protection", new String[]{"pvptimer", "prot"}, Rank.DEFAULT);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new GiveCommand(this));
        addSubModule(new TakeCommand(this));
        addSubModule(new ResetCommand(this));
        addSubModule(new RemoveCommand(this));
    }

    @Override
    public String getDescription() {
        return "Protection management";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (UtilCommand.isValidSender(sender, Player.class, false)) {
            if (args.length == 0) {
                final Player player = UtilJava.cast(Player.class, sender);

                final Gamer playerGamer = this.getManager().getGamerByPlayer(player);
                if (playerGamer == null) {
                    return;
                }

                if (!(playerGamer.hasProtection())) {
                    UtilMessage.message(player, "Protection", "You do not have protection.");
                    return;
                }

                UtilMessage.simpleMessage(player, "Protection", "You have <green><var></green> left of protection.", Collections.singletonList(UtilTime.getTime(playerGamer.getProtection())));
                return;
            }
        }

        UtilCommand.processSubCommand(this, sender, args);
    }

    private static class GiveCommand extends SubCommand<Clans, ProtectionCommand> implements AnyCommandType {

        public GiveCommand(final ProtectionCommand module) {
            super(module, "give", Rank.ADMIN);
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <time>";
        }

        @Override
        public String getDescription() {
            return "Give Protection to a Player";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Protection", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Protection", "You did not input a Time.");
                return;
            }

            final Optional<Long> durationOptional = UtilTime.getDurationByString(args[1]);
            if (!(durationOptional.isPresent()) || durationOptional.get() <= 0L) {
                UtilMessage.message(sender, "Protection", "You did not input a valid Time.");
                return;
            }

            final long duration = durationOptional.get();

            final Gamer targetGamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (targetGamer == null) {
                return;
            }

            targetGamer.addProtection(duration);
            this.getModule().getManager().getRepository().updateData(targetGamer, GamerProperty.PROTECTION);

            final String durationString = UtilTime.getTime(duration);

            UtilMessage.simpleMessage(sender, "Protection", "You gave <green><var></green> to <yellow><var></yellow>.", Arrays.asList(durationString, targetPlayer.getName()));
            UtilMessage.simpleMessage(targetPlayer, "Protection", "<yellow><var></yellow> gave you <green><var></green>.", Arrays.asList(sender.getName(), durationString));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class TakeCommand extends SubCommand<Clans, ProtectionCommand> implements AnyCommandType {

        public TakeCommand(final ProtectionCommand module) {
            super(module, "take", Rank.ADMIN);
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <time>";
        }

        @Override
        public String getDescription() {
            return "Take Protection from a Player";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Protection", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Protection", "You did not input a Time.");
                return;
            }

            final Optional<Long> durationOptional = UtilTime.getDurationByString(args[1]);
            if (!(durationOptional.isPresent()) || durationOptional.get() <= 0L) {
                UtilMessage.message(sender, "Protection", "You did not input a valid Time.");
                return;
            }

            final long duration = durationOptional.get();

            final Gamer targetGamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (targetGamer == null) {
                return;
            }

            targetGamer.takeProtection(duration);
            this.getModule().getManager().getRepository().updateData(targetGamer, GamerProperty.PROTECTION);

            if (targetGamer.hasProtection()) {
                final String durationString = UtilTime.getTime(duration);

                UtilMessage.simpleMessage(sender, "Protection", "You gave <green><var></green> to <yellow><var></yellow>.", Arrays.asList(durationString, targetPlayer.getName()));
                UtilMessage.simpleMessage(targetPlayer, "Protection", "<yellow><var></yellow> gave you <green><var></green>.", Arrays.asList(sender.getName(), durationString));
            } else {
                UtilMessage.simpleMessage(sender, "Protection", "You took away protection from <yellow><var></yellow>.", Collections.singletonList(targetPlayer.getName()));
                UtilMessage.simpleMessage(targetPlayer, "Protection", "<yellow><var></yellow> took away your protection.", Collections.singletonList(sender.getName()));
            }
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class ResetCommand extends SubCommand<Clans, ProtectionCommand> implements AnyCommandType {

        public ResetCommand(final ProtectionCommand module) {
            super(module, "reset", Rank.ADMIN);
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player>";
        }

        @Override
        public String getDescription() {
            return "Reset Protection for a Player";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Protection", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            final Gamer targetGamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (targetGamer == null) {
                return;
            }

            targetGamer.setProtection(this.getModule().getManager().starterProtectionDuration);
            this.getModule().getManager().getRepository().updateData(targetGamer, GamerProperty.PROTECTION);

            UtilMessage.simpleMessage(sender, "Protection", "You reset the protection for <yellow><var></yellow>.", Collections.singletonList(targetPlayer.getName()));
            UtilMessage.simpleMessage(targetPlayer, "Protection", "<yellow><var></yellow> reset your protection.", Collections.singletonList(sender.getName()));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class RemoveCommand extends SubCommand<Clans, ProtectionCommand> implements PlayerCommandType {

        public RemoveCommand(final ProtectionCommand module) {
            super(module, "remove");
        }

        @Override
        public String getDescription() {
            return "Remove Protection";
        }

        @Override
        public void execute(final Player player, final Client client, final me.trae.core.gamer.Gamer gamer, final String[] args) {
            final Gamer playerGamer = this.getModule().getManager().getGamerByPlayer(player);
            if (playerGamer == null) {
                return;
            }

            if (!(playerGamer.hasProtection())) {
                UtilMessage.message(player, "Protection", "You do not have protection.");
                return;
            }

            playerGamer.setProtection(0L);
            this.getModule().getManager().getRepository().updateData(playerGamer, GamerProperty.PROTECTION);

            UtilMessage.message(player, "Protection", "You took away your protection!");
        }
    }
}