package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.types.AdminClan;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SafeCommand extends ClanSubCommand {

    public SafeCommand(final ClanCommand module) {
        super(module, "safe", Rank.ADMIN);
    }

    @Override
    public ChatColor getUsageChatColor() {
        return this.getRequiredRank().getChatColor();
    }

    @Override
    public String getDescription() {
        return "Toggle Safe Clan";
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(clan.isAdmin())) {
            UtilMessage.simpleMessage(player, "Clans", "<var> is not an Admin Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.SELF)));
            return;
        }

        final AdminClan adminClan = UtilJava.cast(AdminClan.class, clan);

        if (adminClan.isSafe()) {
            adminClan.setSafe(false);

            UtilMessage.simpleMessage(player, "Clans", "<var> is no longer a Safe Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.SELF)));
        } else {
            adminClan.setSafe(true);

            UtilMessage.simpleMessage(player, "Clans", "<var> is now a Safe Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.SELF)));
        }

        this.getModule().getManager().getRepository().updateData(adminClan, ClanProperty.SAFE);
    }
}