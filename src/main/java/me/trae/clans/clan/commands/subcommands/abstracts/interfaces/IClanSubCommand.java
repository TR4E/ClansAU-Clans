package me.trae.clans.clan.commands.subcommands.abstracts.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.core.client.Client;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import org.bukkit.entity.Player;

import java.util.List;

public interface IClanSubCommand extends PlayerCommandType {

    MemberRole getRequiredMemberRole();

    void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args);

    List<String> getTabCompletion(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args);

    boolean hasRequiredMemberRole(final Player player, final Client client, final Clan clan, final MemberRole requiredMemberRole, final boolean inform);

    default boolean hasRequiredMemberRole(final Player player, final Client client, final Clan clan, final boolean inform) {
        return this.hasRequiredMemberRole(player, client, clan, this.getRequiredMemberRole(), inform);
    }
}