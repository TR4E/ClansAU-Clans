package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.command.ClanUnClaimEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class UnClaimCommand extends ClanSubCommand implements EventContainer<ClanUnClaimEvent> {

    public UnClaimCommand(final ClanCommand command) {
        super(command, "unclaim");
    }

    @Override
    public String getDescription() {
        return "Unclaim Territory";
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

        final Chunk chunk = player.getLocation().getChunk();

        Clan territoryClan = clan;
        if (!(clan.isTerritoryByChunk(chunk))) {
            territoryClan = this.getModule().getManager().getClanByChunk(chunk);
        }

        if (!(this.canUnClaimTerritory(player, client, clan, territoryClan))) {
            return;
        }

        this.callEvent(new ClanUnClaimEvent(clan, player, client, chunk, territoryClan));
    }

    private boolean canUnClaimTerritory(final Player player, final Client client, final Clan playerClan, final Clan territoryClan) {
        if (territoryClan == null) {
            UtilMessage.message(player, "Clans", "This Territory is not owned by anyone!");
            return false;
        }

        if (!(client.isAdministrating())) {
            if (territoryClan == playerClan) {
                return this.hasRequiredMemberRole(player, client, playerClan, true);
            } else if (territoryClan.isAdmin() || territoryClan.getTerritory().size() <= territoryClan.getMaxClaims(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "This Territory is not owned by your clan!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanUnClaimEvent> getClassOfEvent() {
        return ClanUnClaimEvent.class;
    }

    @Override
    public void onEvent(final ClanUnClaimEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan playerClan = event.getClan();
        final Player player = event.getPlayer();
        final Chunk chunk = event.getChunk();
        final Clan territoryClan = event.getTarget();

        this.getModule().getManager().unOutlineChunk(territoryClan, chunk);

        territoryClan.removeTerritory(chunk);
        this.getModule().getManager().getRepository().updateData(territoryClan, ClanProperty.TERRITORY);

        if (!(territoryClan.hasTerritory())) {
            this.getModule().getManager().resetEnergy(territoryClan);
        }

        UtilJava.call(territoryClan.getHome(), home -> {
            if (home != null && home.getChunk() == chunk) {
                territoryClan.setHome(null);
                this.getModule().getManager().getRepository().updateData(territoryClan, ClanProperty.HOME);
            }
        });

        if (territoryClan == playerClan) {
            UtilMessage.simpleMessage(player, "Clans", "You un-claimed territory at <var>.", Collections.singletonList(UtilChunk.chunkToString(chunk)));

            this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has un-claimed territory at <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), UtilChunk.chunkToString(chunk)), Collections.singletonList(player.getUniqueId()));
        } else {
            UtilMessage.simpleMessage(player, "Clans", "You un-claimed territory at <var> from <var>.", Arrays.asList(UtilChunk.chunkToString(chunk), this.getModule().getManager().getClanName(territoryClan, this.getModule().getManager().getClanRelationByClan(playerClan, territoryClan))));

            this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has un-claimed territory at <var> from <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), UtilChunk.chunkToString(chunk), this.getModule().getManager().getClanName(territoryClan, this.getModule().getManager().getClanRelationByClan(playerClan, territoryClan))), Collections.singletonList(player.getUniqueId()));
            this.getModule().getManager().messageClan(territoryClan, "Clans", "<var> has un-claimed territory at <var> from your Clan.", Arrays.asList(this.getModule().getManager().getClanRelationByClan(territoryClan, playerClan).getSuffix() + player.getName(), UtilChunk.chunkToString(chunk)), null);
        }
    }
}