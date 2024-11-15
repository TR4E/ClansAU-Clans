package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanSetHomeEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class SetHomeCommand extends ClanSubCommand implements EventContainer<ClanSetHomeEvent> {

    public SetHomeCommand(final ClanCommand command) {
        super(command, "sethome");
    }

    @Override
    public String getShortcut() {
        return "sethome";
    }

    @Override
    public String getDescription() {
        return "Set Clan Home";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.ADMIN;
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(this.hasRequiredMemberRole(player, client, clan, true))) {
            return;
        }

        if (!(this.canSetHome(player, clan))) {
            return;
        }

        this.callEvent(new ClanSetHomeEvent(clan, player, client));
    }

    private boolean canSetHome(final Player player, final Clan clan) {
        final Clan territoryClan = this.getModule().getManager().getClanByLocation(player.getLocation());
        if (territoryClan == null || territoryClan != clan) {
            UtilMessage.message(player, "Clans", "You can only set home in your own territory!");
            return false;
        }

        return true;
    }

    @Override
    public Class<ClanSetHomeEvent> getClassOfEvent() {
        return ClanSetHomeEvent.class;
    }

    @Override
    public void onEvent(final ClanSetHomeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        clan.setHome(player.getLocation());
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.HOME);

        UtilMessage.simpleMessage(player, "Clans", "You set the Clan Home at <var>.", Collections.singletonList(clan.getHomeString()));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has set the Clan Home at <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), clan.getHomeString()), Collections.singletonList(player.getUniqueId()));
    }
}