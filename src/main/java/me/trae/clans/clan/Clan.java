package me.trae.clans.clan;

import me.trae.clans.Clans;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.enums.RequestType;
import me.trae.clans.clan.interfaces.IClan;
import me.trae.clans.clan.types.AdminClan;
import me.trae.core.database.containers.DataContainer;
import me.trae.core.database.query.constants.DefaultProperty;
import me.trae.core.utility.*;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Clan implements IClan, DataContainer<ClanProperty> {

    private final String name;

    private final List<String> territory = new ArrayList<>();

    private final LinkedHashMap<RequestType, LinkedHashMap<String, Long>> requests = new LinkedHashMap<>();

    private final LinkedHashMap<UUID, Member> members = new LinkedHashMap<>();
    private final LinkedHashMap<String, Alliance> alliances = new LinkedHashMap<>();
    private final LinkedHashMap<String, Enemy> enemies = new LinkedHashMap<>();
    private final LinkedHashMap<String, Pillage> pillages = new LinkedHashMap<>();

    private long created, lastOnline, lastTNTed;
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

    public Clan(final EnumData<ClanProperty> data) {
        this(data.get(String.class, DefaultProperty.KEY));

        data.getList(String.class, ClanProperty.TERRITORY).forEach(string -> UtilServer.runTask(Clans.class, false, () -> this.addTerritory(UtilChunk.fileToChunk(string))));

        data.getList(String.class, ClanProperty.REQUESTS).forEach(string -> {
            final String[] tokens = string.split(":");

            final RequestType requestType = RequestType.valueOf(tokens[0]);

            if (!(this.getRequests().containsKey(requestType))) {
                this.getRequests().put(requestType, new LinkedHashMap<>());
            }

            this.getRequests().get(requestType).put(tokens[1], Long.parseLong(tokens[2]));
        });

        data.getList(String.class, ClanProperty.MEMBERS).forEach(string -> this.addMember(new Member(string.split(":"))));
        data.getList(String.class, ClanProperty.ALLIANCES).forEach(string -> this.addAlliance(new Alliance(string.split(":"))));
        data.getList(String.class, ClanProperty.ENEMIES).forEach(string -> this.addEnemy(new Enemy(string.split(":"))));
        data.getList(String.class, ClanProperty.PILLAGES).forEach(string -> this.addPillage(new Pillage(string.split(":"))));

        this.created = data.get(Long.class, ClanProperty.CREATED);
        this.lastOnline = data.get(Long.class, ClanProperty.LAST_ONLINE);
        this.lastTNTed = data.get(Long.class, ClanProperty.LAST_TNTED);
        this.founder = UUID.fromString(data.get(String.class, ClanProperty.FOUNDER));
        this.home = UtilLocation.fileToLocation(data.get(String.class, ClanProperty.HOME));
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
    public boolean isTerritoryByChunk(final Chunk chunk) {
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
    public LinkedHashMap<RequestType, LinkedHashMap<String, Long>> getRequests() {
        return this.requests;
    }

    @Override
    public void addRequest(final RequestType requestType, final String key) {
        if (!(this.getRequests().containsKey(requestType))) {
            this.getRequests().put(requestType, new LinkedHashMap<>());
        }

        this.getRequests().get(requestType).put(key, System.currentTimeMillis());
    }

    @Override
    public void removeRequest(final RequestType requestType, final String key) {
        this.getRequests().getOrDefault(requestType, new LinkedHashMap<>()).remove(key);
    }

    @Override
    public boolean isRequested(final RequestType requestType, final String key) {
        return this.getRequests().getOrDefault(requestType, new LinkedHashMap<>()).containsKey(key);
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
    public LinkedHashMap<Player, Member> getOnlineMembers() {
        final LinkedHashMap<Player, Member> map = new LinkedHashMap<>();

        for (final Member member : this.getMembers().values()) {
            if (!(member.isOnline())) {
                continue;
            }

            map.put(member.getOnlinePlayer(), member);
        }

        return map;
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
    public boolean isNeutralByClan(final Clan clan) {
        if (clan != null) {
            if (this == clan) {
                return false;
            }

            if (this.isAllianceByClan(clan)) {
                return false;
            }

            if (this.isEnemyByClan(clan)) {
                return false;
            }

            if (this.isPillageByClan(clan) || clan.isPillageByClan(this)) {
                return false;
            }
        }

        return true;
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
        int size = this.getMembers().size();

        for (final Alliance alliance : this.getAlliances().values()) {
            final Clan allianceClan = manager.getClanByName(alliance.getName());
            if (allianceClan == null) {
                continue;
            }

            size += allianceClan.getMembers().size();
        }

        return size >= manager.getPrimitiveCasted(Integer.class, "Max-Squad-Count");
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
    public long getLastOnline() {
        return this.lastOnline;
    }

    @Override
    public void setLastOnline(final long lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Override
    public long getLastTNTed() {
        return this.lastTNTed;
    }

    @Override
    public void setLastTNTed(final long lastTNTed) {
        this.lastTNTed = lastTNTed;
    }

    @Override
    public boolean isTNTProtected(final ClanManager manager) {
        if (this.isAdmin()) {
            return true;
        }

        final String tntProtectionString = this.getTNTProtectionString(manager, null);

        return tntProtectionString.contains("Yes");
    }

    @Override
    public String getTNTProtectionString(final ClanManager manager, final Player receiverPlayer) {
        if (manager.getPrimitiveCasted(Boolean.class, "SOTW")) {
            return "<green>Yes, start of the world.";
        }

        if (manager.getPrimitiveCasted(Boolean.class, "EOTW")) {
            return "<red>No, end of the world.";
        }

        if (receiverPlayer != null ? this.isOnline(receiverPlayer) : this.isOnline()) {
            return "<gold>No, members are online.";
        }

        final long tntProtectionDuration = manager.getPrimitiveCasted(Long.class, "TNT-Protection-Duration");

        if (this.getLastTNTed() > 0L && !(UtilTime.elapsed(this.getLastTNTed(), tntProtectionDuration))) {
            return String.format("<green>Yes, %s until no protection.", UtilTime.getTime(UtilTime.getRemaining(this.getLastOnline(), tntProtectionDuration)));
        }

        if (this.getLastOnline() > 0L && UtilTime.elapsed(this.getLastOnline(), tntProtectionDuration)) {
            return String.format("<gold>No, %s until protection.", UtilTime.getTime(UtilTime.getRemaining(this.getLastOnline(), tntProtectionDuration)));
        }

        return "<green>Yes, TNT protected.";
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

    @Override
    public List<ClanProperty> getProperties() {
        return Arrays.asList(ClanProperty.values());
    }

    @Override
    public Object getValueByProperty(final ClanProperty property) {
        switch (property) {
            case TERRITORY:
                return this.getTerritory();
            case REQUESTS:
                return this.getRequests().entrySet().stream().map(entry -> entry.getValue().entrySet().stream().map(entry2 -> String.format("%s:%s:%s", entry.getKey().name(), entry2.getKey(), entry2.getValue()))).collect(Collectors.toList());
            case MEMBERS:
                return this.getMembers().values().stream().map(Member::toString).collect(Collectors.toList());
            case ALLIANCES:
                return this.getAlliances().values().stream().map(Alliance::toString).collect(Collectors.toList());
            case ENEMIES:
                return this.getEnemies().values().stream().map(Enemy::toString).collect(Collectors.toList());
            case PILLAGES:
                return this.getPillages().values().stream().map(Pillage::toString).collect(Collectors.toList());
            case CREATED:
                return this.getCreated();
            case LAST_ONLINE:
                return this.getLastOnline();
            case LAST_TNTED:
                return this.getLastTNTed();
            case FOUNDER:
                return this.getFounder().toString();
            case HOME:
                return UtilLocation.locationToFile(this.getHome());
            case ADMIN:
                return this.isAdmin();
            case SAFE:
                return this.isAdmin() && UtilJava.cast(AdminClan.class, this).isSafe();
        }

        return null;
    }
}