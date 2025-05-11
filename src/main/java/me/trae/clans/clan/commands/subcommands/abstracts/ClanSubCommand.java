package me.trae.clans.clan.commands.subcommands.abstracts;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.interfaces.IClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.SubCommand;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ClanSubCommand extends SubCommand<Clans, ClanCommand> implements IClanSubCommand {

    public ClanSubCommand(final ClanCommand module, final String label, final Rank requiredRank) {
        super(module, label, requiredRank);
    }

    public ClanSubCommand(final ClanCommand module, final String label) {
        this(module, label, Rank.DEFAULT);
    }

    @Override
    public ChatColor getUsageChatColor() {
        final MemberRole requiredMemberRole = this.getRequiredMemberRole();

        return requiredMemberRole != null ? requiredMemberRole.getChatColor() : this.getRequiredRank().getChatColor();
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return null;
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        final Clan playerClan = this.getModule().getManager().getClanByPlayer(player);

        this.execute(player, client, gamer, playerClan, args);
    }

    @Override
    public List<String> getTabCompletion(final Player player, final Client client, final Gamer gamer, final String[] args) {
        final Clan playerClan = this.getModule().getManager().getClanByPlayer(player);

        return this.getTabCompletion(player, client, gamer, playerClan, args);
    }

    @Override
    public List<String> getTabCompletion(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasRequiredMemberRole(final Player player, final Client client, final Clan clan, final MemberRole requiredMemberRole, final boolean inform) {
        if (requiredMemberRole == null) {
            return true;
        }

        if (client.isAdministrating()) {
            return true;
        }

        if (clan.isMemberByPlayer(player) && clan.getMemberByPlayer(player).hasRole(requiredMemberRole)) {
            return true;
        }

        if (inform) {
            UtilMessage.simpleMessage(player, "Clans", "You must be Clan <var> to <white><var></white>!", Arrays.asList(requiredMemberRole.getChatColor() + requiredMemberRole.getName(), this.getDescription()));
        }

        return false;
    }
}