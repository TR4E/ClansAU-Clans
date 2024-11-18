package me.trae.clans.clan.commands;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MassClaimCommand extends Command<Clans, ClanManager> implements PlayerCommandType {

    public MassClaimCommand(final ClanManager manager) {
        super(manager, "massclaim", new String[0], Rank.OWNER);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        final Clan playerClan = this.getManager().getClanByPlayer(player);
        if (playerClan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You did not input a Size.");
            return;
        }

        final Optional<Integer> sizeOptional = UtilString.getArgument(Integer.class, args[0]);
        if (!(sizeOptional.isPresent())) {
            UtilMessage.message(player, "Clans", "You did not input a valid Size.");
            return;
        }

        final int size = sizeOptional.get();

        int count = 0;

        final Set<Clan> territoryClanSet = new HashSet<>();

        for (final Chunk nearbyChunk : UtilChunk.getNearbyChunks(player.getLocation().getChunk(), size)) {
            final Clan territoryClan = this.getManager().getClanByChunk(nearbyChunk);
            if (territoryClan != null) {
                territoryClan.removeTerritory(nearbyChunk);
                territoryClanSet.add(territoryClan);
            }

            playerClan.addTerritory(nearbyChunk);
            count++;
        }

        territoryClanSet.forEach(territoryClan -> this.getManager().getRepository().updateData(territoryClan, ClanProperty.TERRITORY));

        this.getManager().getRepository().updateData(playerClan, ClanProperty.TERRITORY);

        UtilMessage.simpleMessage(player, "Clans", "You have mass-claimed <yellow><var></yellow> Chunks for <var>.", Arrays.asList(String.valueOf(count), this.getManager().getClanName(playerClan, ClanRelation.SELF)));
    }
}