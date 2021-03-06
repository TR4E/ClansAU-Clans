package net.clansau.clans.clans;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.utility.UtilPlayer;
import net.clansau.core.utility.UtilTime;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Clan {

    private final Clans instance;

    private final String name;
    private final Map<UUID, ClanRole> membersMap;
    private final Map<String, Boolean> alliesMap;
    private final Map<String, Integer> enemiesMap;
    private final Map<UUID, Long> inviteeReqMap;
    private final Map<String, Long> neutralReqMap, allianceReqMap, trustReqMap, pillagingMap;
    private long created, lastOnline, lastTNTed;
    private Location home;
    private UUID founder;
    private Set<String> territory;
    private int points;

    public Clan(final Clans instance, final String name) {
        this.instance = instance;
        this.name = name;
        this.membersMap = new HashMap<>();
        this.alliesMap = new HashMap<>();
        this.enemiesMap = new HashMap<>();
        this.inviteeReqMap = new HashMap<>();
        this.neutralReqMap = new HashMap<>();
        this.allianceReqMap = new HashMap<>();
        this.trustReqMap = new HashMap<>();
        this.pillagingMap = new HashMap<>();
        this.created = System.currentTimeMillis();
        this.territory = new HashSet<>();
        this.points = 0;
    }

    protected Clans getInstance() {
        return this.instance;
    }

    private ClanManager getManager() {
        return getInstance().getManager(ClanManager.class);
    }

    public final String getName() {
        return this.name;
    }

    public final Map<UUID, ClanRole> getMembersMap() {
        return this.membersMap;
    }

    public final Map<String, Boolean> getAlliesMap() {
        return this.alliesMap;
    }

    public final Map<String, Integer> getEnemiesMap() {
        return this.enemiesMap;
    }

    public final Location getHome() {
        return this.home;
    }

    public void setHome(final Location home) {
        this.home = home;
    }

    public final Map<UUID, Long> getInviteeReqMap() {
        return this.inviteeReqMap;
    }

    public final Map<String, Long> getNeutralReqMap() {
        return this.neutralReqMap;
    }

    public final Map<String, Long> getAllianceReqMap() {
        return this.allianceReqMap;
    }

    public final Map<String, Long> getTrustReqMap() {
        return this.trustReqMap;
    }

    public final Map<String, Long> getPillagingMap() {
        return this.pillagingMap;
    }

    public final long getCreated() {
        return this.created;
    }

    public void setCreated(final long created) {
        this.created = created;
    }

    public final long getLastOnline() {
        return this.lastOnline;
    }

    public void setLastOnline(final long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public final long getLastTNTed() {
        return this.lastTNTed;
    }

    public void setLastTNTed(final long lastTNTed) {
        this.lastTNTed = lastTNTed;
    }

    public final String getAge() {
        return UtilTime.getTime(System.currentTimeMillis() - this.getCreated(), UtilTime.TimeUnit.BEST, 1);
    }

    public final UUID getFounder() {
        return this.founder;
    }

    public void setFounder(final UUID founder) {
        this.founder = founder;
    }

    public final Set<String> getTerritory() {
        return this.territory;
    }

    public void setTerritory(final Set<String> territory) {
        this.territory = territory;
    }

    public void addTerritory(final Chunk chunk) {
        this.getTerritory().add(UtilLocation.chunkToFile(chunk));
    }

    public void removeTerritory(final Chunk chunk) {
        this.getTerritory().remove(UtilLocation.chunkToFile(chunk));
    }

    public final List<Chunk> getTerritoryChunks() {
        final List<Chunk> list = new ArrayList<>();
        for (final String territory : this.getTerritory()) {
            list.add(UtilLocation.stringToChunk(territory));
        }
        return list;
    }

    public final int getPoints() {
        return this.points;
    }

    public void setPoints(final int points) {
        this.points = points;
    }

    public final Set<Player> getOnlineMembers() {
        final Set<Player> list = new HashSet<>();
        for (final UUID uuid : this.getMembersMap().keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                list.add(player);
            }
        }
        return list;
    }

    public final boolean isOnline() {
        return (this.getOnlineMembers().size() > 0);
    }

    public final boolean isMember(final UUID uuid) {
        return this.getMembersMap().containsKey(uuid);
    }

    public final boolean isNeutraled(final Clan target) {
        if (this.isAllied(target)) {
            return false;
        }
        return !(this.isEnemied(target));
    }

    public final boolean isAllied(final Clan target) {
        return (this.getAlliesMap().containsKey(target.getName()) && target.getAlliesMap().containsKey(this.getName()));
    }

    public final boolean isTrusted(final Clan target) {
        if (!(this.isAllied(target))) {
            return false;
        }
        return (this.getAlliesMap().get(target.getName()) && target.getAlliesMap().get(this.getName()));
    }

    public final boolean isEnemied(final Clan target) {
        return (this.getEnemiesMap().containsKey(target.getName()) && target.getEnemiesMap().containsKey(this.getName()));
    }

    public final boolean isPillaging(final Clan target) {
        return this.getPillagingMap().containsKey(target.getName());
    }

    public final boolean isBeingPillaged() {
        for (final Clan other : this.getInstance().getManager(ClanManager.class).getClans().values()) {
            if (other.isPillaging(this)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isBeingPillagedBy(final Clan target) {
        return target.isPillaging(this);
    }

    public final boolean canDomAvoid() {
        for (final Clan other : getInstance().getManager(ClanManager.class).getClans().values()) {
            if (!(other.isEnemied(this)) || (other.getEnemiesMap().get(this.getName()) <= 8)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public final String getMembersString() {
        final List<String> list = new ArrayList<>();
        final List<UUID> members = new ArrayList<>(this.getMembersMap().keySet());
        members.sort((o1, o2) -> this.getClanRole(o2).ordinal() - this.getClanRole(o1).ordinal());
        for (final UUID member : members) {
            final Client client = getInstance().getManager(ClientManager.class).tryGetOnlineClient(member);
            if (client == null) {
                continue;
            }
            list.add(ChatColor.YELLOW + this.getClanRolePrefix(member) + ChatColor.WHITE + "." + UtilPlayer.getOnlineStatus(member) + client.getName());
        }
        return list.stream().collect(Collectors.joining(ChatColor.WHITE + ", "));
    }

    public final String getAlliesString(final Clan clan) {
        final List<String> list = new ArrayList<>();
        final ClanManager manager = getInstance().getManager(ClanManager.class);
        for (final String clanName : this.getAlliesMap().keySet()) {
            final Clan ally = manager.getClan(clanName);
            if (ally == null) {
                continue;
            }
            list.add(manager.getClanRelation(clan, ally).getSuffix() + ally.getName());
        }
        return list.stream().collect(Collectors.joining(ChatColor.WHITE + ", "));
    }

    public final String getEnemiesString(final Clan clan) {
        final List<String> list = new ArrayList<>();
        final ClanManager manager = getInstance().getManager(ClanManager.class);
        for (final String clanName : this.getEnemiesMap().keySet()) {
            final Clan enemy = manager.getClan(clanName);
            if (enemy == null) {
                continue;
            }
            list.add(manager.getClanRelation(clan, enemy).getSuffix() + enemy.getName() + (clan != null && clan.isEnemied(enemy) ? " " + clan.getDomString(enemy) : ""));
        }
        return list.stream().collect(Collectors.joining(ChatColor.WHITE + ", "));
    }

    public final String getDomString(final Clan target) {
        return ChatColor.GRAY + "(" + ChatColor.GREEN + this.getEnemiesMap().get(target.getName()) + ChatColor.GRAY + ":" + ChatColor.RED + target.getEnemiesMap().get(this.getName()) + ChatColor.GRAY + ")";
    }

    public final String getDominanceString(final Clan clan) {
        for (final String clanName : this.getEnemiesMap().keySet()) {
            final Clan enemy = getInstance().getManager(ClanManager.class).getClan(clanName);
            if (enemy == null || !(clan.isEnemied(enemy))) {
                continue;
            }
            return clan.getDomString(enemy);
        }
        return "";
    }

    public final String getHomeString() {
        if (this.getHome() == null) {
            return ChatColor.RED + "Not set";
        }
        return UtilLocation.locToString(this.getHome());
    }

    public final String getTerritoryString() {
        if (this instanceof AdminClan) {
            return this.getTerritory().size() + "/-1";
        }
        return this.getTerritory().size() + "/" + this.getMaxClaimLimit();
    }

    public final int getMaxClaimLimit() {
        return (this.getMembersMap().size() + 3);
    }

    public final ClanRole getClanRole(final UUID uuid) {
        return this.getMembersMap().get(uuid);
    }

    public void setClanRole(final UUID uuid, final ClanRole clanRole) {
        if (!(this.isMember(uuid))) {
            return;
        }
        this.getMembersMap().put(uuid, clanRole);
    }

    public final boolean hasClanRole(final UUID uuid, final ClanRole clanRole) {
        if (!(this.isMember(uuid))) {
            return false;
        }
        return (this.getClanRole(uuid).ordinal() >= clanRole.ordinal());
    }

    public final String getClanRolePrefix(final UUID uuid) {
        return this.getClanRole(uuid).name().substring(0, 1);
    }

    public void messageClan(final String prefix, final String message, final UUID[] ignore) {
        for (final Player player : this.getOnlineMembers()) {
            if (ignore != null && Arrays.asList(ignore).contains(player.getUniqueId())) {
                continue;
            }
            UtilMessage.message(player, prefix, message);
        }
    }

    public void messageAllies(final String prefix, final String message, final UUID[] ignore) {
        for (final String ally : this.getAlliesMap().keySet()) {
            for (final Player player : getInstance().getManager(ClanManager.class).getClan(ally).getOnlineMembers()) {
                if (ignore != null && Arrays.asList(ignore).contains(player.getUniqueId())) {
                    continue;
                }
                UtilMessage.message(player, prefix, message);
            }
        }
    }

    public void soundClan(final Sound sound, final float v1, final float v2, final UUID[] ignore) {
        for (final Player player : this.getOnlineMembers()) {
            if (ignore != null && Arrays.asList(ignore).contains(player.getUniqueId())) {
                continue;
            }
            player.playSound(player.getLocation(), sound, v1, v2);
        }
    }

    public final long getTNTProtected() {
        final long z = ((this.getLastOnline() + (getManager().getClanModule().getPrimitiveCasted(Integer.class, "TNTProtection") * 60000L)) - System.currentTimeMillis());
        System.out.println(z);
        return z;
    }

    public final boolean isTNTProtected() {
        if (this instanceof AdminClan) {
            return true;
        }
        if (getManager().getClanModule().getPrimitiveCasted(Boolean.class, "LastDay")) {
            return false;
        }
        if (this.isOnline()) {
            return false;
        }
        return (this.getTNTProtected() < 0L);
    }

    public final String getTNTProtectionString() {
        if (!(this instanceof AdminClan)) {
            if (getManager().getClanModule().getPrimitiveCasted(Boolean.class, "LastDay")) {
                return ChatColor.GOLD + "LAST DAY OF MAP - NO PROTECTION.";
            }
            if (this.isOnline()) {
                return ChatColor.GOLD + "No, clan members are online.";
            }
            if (this.getTNTProtected() > 0L) {
                return ChatColor.GOLD + "No, " + UtilTime.getTime(this.getTNTProtected(), UtilTime.TimeUnit.BEST, 1) + " until protection.";
            }
        }
        return ChatColor.GREEN + "Yes, TNT protected";
    }
}