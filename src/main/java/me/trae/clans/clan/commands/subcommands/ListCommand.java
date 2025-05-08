package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand extends ClanSubCommand {

    public ListCommand(final ClanCommand module) {
        super(module, "list", Rank.ADMIN);
    }


    @Override
    public ChatColor getUsageChatColor() {
        return this.getRequiredMemberRole().getChatColor();
    }

    @Override
    public String getDescription() {
        return "Show List of Clans";
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        final List<Clan> clanList = new ArrayList<>(this.getModule().getManager().getClans().values());

        if (clanList.isEmpty()) {
            UtilMessage.message(player, "Clan List", "Could not find any registered clients.");
            return;
        }

        clanList.sort(Comparator.comparingInt((Clan value) -> value.isAdmin() ? 1 : 0).reversed().thenComparingLong(Clan::getCreated).reversed());

        Collections.reverse(clanList);

        UtilMessage.simpleMessage(player, "Clan List", "Showing <yellow><var></yellow> Clans:", Collections.singletonList(String.valueOf(clanList.size())));

        final String string = clanList.stream().map(value -> {
            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByClan(clan, value);

            return this.getModule().getManager().getClanShortName(value, clanRelation);
        }).collect(Collectors.joining("<gray>, "));

        UtilMessage.simpleMessage(player, "Clan List", "<gray>[<var><gray>]", Collections.singletonList(string));
    }
}