package me.trae.clans.clan.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.enums.AccessType;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import me.trae.core.utility.objects.Pair;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IClanManager {

    Map<String, Clan> getClans();

    void addClan(final Clan clan);

    void removeClan(final Clan clan);

    Clan getClanByName(final String name);

    Clan getClanByUUID(final UUID uuid);

    Clan getClanByPlayer(final Player player);

    Clan getClanByChunk(final Chunk chunk);

    Clan getClanByLocation(final Location location);

    boolean isClanByName(final String name);

    boolean isClanByUUID(final UUID uuid);

    boolean isClanByPlayer(final Player player);

    boolean isClanByChunk(final Chunk chunk);

    boolean isClanByLocation(final Location location);

    List<Clan> getNearbyClans(final Chunk chunk, int distance);

    /*

     */

    Clan searchClan(final CommandSender sender, final String name, final boolean inform);

    Client searchMember(final CommandSender sender, final Clan clan, final String name, final boolean inform);

    void messageClan(final Clan clan, final String prefix, final String message, final List<String> variables, final List<UUID> ignore);

    void messageAllies(final Clan clan, final String prefix, final String message, final List<String> variables, final List<UUID> ignore);

    /*

     */

    ClanRelation getClanRelationByClan(final Clan clan, final Clan target);

    ClanRelation getClanRelationByPlayer(final Player player, final Player target);

    LinkedHashMap<String, String> getClanInformation(final Player player, final Client client, final Clan playerClan, final Clan targetClan);

    /*

     */

    String getClanFullName(final Clan clan, final ClanRelation clanRelation);

    String getClanShortName(final Clan clan, final ClanRelation clanRelation);

    String getClanName(final Clan clan, final ClanRelation clanRelation);

    String getTerritoryClanNameForChat(final Clan playerClan, final Clan territoryClan, final Location location);

    String getTerritoryClanNameForScoreboard(final Clan playerClan, final Clan territoryClan);

    Pair<String, String> getTerritoryClanNameForTitle(final Clan playerClan, final Clan territoryClan, final Location location);

    /*

     */

    boolean hasAccess(final Player player, final Clan playerClan, final Clan territoryClan, final AccessType accessType);

    boolean canHurt(final Player damager, final Player damagee);

    boolean canCast(final Location location);

    boolean canCast(final Player player);

    boolean isSafeByLocation(final Location location);

    boolean isSafeByPlayer(final Player player);

    /*

     */

    void removeClanChat(final Player player);

    void outlineChunk(final Clan clan, final Chunk chunk);

    void unOutlineChunk(final Clan clan, final Chunk chunk);

    void resetEnergy(final Clan clan);

    void disbandClan(final Clan clan);
}