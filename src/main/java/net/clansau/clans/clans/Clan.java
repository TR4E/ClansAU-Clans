package net.clansau.clans.clans;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.utility.UtilPlayer;
import net.clansau.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
    private long created;
    private Location home;
    private UUID founder;
    private Set<String> territory;

    public Clan(final Clans instance, final String name) {
        this.instance = instance;
        this.name = name;
        this.created = System.currentTimeMillis();
        this.membersMap = new HashMap<>();
        this.alliesMap = new HashMap<>();
        this.enemiesMap = new HashMap<>();
        this.territory = new HashSet<>();
        this.inviteeReqMap = new HashMap<>();
        this.neutralReqMap = new HashMap<>();
        this.allianceReqMap = new HashMap<>();
        this.trustReqMap = new HashMap<>();
        this.pillagingMap = new HashMap<>();
    }

    protected Clans getInstance() {
        return this.instance;
    }

    public final String getName() {
        return this.name;
    }

    public final long getCreated() {
        return this.created;
    }

    public void setCreated(final long created) {
        this.created = created;
    }

    public final String getAge() {
        return UtilTime.getTime(System.currentTimeMillis() - this.getCreated(), UtilTime.TimeUnit.BEST, 1);
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
        for (final Map.Entry<UUID, ClanRole> entry : this.getMembersMap().entrySet()) {
            final Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null) {
                continue;
            }
            final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
            if (client == null) {
                continue;
            }
            list.sort(Comparator.comparing(s -> this.getClanRole(client.getUUID())));
            list.add(ChatColor.YELLOW + this.getClanRolePrefix(client.getUUID()) + ChatColor.WHITE + "." + UtilPlayer.getOnlineStatus(client.getUUID()) + client.getName());
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

    public final String getTNTProtection() {
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
}