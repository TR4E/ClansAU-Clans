package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.menus.energy.EnergyMenu;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class EnergyCommand extends ClanSubCommand {

    public EnergyCommand(final ClanCommand module) {
        super(module, "energy");
    }

    @Override
    public String getDescription() {
        return "Buy Clan Energy";
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (!(this.getModule().getManager().energyEnabled)) {
            UtilMessage.message(player, "Clans", "Clan Energy is currently disabled!");
            return;
        }

        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(clan.hasTerritory())) {
            UtilMessage.message(player, "Clans", "You do not need to buy energy when you have no territory!");
            return;
        }

        UtilMenu.open(new EnergyMenu(this.getModule().getManager(), player) {
            @Override
            public Clan getClan() {
                return clan;
            }
        });
    }
}