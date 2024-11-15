package me.trae.clans.clan.commands.chat;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ChatType;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.containers.ChatTypeContainer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ClanChatCommand extends Command<Clans, ClanManager> implements ChatTypeContainer<ChatType> {

    public ClanChatCommand(final ClanManager manager) {
        super(manager, "cc", new String[]{"clanchat"}, Rank.DEFAULT);
    }

    @Override
    public String getDescription() {
        return "Toggle Clan Chat";
    }

    @Override
    public ChatColor getUsageChatColor() {
        return MemberRole.RECRUIT.getChatColor();
    }

    @Override
    public ChatType getChatType() {
        return ChatType.CLAN_CHAT;
    }

    @Override
    public String getPrefix() {
        return "Clans";
    }

    @Override
    public List<Player> getReceivers(final Player player) {
        final Clan playerClan = this.getManager().getClanByPlayer(player);

        return UtilServer.getOnlinePlayers(playerClan::isMemberByPlayer);
    }

    @Override
    public String getFormat(final Player player, final Client client, final Gamer gamer, final String message, final boolean spy) {
        String displayName = player.getName();

        if (spy) {
            final Clan playerClan = this.getManager().getClanByPlayer(player);

            displayName = String.format("%s %s", playerClan.getName(), displayName);
        }

        return String.format("%s %s", ClanRelation.SELF.getSuffix() + displayName, ClanRelation.SELF.getPrefix() + message);
    }

    @Override
    public boolean canExecute(final Player player) {
        if (!(this.getManager().isClanByPlayer(player))) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return false;
        }

        return true;
    }

    @Override
    public boolean isSpyable() {
        return true;
    }
}