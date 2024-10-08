package me.trae.clans.clan;

import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.interfaces.IClan;
import me.trae.core.utility.*;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class Clan implements IClan {

    private final String name;

    private final List<String> territory = new ArrayList<>();

    private final LinkedHashMap<UUID, Member> members = new LinkedHashMap<>();
    private final LinkedHashMap<String, Alliance> alliances = new LinkedHashMap<>();
    private final LinkedHashMap<String, Enemy> enemies = new LinkedHashMap<>();
    private final LinkedHashMap<String, Pillage> pillages = new LinkedHashMap<>();

    private long created;
    private UUID founder;
    private Location home;

    public Clan(final String name) {
        this.name = name;
    }

    public Clan(final String name, final Player player) {
        this(name);

        this.created = System.currentTimeMillis();
        this.founder = player.getUniqueId();

        this.addMember(new Member(player, MemberRole.LEADER));
    }

    @Override
    public String getType() {
        return "Clan";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        if (this.isAdmin() && this.getName().contains("_") && !(this.getName().startsWith("_") && this.getName().endsWith("_"))) {
            return this.getName().replaceAll("_", " ");
        }

        return this.getName();
    }

    @Override
    public List<String> getTerritory() {
        return this.territory;
    }

    @Override
    public List<Chunk> getTerritoryChunks() {
        return UtilChunk.toList(this.getTerritory());
    }

    @Override
    public void addTerritory(final Chunk chunk) {
        this.getTerritory().add(UtilChunk.chunkToFile(chunk));
    }

    @Override
    public void removeTerritory(final Chunk chunk) {
        this.getTerritory().remove(UtilChunk.chunkToFile(chunk));
    }

    @Override
    public boolean isTerritory(final Chunk chunk) {
        return this.getTerritory().contains(UtilChunk.chunkToFile(chunk));
    }

    @Override
    public boolean hasTerritory() {
        return !(this.getTerritory().isEmpty());
    }

    @Override
    public int getMaxClaims(final ClanManager manager) {
        final int maxClaims = 3 + this.getMembers().size();

        return UtilMath.getMinAndMax(Integer.class, 0, manager.getPrimitiveCasted(Integer.class, "Max-Claim-Limit"), maxClaims);
    }

    @Override
    public String getTerritoryString(final ClanManager manager) {
        if (this.isAdmin()) {
            return String.format("%s/∞", this.getTerritory().size());
        }

        return String.format("%s/%s", this.getTerritory().size(), this.getMaxClaims(manager));
    }

    @Override
    public LinkedHashMap<UUID, Member> getMembers() {
        return this.members;
    }

    @Override
    public void addMember(final Member member) {
        this.getMembers().put(member.getUUID(), member);
    }

    @Override
    public void removeMember(final Member member) {
        this.getMembers().remove(member.getUUID());
    }

    @Override
    public Member getMemberByUUID(final UUID uuid) {
        return this.getMembers().getOrDefault(uuid, null);
    }

    @Override
    public Member getMemberByPlayer(final Player player) {
        return this.getMemberByUUID(player.getUniqueId());
    }

    @Override
    public boolean isMemberByUUID(final UUID uuid) {
        return this.getMembers().containsKey(uuid);
    }

    @Override
    public boolean isMemberByPlayer(final Player player) {
        return this.isMemberByUUID(player.getUniqueId());
    }

    @Override
    public String getMembersString(final Player receiverPlayer) {
        final List<String> list = new ArrayList<>();

        for (final Member member : this.getMembers().values()) {
            final ChatColor chatColor = (member.isOnline() && receiverPlayer.canSee(member.getOnlinePlayer()) ? ChatColor.GREEN : ChatColor.RED);
            final String name = UtilPlayer.getPlayerNameByUUID(member.getUUID());

            list.add(String.format("<yellow>%s<gray>.%s", member.getRole().getPrefix(), chatColor + name));
        }

        return String.join("<white>, ", list);
    }

    @Override
    public LinkedHashMap<String, Alliance> getAlliances() {
        return this.alliances;
    }

    @Override
    public void addAlliance(final Alliance alliance) {
        this.getAlliances().put(alliance.getName().toLowerCase(), alliance);
    }

    @Override
    public void removeAlliance(final Alliance alliance) {
        this.getAlliances().remove(alliance.getName().toLowerCase());
    }

    @Override
    public Alliance getAllianceByClan(final Clan clan) {
        return this.getAlliances().getOrDefault(clan.getName().toLowerCase(), null);
    }

    @Override
    public boolean isAllianceByClan(final Clan clan) {
        return this.getAlliances().containsKey(clan.getName().toLowerCase());
    }

    @Override
    public boolean isTrustedByClan(final Clan clan) {
        return this.isAllianceByClan(clan) && this.getAllianceByClan(clan).isTrusted();
    }

    @Override
    public String getAlliesString(final ClanManager manager, final Clan receiverClan) {
        final List<String> list = new ArrayList<>();

        for (final Alliance alliance : this.getAlliances().values()) {
            final Clan allianceClan = manager.getClanByName(alliance.getName());
            if (allianceClan == null) {
                continue;
            }

            final ClanRelation clanRelation = manager.getClanRelationByClan(receiverClan, allianceClan);

            list.add(clanRelation.getSuffix() + allianceClan.getName());
        }

        return String.join("<gray>, ", list);
    }

    @Override
    public LinkedHashMap<String, Enemy> getEnemies() {
        return this.enemies;
    }

    @Override
    public void addEnemy(final Enemy enemy) {
        this.getEnemies().put(enemy.getName().toLowerCase(), enemy);
    }

    @Override
    public void removeEnemy(final Enemy enemy) {
        this.getEnemies().remove(enemy.getName().toLowerCase());
    }

    @Override
    public Enemy getEnemyByClan(final Clan clan) {
        return this.getEnemies().getOrDefault(clan.getName().toLowerCase(), null);
    }

    @Override
    public boolean isEnemyByClan(final Clan clan) {
        return this.getEnemies().containsKey(clan.getName().toLowerCase());
    }

    @Override
    public String getEnemiesString(final ClanManager manager, final Clan receiverClan) {
        final List<String> list = new ArrayList<>();

        for (final Enemy enemy : this.getEnemies().values()) {
            final Clan enemyClan = manager.getClanByName(enemy.getName());
            if (enemyClan == null) {
                continue;
            }

            final ClanRelation clanRelation = manager.getClanRelationByClan(receiverClan, enemyClan);

            list.add(clanRelation.getSuffix() + enemyClan.getName());
        }

        return String.join("<gray>, ", list);
    }

    @Override
    public LinkedHashMap<String, Pillage> getPillages() {
        return this.pillages;
    }

    @Override
    public void addPillage(final Pillage pillage) {
        this.getPillages().put(pillage.getName().toLowerCase(), pillage);
    }

    @Override
    public void removePillage(final Pillage pillage) {
        this.getPillages().remove(pillage.getName().toLowerCase());
    }

    @Override
    public Pillage getPillageByClan(final Clan clan) {
        return this.getPillages().getOrDefault(clan.getName().toLowerCase(), null);
    }

    @Override
    public boolean isPillageByClan(final Clan clan) {
        return this.getPillages().containsKey(clan.getName().toLowerCase());
    }

    @Override
    public String getPillagesString(final ClanManager manager, final Clan receiverClan) {
        final List<String> list = new ArrayList<>();

        for (final Pillage pillage : this.getPillages().values()) {
            final Clan pillageClan = manager.getClanByName(pillage.getName());
            if (pillageClan == null) {
                continue;
            }

            final ClanRelation clanRelation = manager.getClanRelationByClan(receiverClan, pillageClan);

            list.add(clanRelation.getSuffix() + pillageClan.getName());
        }

        return String.join("<gray>, ", list);
    }

    @Override
    public boolean isBeingPillaged(final ClanManager manager) {
        for (final Clan clan : manager.getClans().values()) {
            if (!(clan.isPillageByClan(this))) {
                continue;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean isOnline() {
        return this.getMembers().values().stream().anyMatch(Member::isOnline);
    }

    @Override
    public boolean isOnline(final Player receiverPlayer) {
        return this.getMembers().values().stream().filter(Member::isOnline).anyMatch(member -> receiverPlayer.canSee(member.getOnlinePlayer()));
    }

    @Override
    public boolean isSquadFull(final ClanManager manager) {
        return this.getMembers().size() >= manager.getPrimitiveCasted(Integer.class, "Max-Squad-Count");
    }

    @Override
    public long getCreated() {
        return this.created;
    }

    @Override
    public String getCreatedString() {
        return UtilTime.getTime(System.currentTimeMillis() - this.getCreated());
    }

    @Override
    public String getTNTProtectionString(final ClanManager manager, final Player receiverPlayer) {
        if (manager.getPrimitiveCasted(Boolean.class, "SOTW")) {
            return "<green>Yes, start of the world.";
        }

        if (manager.getPrimitiveCasted(Boolean.class, "EOTW")) {
            return "<red>No, end of the world.";
        }

        if (!(this.isOnline(receiverPlayer))) {
            return "<green>Yes, members are not online.";
        }

        return "<gold>No, members are online.";
    }

    @Override
    public UUID getFounder() {
        return this.founder;
    }

    @Override
    public String getFounderString() {
        if (this.getFounder() == null) {
            return "<red>N/A";
        }

        return String.format("<yellow>%s", UtilPlayer.getPlayerNameByUUID(this.getFounder()));
    }

    @Override
    public Location getHome() {
        return this.home;
    }

    @Override
    public void setHome(final Location home) {
        this.home = home;
    }

    @Override
    public boolean hasHome() {
        return this.getHome() != null;
    }

    @Override
    public String getHomeString() {
        if (this.hasHome()) {
            return UtilLocation.locationToString(this.getHome());
        }

        return "<red>Not set";
    }
}