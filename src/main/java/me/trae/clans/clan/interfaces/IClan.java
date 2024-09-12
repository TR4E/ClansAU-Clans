package me.trae.clans.clan.interfaces;

import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.Member;
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

    boolean isTerritory(final Chunk chunk);

    boolean hasTerritory();

    int getMaxClaims(final ClanManager manager);

    String getTerritoryString(final ClanManager manager);

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

    /*

     */

    boolean isOnline();

    boolean isOnline(final Player receiverPlayer);

    boolean isSquadFull(final ClanManager manager);

    /*

     */

    long getCreated();

    String getCreatedString();

    String getTNTProtectionString(final ClanManager manager, final Player receiverPlayer);

    UUID getFounder();

    String getFounderString();

    Location getHome();

    void setHome(final Location home);

    boolean hasHome();

    String getHomeString();
}