package me.trae.clans.koth.commands;

import me.trae.clans.Clans;
import me.trae.clans.koth.Koth;
import me.trae.clans.koth.KothManager;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilCommand;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class KothCommand extends Command<Clans, KothManager> implements PlayerCommandType {

    public KothCommand(final KothManager manager) {
        super(manager, "koth", new String[0], Rank.ADMIN);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new StartCommand(this));
        addSubModule(new StopCommand(this));
    }

    @Override
    public String getDescription() {
        return "KoTH management";
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        UtilCommand.processSubCommand(this, player, args);
    }

    private static class StartCommand extends SubCommand<Clans, KothCommand> implements PlayerCommandType {

        public StartCommand(final KothCommand module) {
            super(module, "start");
        }

        @Override
        public String getDescription() {
            return "Start the KoTH";
        }

        @Override
        public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
            final Koth koth = this.getModule().getManager().getModuleByClass(Koth.class);

            if (koth.isActive()) {
                UtilMessage.message(player, "KoTH", "The KoTH is already active!");
                return;
            }

            koth.start();
        }
    }

    private static class StopCommand extends SubCommand<Clans, KothCommand> implements PlayerCommandType {

        public StopCommand(final KothCommand module) {
            super(module, "stop");
        }

        @Override
        public String getDescription() {
            return "Start the KoTH";
        }

        @Override
        public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
            final Koth koth = this.getModule().getManager().getModuleByClass(Koth.class);

            if (!(koth.isActive())) {
                UtilMessage.message(player, "KoTH", "There is currently no KoTH active!");
                return;
            }

            koth.stop();
        }
    }
}