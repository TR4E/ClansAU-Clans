package me.trae.clans.clan.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.enums.RequestType;
import me.trae.clans.clan.types.AdminClan;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public interface IClan {

    default boolean isAdmin() {
        return this instanceof AdminClan;
    }

    String getType();

    String getName();

    String getDisplayName();

    /*

     */

    List<String> getTerritory();

    List<Chunk> getTerritoryChunks();

    void addTerritory(final Chunk chunk);

    void removeTerritory(final Chunk chunk);

    boolean isTerritoryByChunk(final Chunk chunk);

    boolean hasTerritory();

    int getMaxClaims(final ClanManager manager);

    String getTerritoryString(final ClanManager manager);

    /*

     */

    LinkedHashMap<RequestType, LinkedHashMap<String, Long>> getRequests();

    void addRequest(final RequestType requestType, final String key);

    void removeRequest(final RequestType requestType, final String key);

    boolean isRequested(final RequestType requestType, final String key);

    /*

     */

    LinkedHashMap<UUID, Member> getMembers();

    void addMember(final Member member);

    void removeMember(final Member member);

    Member getMemberByUUID(final UUID uuid);

    Member getMemberByPlayer(final Player player);

    boolean isMemberByUUID(final UUID uuid);

    boolean isMemberByPlayer(final Player player);

    String getMembersString(final Player receiverPlayer);

    LinkedHashMap<Player, Member> getOnlineMembers();

    /*

     */

    LinkedHashMap<String, Alliance> getAlliances();

    void addAlliance(final Alliance alliance);

    void removeAlliance(final Alliance alliance);

    Alliance getAllianceByClan(final Clan clan);

    boolean isAllianceByClan(final Clan clan);

    boolean isTrustedByClan(final Clan clan);

    String getAlliesString(final ClanManager manager, final Clan receiverClan);

    /*

     */

    LinkedHashMap<String, Enemy> getEnemies();

    void addEnemy(final Enemy enemy);

    void removeEnemy(final Enemy enemy);

    Enemy getEnemyByClan(final Clan clan);

    boolean isEnemyByClan(final Clan clan);

    String getEnemiesString(final ClanManager manager, final Clan receiverClan);

    String getShortDominanceString(final Clan receiverClan);

    String getDominanceString(final Clan receiverClan);

    /*

     */

    LinkedHashMap<String, Pillage> getPillages();

    void addPillage(final Pillage pillage);

    void removePillage(final Pillage pillage);

    Pillage getPillageByClan(final Clan clan);

    boolean isPillageByClan(final Clan clan);

    String getPillagesString(final ClanManager manager, final Clan receiverClan);

    boolean isBeingPillaged(final ClanManager manager);

    /*

     */

    boolean isNeutralByClan(final Clan clan);

    boolean isOnline();

    boolean isOnline(final Player receiverPlayer);

    boolean isSquadFull(final ClanManager manager);

    /*

     */

    long getCreated();

    String getCreatedString();

    long getLastOnline();

    void setLastOnline(final long lastOnline);

    long getLastTNTed();

    void setLastTNTed(final long lastTNTed);

    boolean isTNTProtected(final ClanManager manager);

    String getTNTProtectionString(final ClanManager manager, final Player receiverPlayer);

    int getEnergy();

    void setEnergy(final int energy);

    long getEnergyDuration();

    double getEnergyDepletionRatio();

    String getEnergyRemainingString();

    UUID getFounder();

    String getFounderString();

    Location getHome();

    void setHome(final Location home);

    boolean hasHome();

    String getHomeString();
}