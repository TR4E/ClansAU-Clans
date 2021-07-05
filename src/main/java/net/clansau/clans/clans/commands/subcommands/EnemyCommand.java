package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EnemyCommand extends IClanCommand {

    public EnemyCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan to Enemy.");
            return;
        }
        final Clan target = getManager().searchClan(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.equals(clan)) {
            UtilMessage.message(player, "Clans", "You cannot enemy yourself.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canEnemyClan(player, client, clan, target))) {
            return;
        }
        if (target.isEnemied(clan)) {
            UtilMessage.message(player, "Clans", "You are already enemies with " + ChatColor.RED + getManager().getName(target, true) + ChatColor.GRAY + ".");
            return;
        }
        if (target.isAllied(clan)) {
            clan.getAlliesMap().remove(target.getName());
            target.getAlliesMap().remove(clan.getName());
            getManager().getRepository().updateAllies(clan);
            getManager().getRepository().updateAllies(target);
        }
        clan.getEnemiesMap().put(target.getName(), 0);
        target.getEnemiesMap().put(clan.getName(), 0);
        getManager().getRepository().updateEnemies(clan);
        getManager().getRepository().updateEnemies(target);
        UtilMessage.message(player, "Clans", "You waged war with " + ChatColor.RED + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " waged war with " + ChatColor.RED + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", ChatColor.RED + getManager().getName(clan, true) + ChatColor.GRAY + " waged war with your Clan.", null);
    }

    private boolean canEnemyClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to enemy a Clan.");
                return false;
            }
            if (target instanceof AdminClan) {
                UtilMessage.message(player, "Clans", "You cannot enemy Admin Clans.");
                return false;
            }
            if (target.isAllied(clan) || target.isBeingPillagedBy(clan) || clan.isBeingPillagedBy(target)) {
                UtilMessage.message(player, "Clans", "You must be neutral with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
            }
        }
        return true;
    }
}