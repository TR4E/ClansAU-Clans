package net.clansau.clans.clans;

import net.clansau.clans.Clans;
import net.clansau.clans.config.OptionsManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.Manager;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ClanManager extends Manager {

    private final Map<String, Clan> clans;

    public ClanManager(final Clans instance) {
        super(instance, "Clan Manager");
        this.clans = new HashMap<>();
    }

    @Override
    protected void registerModules() {
    }

    public final ClanRepository getRepository() {
        return getInstance().getManager(ClanRepository.class);
    }

    public final Map<String, Clan> getClans() {
        return this.clans;
    }

    public void addClan(final Clan clan) {
        this.getClans().put(clan.getName(), clan);
    }

    public void removeClan(final Clan clan) {
        this.getClans().remove(clan.getName());
    }

    public final boolean isClan(final String name) {
        return this.getClans().containsKey(name);
    }

    public final Clan getClan(final String name) {
        return this.getClans().get(name);
    }

    public final Clan getClan(final UUID uuid) {
        for (final Clan clan : this.getClans().values()) {
            if (clan.getMembersMap().containsKey(uuid)) {
                return clan;
            }
        }
        return null;
    }

    public final Clan getClan(final Chunk chunk) {
        for (final Clan clan : this.getClans().values()) {
            if (clan.getTerritory().contains(UtilLocation.chunkToFile(chunk))) {
                return clan;
            }
        }
        return null;
    }

    public final Clan getClan(final Location location) {
        return this.getClan(location.getChunk());
    }

    public final Clan searchClan(final CommandSender sender, final String name, final boolean inform) {
        final List<Clan> list = new ArrayList<>();
        for (final Clan clan : this.getClans().values()) {
            if (clan.getName().equalsIgnoreCase(name)) {
                return clan;
            }
            if (clan.getName().toLowerCase().contains(name.toLowerCase())) {
                list.add(clan);
            }
        }
        if (list.size() == 1) {
            return list.get(0);
        } else if (inform) {
            UtilMessage.message(sender, "Clan Search", ChatColor.YELLOW.toString() + list.size() + ChatColor.GRAY + " matches found [" + (list.size() == 0 ? ChatColor.YELLOW + name : list.stream().map(c -> (sender instanceof Player ? this.getClanRelation(this.getClan(((Player) sender).getUniqueId()), c).getSuffix() : ChatColor.YELLOW) + c.getName()).collect(Collectors.joining(ChatColor.GRAY + ", ")) + "]."));
        }
        return null;
    }

    public final Client searchMember(final Player player, final String name, final boolean inform) {
        final Clan clan = this.getClan(player.getUniqueId());
        if (clan == null) {
            return null;
        }
        final List<Client> list = new ArrayList<>();
        for (final UUID uuid : clan.getMembersMap().keySet()) {
            final Client client = getInstance().getManager(ClientManager.class).tryGetOnlineClient(uuid);
            if (client != null) {
                if (client.getName().equalsIgnoreCase(name)) {
                    return client;
                }
                if (client.getName().toLowerCase().contains(name.toLowerCase())) {
                    list.add(client);
                }
            }
        }
        if (list.size() == 1) {
            return list.get(0);
        } else if (inform) {
            UtilMessage.message(player, "Member Search", ChatColor.YELLOW.toString() + list.size() + ChatColor.GRAY + " matches found [" + (list.size() == 0 ? ChatColor.YELLOW + name : list.stream().map(c -> ChatColor.AQUA + c.getName()).collect(Collectors.toList())) + ChatColor.GRAY + "].");
        }
        return null;
    }

    public final long getMaxPillageLength() {
        return (getInstance().getManager(OptionsManager.class).getPillageLength() * 60000L);
    }

    public final boolean isNameAllowed(final String name) {
        final String[] blacklisted = {"sethome", "promote", "demote",
                "admin", "help", "create", "rename", "disband", "delete", "invite", "join",
                "kick", "ally", "trust", "enemy", "claim", "unclaim", "list", "territory",
                "home", "spawn", "redspawn", "bluespawn", "fields", "lake", "shops", "redshops", "blueshops", "outskirts", "leave", "top",
                "unclaimall", "map", "home", "neutral", "energy", "wilderness"};
        return !(Arrays.asList(blacklisted).contains(name.toLowerCase()));
    }

    public final ClanRelation getClanRelation(final Clan clan, final Clan target) {
        if (clan == null || target == null) {
            return ClanRelation.NEUTRAL;
        }
        if (clan.equals(target)) {
            return ClanRelation.SELF;
        }
        if (clan instanceof AdminClan || target instanceof AdminClan) {
            return ClanRelation.ADMIN;
        }
        if (clan.isAllied(target)) {
            if (clan.isTrusted(target)) {
                return ClanRelation.TRUSTED_ALLY;
            }
            return ClanRelation.ALLY;
        }
        if (clan.isEnemied(target)) {
            return ClanRelation.ENEMY;
        }
        if (clan.isPillaging(target) || target.isPillaging(clan)) {
            return ClanRelation.PILLAGE;
        }
        return ClanRelation.NEUTRAL;
    }
}