package me.trae.clans.world.commands;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.clans.world.enums.WarpType;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.AnyCommandType;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class LocationsCommand extends Command<Clans, WorldManager> implements AnyCommandType {

    public LocationsCommand(final WorldManager manager) {
        super(manager, "locations", new String[]{"coords"}, Rank.DEFAULT);
    }

    @Override
    public String getDescription() {
        return "Show List of Map Locations";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        UtilMessage.message(sender, "Locations", "Showing List of Map Locations:");

        for (final WarpType warpType : WarpType.values()) {
            final Location location = warpType.getLocation();
            if (location == null) {
                continue;
            }

            UtilMessage.simpleMessage(sender, "Locations", "The <var> is located at <var>.", Arrays.asList(warpType.getDisplayName(), UtilLocation.locationToString(location)));
        }
    }
}