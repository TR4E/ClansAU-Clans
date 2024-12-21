package me.trae.clans.worldevent.commands;

import me.trae.clans.Clans;
import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.AnyCommandType;
import me.trae.core.utility.UtilCommand;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.constants.CoreArgumentType;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WorldEventCommand extends Command<Clans, WorldEventManager> implements AnyCommandType {

    public WorldEventCommand(final WorldEventManager manager) {
        super(manager, "worldevent", new String[]{"event", "events"}, Rank.ADMIN);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new StartCommand(this));
        addSubModule(new StopCommand(this));
    }

    @Override
    public String getDescription() {
        return "World Event management";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        UtilCommand.processSubCommand(this, sender, args);
    }

    private static class StartCommand extends SubCommand<Clans, WorldEventCommand> implements AnyCommandType {

        public StartCommand(final WorldEventCommand module) {
            super(module, "start");
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <world event>";
        }

        @Override
        public String getDescription() {
            return "Start a World Event";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "World Event", "You did not input a World Event.");
                return;
            }

            if (this.getModule().getManager().getActiveWorldEvent() != null) {
                UtilMessage.message(sender, "World Event", "There already is a World Event active.");
                return;
            }

            final WorldEvent worldEvent = this.getModule().getManager().searchWorldEvent(sender, args[0], true);
            if (worldEvent == null) {
                return;
            }

            worldEvent.start();
        }

        @Override
        public List<String> getTabCompletion(final CommandSender sender, final String[] args) {
            if (args.length == 1) {
                return CoreArgumentType.CUSTOM.apply(this.getModule().getManager().getModulesByClass(WorldEvent.class).stream().map(WorldEvent::getSlicedName).collect(Collectors.toList()), args[0]);
            }

            return Collections.emptyList();
        }
    }

    private static class StopCommand extends SubCommand<Clans, WorldEventCommand> implements AnyCommandType {

        public StopCommand(final WorldEventCommand module) {
            super(module, "stop");
        }

        @Override
        public String getDescription() {
            return "Stop the Active World Event";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            final WorldEvent activeWorldEvent = this.getModule().getManager().getActiveWorldEvent();
            if (activeWorldEvent == null) {
                UtilMessage.message(sender, "World Event", "There is no World Event to Stop!");
                return;
            }

            activeWorldEvent.end();
        }
    }
}