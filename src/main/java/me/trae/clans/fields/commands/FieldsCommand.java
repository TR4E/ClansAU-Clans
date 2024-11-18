package me.trae.clans.fields.commands;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.SubCommand;
import me.trae.core.command.types.models.AnyCommandType;
import me.trae.core.utility.UtilCommand;
import org.bukkit.command.CommandSender;

public class FieldsCommand extends Command<Clans, FieldsManager> implements AnyCommandType {

    public FieldsCommand(final FieldsManager manager) {
        super(manager, "fields", new String[0], Rank.ADMIN);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new ResetCommand(this));
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
}