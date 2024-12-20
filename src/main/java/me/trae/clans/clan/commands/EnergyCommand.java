package me.trae.clans.clan.commands;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.utility.constants.ClansArgumentType;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.AnyCommandType;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EnergyCommand extends Command<Clans, ClanManager> implements AnyCommandType {

    public EnergyCommand(final ClanManager manager) {
        super(manager, "energy", new String[0], Rank.ADMIN);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new SetCommand(this));
        addSubModule(new GiveCommand(this));
        addSubModule(new TakeCommand(this));
        addSubModule(new ResetCommand(this));
    }

    @Override
    public String getDescription() {
        return "Energy management";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        UtilCommand.processSubCommand(this, sender, args);
    }

    private static class SetCommand extends SubCommand<Clans, EnergyCommand> implements AnyCommandType {

        public SetCommand(final EnergyCommand module) {
            super(module, "set");
        }

        @Override
        public String getDescription() {
            return "Update Energy for a Clan";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <clan> <time>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a Clan.");
                return;
            }

            final Clan targetClan = this.getModule().getManager().searchClan(sender, args[0], true);
            if (targetClan == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a Time.");
                return;
            }

            final Optional<Long> durationOptional = UtilTime.getDurationByString(args[1]);
            if (!(durationOptional.isPresent())) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a valid Time.");
                return;
            }

            long duration = durationOptional.get();

            if (duration <= 0L) {
                duration = 0L;
            }

            targetClan.setEnergy(duration);
            this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ENERGY);

            targetClan.getOnlineMembers().keySet().forEach(memberPlayer -> UtilServer.callEvent(new ScoreboardUpdateEvent(memberPlayer)));

            final String durationString = UtilTime.getTime(duration);

            UtilMessage.simpleMessage(sender, "Clan Energy", "You updated the energy for <var> to <green><var></green>.", Arrays.asList(this.getModule().getManager().getClanName(targetClan, ClanRelation.NEUTRAL), durationString));
            this.getModule().getManager().messageClan(targetClan, "Clan Energy", "Your Clan Energy has been updated to <green><var></green> by <yellow><var></yellow>.", Arrays.asList(durationString, sender.getName()), Collections.singletonList(sender instanceof Player ? UtilJava.cast(Player.class, sender).getUniqueId() : null));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return ClansArgumentType.CLANS.apply(args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class GiveCommand extends SubCommand<Clans, EnergyCommand> implements AnyCommandType {

        public GiveCommand(final EnergyCommand module) {
            super(module, "give");
        }

        @Override
        public String getDescription() {
            return "Give Energy to a Clan";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <clan> <time>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a Clan.");
                return;
            }

            final Clan targetClan = this.getModule().getManager().searchClan(sender, args[0], true);
            if (targetClan == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a Time.");
                return;
            }

            final Optional<Long> durationOptional = UtilTime.getDurationByString(args[1]);
            if (!(durationOptional.isPresent())) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a valid Time.");
                return;
            }

            long duration = durationOptional.get();

            if (duration <= 0L) {
                duration = 0L;
            }

            targetClan.setEnergy(targetClan.getEnergy() + duration);
            this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ENERGY);

            targetClan.getOnlineMembers().keySet().forEach(memberPlayer -> UtilServer.callEvent(new ScoreboardUpdateEvent(memberPlayer)));

            final String durationString = UtilTime.getTime(duration);

            UtilMessage.simpleMessage(sender, "Clan Energy", "You gave <green><var></green> of energy to <var>.", Arrays.asList(durationString, this.getModule().getManager().getClanName(targetClan, ClanRelation.NEUTRAL)));
            this.getModule().getManager().messageClan(targetClan, "Clan Energy", "<yellow><var></yellow> gave <green><var></green> of energy to your clan.", Arrays.asList(sender.getName(), durationString), Collections.singletonList(sender instanceof Player ? UtilJava.cast(Player.class, sender).getUniqueId() : null));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return ClansArgumentType.CLANS.apply(args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class TakeCommand extends SubCommand<Clans, EnergyCommand> implements AnyCommandType {

        public TakeCommand(final EnergyCommand module) {
            super(module, "take");
        }

        @Override
        public String getDescription() {
            return "Take Energy from a Clan";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <clan> <time>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a Clan.");
                return;
            }

            final Clan targetClan = this.getModule().getManager().searchClan(sender, args[0], true);
            if (targetClan == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a Time.");
                return;
            }

            final Optional<Long> durationOptional = UtilTime.getDurationByString(args[1]);
            if (!(durationOptional.isPresent())) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a valid Time.");
                return;
            }

            long duration = durationOptional.get();

            if (duration <= 0L) {
                duration = 0L;
            }

            targetClan.setEnergy(targetClan.getEnergy() - duration);
            this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ENERGY);

            targetClan.getOnlineMembers().keySet().forEach(memberPlayer -> UtilServer.callEvent(new ScoreboardUpdateEvent(memberPlayer)));

            final String durationString = UtilTime.getTime(duration);

            UtilMessage.simpleMessage(sender, "Clan Energy", "You took <green><var></green> of energy from <var>.", Arrays.asList(durationString, this.getModule().getManager().getClanName(targetClan, ClanRelation.NEUTRAL)));
            this.getModule().getManager().messageClan(targetClan, "Clan Energy", "<yellow><var></yellow> took <green><var></green> of energy from your clan.", Arrays.asList(sender.getName(), durationString), Collections.singletonList(sender instanceof Player ? UtilJava.cast(Player.class, sender).getUniqueId() : null));

        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return ClansArgumentType.CLANS.apply(args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class ResetCommand extends SubCommand<Clans, EnergyCommand> implements AnyCommandType {

        public ResetCommand(final EnergyCommand module) {
            super(module, "reset");
        }

        @Override
        public String getDescription() {
            return "Reset Energy for a Clan";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <clan>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Clan Energy", "You did not input a Clan.");
                return;
            }

            final Clan targetClan = this.getModule().getManager().searchClan(sender, args[0], true);
            if (targetClan == null) {
                return;
            }

            final long duration = this.getModule().getManager().defaultEnergy;

            targetClan.setEnergy(duration);
            this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ENERGY);

            targetClan.getOnlineMembers().keySet().forEach(memberPlayer -> UtilServer.callEvent(new ScoreboardUpdateEvent(memberPlayer)));

            final String durationString = UtilTime.getTime(duration);

            UtilMessage.simpleMessage(sender, "Clan Energy", "You reset the energy for <var> to <green><var></green>.", Arrays.asList(this.getModule().getManager().getClanName(targetClan, ClanRelation.NEUTRAL), durationString));
            this.getModule().getManager().messageClan(targetClan, "Clan Energy", "Your Clan Energy has been reset to <green><var></green> by <yellow><var></yellow>.", Arrays.asList(durationString, sender.getName()), Collections.singletonList(sender instanceof Player ? UtilJava.cast(Player.class, sender).getUniqueId() : null));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return ClansArgumentType.CLANS.apply(args[0]);
            }

            return Collections.emptyList();
        }
    }
}