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
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.*;
import me.trae.core.utility.constants.CoreArgumentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class EconomyCommand extends Command<Clans, GamerManager> implements AnyCommandType {

    public EconomyCommand(final GamerManager manager) {
        super(manager, "economy", new String[]{"eco", "balance", "bal", "coins", "gold", "money"}, Rank.DEFAULT);

        for (final String label : Arrays.asList("pay", "sendcoins")) {
            this.addShortcut(label, "pay");
        }
    }

    @Override
    public String getDescription() {
        return "Economy management";
    }

    @Override
    public void registerSubModules() {
        addSubModule(new SetCommand(this));
        addSubModule(new GiveCommand(this));
        addSubModule(new TakeCommand(this));
        addSubModule(new ResetCommand(this));
        addSubModule(new PayCommand(this));
        addSubModule(new TopCommand(this));
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (!(UtilCommand.isValidSender(sender, Player.class, true))) {
                return;
            }

            final Player player = UtilJava.cast(Player.class, sender);

            final Gamer gamer = this.getManager().getGamerByPlayer(player);

            UtilMessage.simpleMessage(player, "Economy", "Your Balance: <gold><var>", Collections.singletonList(gamer.getCoinsString()));
            return;
        }

        UtilCommand.processSubCommand(this, sender, args);
    }

    private static class SetCommand extends SubCommand<Clans, EconomyCommand> implements AnyCommandType {

        public SetCommand(final EconomyCommand module) {
            super(module, "set", Rank.ADMIN);
        }

        @Override
        public String getDescription() {
            return "Update Coins for a Player";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Economy", "You did not input an Amount.");
                return;
            }

            final Optional<Integer> amountOptional = UtilString.getArgument(Integer.class, args[1]);
            if (!(amountOptional.isPresent())) {
                UtilMessage.message(sender, "Economy", "You did not input a valid Amount.");
                return;
            }

            final int amount = amountOptional.get();

            if (amount < 0) {
                UtilMessage.message(sender, "Economy", "The amount must be greater than or equal to 0.");
                return;
            }

            final Gamer gamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (gamer != null) {
                gamer.setCoins(amount);
                this.getModule().getManager().getRepository().updateData(gamer, GamerProperty.COINS);

                UtilServer.callEvent(new ScoreboardUpdateEvent(targetPlayer));
            }

            final String coinsString = UtilString.toDollar(amount);

            UtilMessage.simpleMessage(sender, "Economy", "You updated the coins for <yellow><var></yellow> to <gold><var></gold>.", Arrays.asList(targetPlayer.getName(), coinsString));
            UtilMessage.simpleMessage(targetPlayer, "Economy", "Your coins are updated to <gold><var></gold> by <yellow><var></yellow>.", Arrays.asList(coinsString, sender.getName()));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class GiveCommand extends SubCommand<Clans, EconomyCommand> implements AnyCommandType {

        public GiveCommand(final EconomyCommand module) {
            super(module, "give", Rank.ADMIN);
        }

        @Override
        public String getDescription() {
            return "Give Coins to a Player";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Economy", "You did not input an Amount.");
                return;
            }

            final Optional<Integer> amountOptional = UtilString.getArgument(Integer.class, args[1]);
            if (!(amountOptional.isPresent())) {
                UtilMessage.message(sender, "Economy", "You did not input a valid Amount.");
                return;
            }

            final int amount = amountOptional.get();

            if (amount <= 0) {
                UtilMessage.message(sender, "Economy", "The amount must be greater than 0.");
                return;
            }

            final Gamer targetGamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (targetGamer == null) {
                UtilMessage.message(sender, "Economy", "Could not process sending money!");
                return;
            }

            targetGamer.setCoins(targetGamer.getCoins() + amount);
            this.getModule().getManager().getRepository().updateData(targetGamer, GamerProperty.COINS);
            UtilServer.callEvent(new ScoreboardUpdateEvent(targetPlayer));

            final String coinsString = UtilString.toDollar(amount);

            UtilMessage.simpleMessage(sender, "Economy", "You gave <gold><var></gold> to <yellow><var></yellow>.", Arrays.asList(coinsString, targetPlayer.getName()));
            UtilMessage.simpleMessage(targetPlayer, "Economy", "<yellow><var></yellow> gave you <gold><var></gold>.", Arrays.asList(sender.getName(), coinsString));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class TakeCommand extends SubCommand<Clans, EconomyCommand> implements AnyCommandType {

        public TakeCommand(final EconomyCommand module) {
            super(module, "take", Rank.ADMIN);
        }

        @Override
        public String getDescription() {
            return "Take Coins from a Player";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Economy", "You did not input an Amount.");
                return;
            }

            final Optional<Integer> amountOptional = UtilString.getArgument(Integer.class, args[1]);
            if (!(amountOptional.isPresent())) {
                UtilMessage.message(sender, "Economy", "You did not input a valid Amount.");
                return;
            }

            final int amount = amountOptional.get();

            if (amount <= 0) {
                UtilMessage.message(sender, "Economy", "The amount must be greater than 0.");
                return;
            }

            final Gamer targetGamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (targetGamer == null) {
                UtilMessage.message(sender, "Economy", "Could not process sending money!");
                return;
            }

            targetGamer.setCoins(targetGamer.getCoins() - amount);
            this.getModule().getManager().getRepository().updateData(targetGamer, GamerProperty.COINS);
            UtilServer.callEvent(new ScoreboardUpdateEvent(targetPlayer));

            final String coinsString = UtilString.toDollar(amount);

            UtilMessage.simpleMessage(sender, "Economy", "You took <gold><var></gold> from <yellow><var></yellow>.", Arrays.asList(coinsString, targetPlayer.getName()));
            UtilMessage.simpleMessage(targetPlayer, "Economy", "<yellow><var></yellow> took <gold><var></gold> from you.", Arrays.asList(sender.getName(), coinsString));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class ResetCommand extends SubCommand<Clans, EconomyCommand> implements AnyCommandType {

        public ResetCommand(final EconomyCommand module) {
            super(module, "reset", Rank.ADMIN);
        }

        @Override
        public String getDescription() {
            return "Reset Coins for a Player";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player>";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(sender, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            final Gamer targetGamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (targetGamer == null) {
                UtilMessage.message(sender, "Economy", "Could not process sending money!");
                return;
            }

            targetGamer.setCoins(this.getModule().getManager().starterCoinsAmount);
            this.getModule().getManager().getRepository().updateData(targetGamer, GamerProperty.COINS);
            UtilServer.callEvent(new ScoreboardUpdateEvent(targetPlayer));

            final String coinsString = UtilString.toDollar(this.getModule().getManager().starterCoinsAmount);

            UtilMessage.simpleMessage(sender, "Economy", "You reset the coins for <yellow><var></yellow> to <gold><var></gold>.", Arrays.asList(targetPlayer.getName(), coinsString));
            UtilMessage.simpleMessage(targetPlayer, "Economy", "Your coins have been reset to <gold><var></gold> by <yellow><var></yellow>.", Arrays.asList(coinsString, sender.getName()));
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(sender, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class PayCommand extends SubCommand<Clans, EconomyCommand> implements PlayerCommandType {

        public PayCommand(final EconomyCommand module) {
            super(module, "pay", Rank.DEFAULT);
        }

        @Override
        public String getDescription() {
            return "Pay Coins to a Player";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public void execute(final Player player, final Client client, final me.trae.core.gamer.Gamer gamer, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(player, "Economy", "You did not input a Player.");
                return;
            }

            final Player targetPlayer = UtilPlayer.searchPlayer(player, args[0], true);
            if (targetPlayer == null) {
                return;
            }

            if (targetPlayer == player) {
                UtilMessage.message(player, "Economy", "You cannot send yourself coins!");
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(player, "Economy", "You did not input an Amount.");
                return;
            }

            final Optional<Integer> amountOptional = UtilString.getArgument(Integer.class, args[1]);
            if (!(amountOptional.isPresent())) {
                UtilMessage.message(player, "Economy", "You did not input a valid Amount.");
                return;
            }

            final int amount = amountOptional.get();

            if (amount <= 0) {
                UtilMessage.message(player, "Economy", "The amount must be greater than 0.");
                return;
            }

            final Gamer playerGamer = this.getModule().getManager().getGamerByPlayer(player);
            final Gamer targetGamer = this.getModule().getManager().getGamerByPlayer(targetPlayer);
            if (playerGamer == null || targetGamer == null) {
                UtilMessage.message(player, "Economy", "Could not process sending money! (Contact Server Administrators)");
                return;
            }

            if (!(playerGamer.hasCoins(amount))) {
                UtilMessage.message(player, "Economy", "You do not have sufficient coins to send!");
                return;
            }

            playerGamer.setCoins(playerGamer.getCoins() - amount);
            this.getModule().getManager().getRepository().updateData(playerGamer, GamerProperty.COINS);
            UtilServer.callEvent(new ScoreboardUpdateEvent(player));

            targetGamer.setCoins(targetGamer.getCoins() + amount);
            this.getModule().getManager().getRepository().updateData(targetGamer, GamerProperty.COINS);
            UtilServer.callEvent(new ScoreboardUpdateEvent(targetPlayer));

            final String coinsString = UtilString.toDollar(amount);

            UtilMessage.simpleMessage(player, "Economy", "You sent <gold><var></gold> to <yellow><var></yellow>.", Arrays.asList(coinsString, targetPlayer.getName()));
            UtilMessage.simpleMessage(targetPlayer, "Economy", "<yellow><var></yellow> sent you <gold><var></gold>.", Arrays.asList(player.getName(), coinsString));
        }

        @Override
        public List<String> getTabCompletion(final Player player, final Client client, final me.trae.core.gamer.Gamer gamer, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(player, args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class TopCommand extends SubCommand<Clans, EconomyCommand> implements AnyCommandType {

        public TopCommand(final EconomyCommand module) {
            super(module, "top", Rank.ADMIN);
        }

        @Override
        public String getDescription() {
            return "View Top Balances";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            final List<Gamer> gamerList = new ArrayList<>(this.getInstance().getManagerByClass(GamerManager.class).getGamers().values());

            if (gamerList.isEmpty()) {
                UtilMessage.message(sender, "Economy", "Could not find any Top Balances!");
                return;
            }

            gamerList.sort(Comparator.comparingInt(Gamer::getCoins));
            Collections.reverse(gamerList);

            UtilMessage.simpleMessage(sender, "Economy", "Showing Top 10 Balances:");

            int count = 0;
            for (final Gamer gamer : gamerList) {
                if (count == 10) {
                    break;
                }

                UtilMessage.simpleMessage(sender, "<green>#<var></green> - <yellow><var></yellow>: <gold><var>", Arrays.asList(String.valueOf(count + 1), gamer.getPlayer().getName(), gamer.getCoinsString()));
                count++;
            }
        }
    }
}