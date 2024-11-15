package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanUnClaimAllEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class UnClaimAllCommand extends ClanSubCommand implements EventContainer<ClanUnClaimAllEvent> {

    public UnClaimAllCommand(final ClanCommand command) {
        super(command, "unclaimall");
    }

    @Override
    public String getDescription() {
        return "Unclaim All Territory";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.LEADER;
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

        if (!(this.canUnClaimAll(player, clan))) {
            return;
        }

        this.callEvent(new ClanUnClaimAllEvent(clan, player, client));
    }

    private boolean canUnClaimAll(final Player player, final Clan clan) {
        if (!(clan.hasTerritory())) {
            UtilMessage.message(player, "Clans", "Your Clan does not have any territory to un-claim!");
            return false;
        }

        return true;
    }

    @Override
    public Class<ClanUnClaimAllEvent> getClassOfEvent() {
        return ClanUnClaimAllEvent.class;
    }

    @Override
    public void onEvent(final ClanUnClaimAllEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final List<Chunk> chunks = clan.getTerritoryChunks();

        for (final Chunk chunk : chunks) {
            this.getModule().getManager().unOutlineChunk(clan, chunk);
        }

        clan.getTerritory().clear();
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.TERRITORY);

        clan.setHome(null);
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.HOME);

        UtilMessage.message(player, "Clans", "You un-claimed all territory.");

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has un-claimed all territory.", Collections.singletonList(ClanRelation.SELF.getSuffix() + player.getName()), Collections.singletonList(player.getUniqueId()));
    }
}