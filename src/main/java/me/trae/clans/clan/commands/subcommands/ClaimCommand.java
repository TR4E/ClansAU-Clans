package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.command.ClanClaimEvent;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class ClaimCommand extends ClanSubCommand implements EventContainer<ClanClaimEvent> {

    public ClaimCommand(final ClanCommand command) {
        super(command, "claim");
    }

    @Override
    public String getDescription() {
        return "Claim Territory";
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

        final Chunk chunk = player.getLocation().getChunk();

        if (!(this.canClaimTerritory(player, client, clan, chunk))) {
            return;
        }

        this.callEvent(new ClanClaimEvent(clan, player, client, chunk));
    }

    private boolean canClaimTerritory(final Player player, final Client client, final Clan clan, final Chunk chunk) {
        final Clan territoryClan = this.getModule().getManager().getClanByChunk(chunk);
        if (territoryClan != null) {
            if (territoryClan == clan) {
                UtilMessage.message(player, "Clans", "This Territory is already owned by your clan!");
                return false;
            }

            UtilMessage.simpleMessage(player, "Clans", "This Territory is owned by <var>!", Collections.singletonList(this.getModule().getManager().getClanName(territoryClan, this.getModule().getManager().getClanRelationByClan(clan, territoryClan))));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (clan.getTerritory().size() >= clan.getMaxClaims(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "You cannot claim any more land!");
                return false;
            }

            final ClientManager clientManager = this.getInstance(Core.class).getManagerByClass(ClientManager.class);

            for (final Player nearbyPlayer : UtilChunk.getChunkEntities(Player.class, chunk)) {
                final Clan nearbyPlayerClan = this.getModule().getManager().getClanByPlayer(nearbyPlayer);

                if (nearbyPlayerClan == clan || nearbyPlayerClan.isAllianceByClan(clan) || clientManager.getClientByPlayer(nearbyPlayer).isAdministrating()) {
                    continue;
                }

                UtilMessage.message(player, "Clans", "You cannot claim land containing enemies!");
                return false;
            }

            boolean isNextToOwnTerritory = false;

            for (final Chunk nearbyChunk : UtilChunk.getNearbyChunks(chunk, 1)) {
                final Clan nearbyChunkClan = this.getModule().getManager().getClanByChunk(nearbyChunk);
                if (nearbyChunkClan == null) {
                    continue;
                }

                if (nearbyChunkClan == clan) {
                    isNextToOwnTerritory = true;
                    continue;
                }

                UtilMessage.message(player, "Clans", "You cannot claim land next to enemy territory!");
                return false;
            }

            if (!(isNextToOwnTerritory) && clan.hasTerritory()) {
                UtilMessage.message(player, "Clans", "You can only claim next to your own territory!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanClaimEvent> getClassOfEvent() {
        return ClanClaimEvent.class;
    }

    @Override
    public void onEvent(final ClanClaimEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Chunk chunk = event.getChunk();

        this.getModule().getManager().outlineChunk(clan, chunk);

        clan.addTerritory(chunk);
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.TERRITORY);

        UtilMessage.simpleMessage(player, "Clans", "You claimed land at <var>.", Collections.singletonList(UtilChunk.chunkToString(chunk)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has claimed land at <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), UtilChunk.chunkToString(chunk)), Collections.singletonList(player.getUniqueId()));
    }
}