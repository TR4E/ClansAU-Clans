package me.trae.clans.fields.commands;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.AnyCommandType;
import me.trae.core.utility.*;
import org.bukkit.command.CommandSender;

public class FieldsCommand extends Command<Clans, FieldsManager> implements AnyCommandType {

    public FieldsCommand(final FieldsManager manager) {
        super(manager, "fields", new String[0], Rank.ADMIN);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new ResetCommand(this));
        addSubModule(new InfoCommand(this));
    }

    @Override
    public String getDescription() {
        return "Fields management";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        UtilCommand.processSubCommand(this, sender, args);
    }

    private static class ResetCommand extends SubCommand<Clans, FieldsCommand> implements AnyCommandType {

        public ResetCommand(final FieldsCommand module) {
            super(module, "reset");
        }

        @Override
        public String getDescription() {
            return "Replenish all Fields Blocks";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            this.getModule().getManager().replenish();
        }
    }

    private static class InfoCommand extends SubCommand<Clans, FieldsCommand> implements AnyCommandType {

        public InfoCommand(final FieldsCommand module) {
            super(module, "info");
        }

        @Override
        public String getDescription() {
            return "View Fields Information";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            UtilMessage.message(sender, "Fields", "Information:");

            UtilJava.call(this.getModule().getManager().getBlocks().values(), list -> {
                final String remainingBlocks = String.valueOf(list.stream().filter(fieldsBlock -> !(fieldsBlock.isBroken())).count());
                final String totalBlocks = String.valueOf(list.size());

                UtilMessage.simpleMessage(sender, UtilString.pair("<green>Blocks", UtilString.format("<white>%s/%s", remainingBlocks, totalBlocks)));
            });

            UtilMessage.simpleMessage(sender, UtilString.pair("<green>Last Restored", UtilString.format("<white>%s", UtilTime.getTime(System.currentTimeMillis() - this.getModule().getManager().getLastUpdated()))));
        }
    }
}