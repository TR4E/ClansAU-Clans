package net.clansau.clans.clans;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.commands.ClanCommand;
import net.clansau.clans.clans.commands.subcommands.*;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.clans.clans.listeners.ChatListener;
import net.clansau.clans.config.OptionsManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.Manager;
import net.clansau.core.framework.blockrestore.BlockRestoreData;
import net.clansau.core.framework.blockrestore.BlockRestoreManager;
import net.clansau.core.utility.UtilBlock;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        addModule(new ChatListener(this));

        addModule(new ClanCommand(this));
        this.registerCommands();
    }

    private void registerCommands() {
        addModule(new ListCommand(this));
        addModule(new CreateCommand(this));
        addModule(new DisbandCommand(this));
        addModule(new InviteCommand(this));
        addModule(new JoinCommand(this));
        addModule(new LeaveCommand(this));
        addModule(new KickCommand(this));
        addModule(new PromoteCommand(this));
        addModule(new DemoteCommand(this));
        addModule(new NeutralCommand(this));
        addModule(new AllyCommand(this));
        addModule(new TrustCommand(this));
        addModule(new EnemyCommand(this));
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
        final Client client = getInstance().getManager(ClientManager.class).searchClient(sender, name, false);
        if (client != null) {
            final Clan clan = this.getClan(client.getUUID());
            if (clan != null) {
                list.add(clan);
            }
        }
        if (list.size() == 1) {
            return list.get(0);
        } else if (inform) {
            UtilMessage.message(sender, "Clan Search", ChatColor.YELLOW.toString() + list.size() + ChatColor.GRAY + " matches found [" + (list.size() == 0 ? ChatColor.YELLOW + name : list.stream().map(c -> (sender instanceof Player ? this.getClanRelation(this.getClan(((Player) sender).getUniqueId()), c).getSuffix() : ChatColor.YELLOW) + c.getName()).collect(Collectors.joining(ChatColor.GRAY + ", "))) + ChatColor.GRAY + "].");
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

    public void disbandClan(final Clan clan) {
        for (final String territory : clan.getTerritory()) {
            this.unOutlineChunk(UtilLocation.stringToChunk(territory));
        }
        for (final String a : clan.getAlliesMap().keySet()) {
            final Clan ally = this.getClan(a);
            ally.getAlliesMap().remove(clan.getName());
            getRepository().updateAllies(ally);
        }
        for (final String e : clan.getEnemiesMap().keySet()) {
            final Clan enemy = this.getClan(e);
            enemy.getEnemiesMap().remove(clan.getName());
            getRepository().updateEnemies(enemy);
        }
        for (final String p : clan.getPillagingMap().keySet()) {
            final Clan pillager = this.getClan(p);
            pillager.getPillagingMap().remove(clan.getName());
        }
        this.removeClan(clan);
        getRepository().deleteClan(clan);
    }

    public void outlineChunk(final Chunk chunk, final Material material, final long expire) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (z == 15 || z == 0 || x == 15 | x == 0) {
                    final Block block = UtilBlock.getBlockUnder(chunk.getWorld().getHighestBlockAt(chunk.getBlock(x, 0, z).getLocation()).getLocation());
                    if (block == null) {
                        continue;
                    }
                    if (UtilBlock.isUsable(block.getType())) {
                        continue;
                    }
                    if (expire > 0L) {
                        new BlockRestoreData(getInstance(), block, material, (byte) 0, expire, null);
                    } else {
                        block.setType(material);
                    }
                }
            }
        }
    }

    public void unOutlineChunk(final Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (z == 15 || z == 0 || x == 15 || x == 0) {
                    final Block block = UtilBlock.getBlockUnder(chunk.getWorld().getHighestBlockAt(chunk.getBlock(x, 0, z).getLocation()).getLocation());
                    if (block == null) {
                        continue;
                    }
                    final BlockRestoreData restoreData = getInstance().getManager(BlockRestoreManager.class).getRestoreBlock(block);
                    if (restoreData == null) {
                        continue;
                    }
                    restoreData.restore();
                }
            }
        }
    }

    public final long getMaxPillageLength() {
        return (getInstance().getManager(OptionsManager.class).getClansPillageLength() * 60000L);
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

    public final String getName(final Clan clan, final boolean fullname) {
        if (clan instanceof AdminClan) {
            if (clan.getName().equalsIgnoreCase("Outskirts")) {
                return ChatColor.YELLOW + "Outskirts";
            }
            if (fullname) {
                return "Admin Clan " + clan.getName();
            }
            return ChatColor.WHITE + clan.getName();
        }
        if (fullname) {
            return "Clan " + clan.getName();
        }
        return clan.getName();
    }

    public void removeClanChat(final UUID uuid) {
//        final Gamer gamer = getInstance().getManager(GamerManager.class).getGamer(uuid);
//        if (gamer != null && (gamer.getChatType().equals(Gamer.ChatType.CLAN_CHAT) || gamer.getChatType().equals(Gamer.ChatType.ALLY_CHAT))) {
//            gamer.setChatType(Gamer.ChatType.GLOBAL_CHAT);
//        }
    }
}