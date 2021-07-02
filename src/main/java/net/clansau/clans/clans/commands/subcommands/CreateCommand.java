package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.IClanCommand;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.recharge.RechargeManager;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class CreateCommand extends IClanCommand {

    public CreateCommand(final ClanManager manager, final Player player, final String[] args) {
        super(manager, player, args);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        if (getManager().getClan(player.getUniqueId()) != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return;
        }
        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan name.");
            return;
        }
        final String name = args[1];
        if (getManager().isClan(name)) {
            UtilMessage.message(player, "Clans", "Clan name is already used by another Clan.");
            return;
        }
        if (UtilFormat.hasSymbols(name)) {
            UtilMessage.message(player, "Clans", "You cannot have special characters in your Clan name.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(client.isAdministrating())) {
            if (!(getManager().isNameAllowed(name))) {
                UtilMessage.message(player, "Clans", "You cannot use that as your Clan name.");
                return;
            }
//            final OptionsManager optionsManager = getInstance().getManager(OptionsManager.class);
//            if (name.length() > optionsManager.getMaxClanNameLength()) {
//                UtilMessage.message(player, "Clans", "Clan name is too long. Maximum length is " + ChatColor.YELLOW + optionsManager.getMaxClanNameLength() + ChatColor.GRAY + ".");
//                return;
//            }
//            if (name.length() < optionsManager.getMinClanNameLength()) {
//                UtilMessage.message(player, "Clans", "Clan name is too long. Minimum length is " + ChatColor.YELLOW + optionsManager.getMinClanNameLength() + ChatColor.GRAY + ".");
//                return;
//            }
            if (!(getInstance().getManager(RechargeManager.class).add(player, "Clan Create", 600000L, true))) {
                return;
            }
            Clan clan = new Clan(getInstance(), name);
            if (client.isAdministrating()) {
                clan = new AdminClan(getInstance(), name);
            }
//            Bukkit.getServer().getPluginManager().callEvent(new ClanCreateEvent(player, clan));
        }
    }
}