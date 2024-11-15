package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanDeleteHomeEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Collections;

public class DeleteHomeCommand extends ClanSubCommand implements EventContainer<ClanDeleteHomeEvent> {

    public DeleteHomeCommand(final ClanCommand command) {
        super(command, "delhome");
    }

    @Override
    public String getShortcut() {
        return "delhome";
    }

    @Override
    public String getDescription() {
        return "Delete Clan Home";
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

        if (!(this.canDeleteHome(player, clan))) {
            return;
        }

        this.callEvent(new ClanDeleteHomeEvent(clan, player, client));
    }

    private boolean canDeleteHome(final Player player, final Clan clan) {
        if (!(clan.hasHome())) {
            UtilMessage.message(player, "Clans", "Your Clan does not have a home set!");
            return false;
        }

        return true;
    }

    @Override
    public Class<ClanDeleteHomeEvent> getClassOfEvent() {
        return ClanDeleteHomeEvent.class;
    }

    @Override
    public void onEvent(final ClanDeleteHomeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        clan.setHome(null);
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.HOME);

        UtilMessage.simpleMessage(player, "Clans", "You deleted the Clan Home.");

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has deleted the Clan Home.", Collections.singletonList(ClanRelation.SELF.getSuffix() + player.getName()), Collections.singletonList(player.getUniqueId()));
    }
}