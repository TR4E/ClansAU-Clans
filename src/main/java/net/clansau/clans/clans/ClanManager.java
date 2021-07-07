package net.clansau.clans.clans;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.commands.ClanCommand;
import net.clansau.clans.clans.commands.chat.AllyChatCommand;
import net.clansau.clans.clans.commands.chat.ClanChatCommand;
import net.clansau.clans.clans.commands.subcommands.*;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.clans.clans.listeners.*;
import net.clansau.clans.config.OptionsManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.Manager;
import net.clansau.core.framework.blockrestore.BlockRestoreData;
import net.clansau.core.framework.blockrestore.BlockRestoreManager;
import net.clansau.core.general.combat.CombatManager;
import net.clansau.core.utility.TitleManager;
import net.clansau.core.utility.UtilBlock;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
        addModule(new ConnectionListener(this));
        addModule(new DeathListener(this));
        addModule(new DisplayTerritoryOwner(this));
        addModule(new ExplosionListener(this));
        addModule(new HandleDominance(this));
        addModule(new PillageDisableBlockBreak(this));
        addModule(new PillageDisableBlockPlace(this));
        addModule(new PillageDisableItemDespawn(this));
        addModule(new PillageListener(this));

        addModule(new ClanCommand(this));
        addModule(new ClanChatCommand(this));
        addModule(new AllyChatCommand(this));
        this.registerCommands();
    }

    private void registerCommands() {
        addModule(new ListCommand(this));
        addModule(new TopCommand(this));
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
        addModule(new ClaimCommand(this));
        addModule(new UnClaimCommand(this));
        addModule(new UnClaimAllCommand(this));
        addModule(new SetHomeCommand(this));
        addModule(new HomeCommand(this));
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
        for (final Chunk chunk : clan.getTerritoryChunks()) {
            this.unOutlineChunk(chunk);
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

    public final boolean isSafe(final Location location) {
        final Clan land = this.getClan(location);
        if (land == null) {
            return false;
        }
        if (land instanceof AdminClan && ((AdminClan) land).isSafe()) {
            if (land.getName().toLowerCase().contains("spawn")) {
                return location.getY() >= 100.0D;
            }
            return true;
        }
        return false;
    }

    public final boolean isSafe(final Player player) {
        return (this.isSafe(player.getLocation()) && !(getInstance().getManager(CombatManager.class).isInCombat(player)));
    }

    public final boolean canCast(final Location location) {
        return !(this.isSafe(location));
    }

    public final boolean canCast(final Player player) {
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return false;
        }
        if (client.isAdministrating()) {
            return true;
        }
        if (!(this.canCast(player.getLocation()))) {
            UtilMessage.message(player, "Restrictions", "You are not allowed to cast abilities in safe zones!");
            return false;
        }
        return true;
    }

    public final boolean canHurt(final Player damager, final Player damagee) {
        if (damager.equals(damagee)) {
            return false;
        }
        if (damagee.getGameMode().equals(GameMode.CREATIVE) || damagee.getGameMode().equals(GameMode.SPECTATOR) || ((CraftPlayer) damagee).getHandle().isSpectator()) {
            return false;
        }
        final Clan damagerClan = this.getClan(damager.getUniqueId());
        final Clan damageeClan = this.getClan(damagee.getUniqueId());
        if (damagerClan != null && damageeClan != null) {
            if (damagerClan.equals(damageeClan) || damagerClan.isAllied(damageeClan)) {
                return false;
            }
        }
        return (!(this.isSafe(damager) && this.isSafe(damagee)));
    }

    public final boolean hasAccess(final Player player, final Location location) {
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return false;
        }
        if (client.isAdministrating()) {
            return true;
        }
        final Clan clan = this.getClan(player.getUniqueId());
        final Clan land = this.getClan(location);
        final ClanRelation relation = this.getClanRelation(clan, land);
        return (land == null || relation.equals(ClanRelation.SELF) || relation.equals(ClanRelation.TRUSTED_ALLY) || relation.equals(ClanRelation.PILLAGE));
    }

    public void displayOwner(final Player player, final Location location) {
        String append = "";
        final Clan clan = this.getClan(player.getUniqueId());
        final Clan land = this.getClan(location);
        if (land != null) {
            if (land instanceof AdminClan) {
                if (((AdminClan) land).isSafe()) {
                    append = ChatColor.GRAY + " (" + ChatColor.AQUA + "Safe" + ChatColor.GRAY + ")";
                } else if (land.getName().equalsIgnoreCase("fields")) {
                    append = ChatColor.GRAY + " (" + ChatColor.RED + ChatColor.BOLD + "PvP Hotspot" + ChatColor.GRAY + ")";
                }
            } else {
                if (clan != null) {
                    if (clan.isTrusted(land)) {
                        append = ChatColor.GRAY + " (" + ChatColor.YELLOW + "Trusted" + ChatColor.GRAY + ")";
                    } else if (clan.isEnemied(land)) {
                        append = " " + clan.getDomString(land);
                    }
                }
            }
        }
        final String name = (land != null ? (this.getClanRelation(clan, land).getSuffix() + this.getName(land, false) + append) : ChatColor.YELLOW + "Wilderness");
        getInstance().getManager(TitleManager.class).sendPlayer(player, " ", name, 2);
        UtilMessage.message(player, "Territory", name);
    }
}