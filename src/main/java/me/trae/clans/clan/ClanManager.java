package me.trae.clans.clan;

import me.trae.clans.Clans;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.MassClaimCommand;
import me.trae.clans.clan.commands.chat.AllyChatCommand;
import me.trae.clans.clan.commands.chat.ClanChatCommand;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.AccessType;
import me.trae.clans.clan.enums.ChatType;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.interfaces.IClanManager;
import me.trae.clans.clan.modules.HandleClanLastOnlineOnPlayerQuit;
import me.trae.clans.clan.modules.HandleClanUpdater;
import me.trae.clans.clan.modules.champions.HandleOverpoweredKits;
import me.trae.clans.clan.modules.chat.HandleChatReceiver;
import me.trae.clans.clan.modules.chat.HandleClansPlayerDisplayNameFormat;
import me.trae.clans.clan.modules.pillage.HandleDominancePointsOnPlayerDeath;
import me.trae.clans.clan.modules.pillage.HandlePillageAlerts;
import me.trae.clans.clan.modules.pillage.HandlePillageUpdater;
import me.trae.clans.clan.modules.scoreboard.HandleClansScoreboardSetup;
import me.trae.clans.clan.modules.scoreboard.HandleClansScoreboardUpdate;
import me.trae.clans.clan.modules.territory.*;
import me.trae.clans.clan.types.AdminClan;
import me.trae.core.Core;
import me.trae.core.blockrestore.BlockRestoreManager;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.database.repository.containers.RepositoryContainer;
import me.trae.core.framework.SpigotManager;
import me.trae.core.gamer.Gamer;
import me.trae.core.gamer.GamerManager;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.objects.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClanManager extends SpigotManager<Clans> implements IClanManager, RepositoryContainer<ClanRepository> {

    private final Map<String, Clan> CLANS = new HashMap<>();

    @ConfigInject(type = Long.class, path = "Chunk-Outline-Duration", defaultValue = "300_000")
    private long chunkOutlineDuration;

    @ConfigInject(type = Long.class, path = "Pillage-Length", defaultValue = "600_000")
    public long pillageLength;

    @ConfigInject(type = Integer.class, path = "Required-Pillage-Points", defaultValue = "16")
    public int requiredPillagePoints;

    @ConfigInject(type = Integer.class, path = "Max-Squad-Count", defaultValue = "8")
    public int maxSquadCount;

    @ConfigInject(type = Integer.class, path = "Max-Claim-Limit", defaultValue = "8")
    public int maxClaimLimit;

    @ConfigInject(type = Long.class, path = "TNT-Protection-Duration", defaultValue = "1_800_000")
    public long tntProtectionDuration;

    @ConfigInject(type = Boolean.class, path = "SOTW", defaultValue = "false")
    public boolean sotw;

    @ConfigInject(type = Boolean.class, path = "EOTW", defaultValue = "false")
    public boolean eotw;

    public ClanManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new ClanCommand(this));
        addModule(new MassClaimCommand(this));

        // Chat Commands
        addModule(new AllyChatCommand(this));
        addModule(new ClanChatCommand(this));

        // Champions Modules
        addModule(new HandleOverpoweredKits(this));

        // Chat Modules
        addModule(new HandleChatReceiver(this));
        addModule(new HandleClansPlayerDisplayNameFormat(this));

        // Pillage Modules
        addModule(new HandleDominancePointsOnPlayerDeath(this));
        addModule(new HandlePillageAlerts(this));
        addModule(new HandlePillageUpdater(this));

        // Scoreboard Modules
        addModule(new HandleClansScoreboardSetup(this));
        addModule(new HandleClansScoreboardUpdate(this));

        // Territory Modules
        addModule(new DisplayTerritoryOwner(this));
        addModule(new HandleClanTerritoryBlockBreak(this));
        addModule(new HandleClanTerritoryBlockInteract(this));
        addModule(new HandleClanTerritoryBlockPlace(this));
        addModule(new HandleClanTerritoryDoorInteract(this));

        // Modules
        addModule(new HandleClanLastOnlineOnPlayerQuit(this));
        addModule(new HandleClanUpdater(this));
    }

    @Override
    public Class<ClanRepository> getClassOfRepository() {
        return ClanRepository.class;
    }

    @Override
    public Map<String, Clan> getClans() {
        return this.CLANS;
    }

    @Override
    public void addClan(final Clan clan) {
        this.getClans().put(clan.getName().toLowerCase(), clan);
    }

    @Override
    public void removeClan(final Clan clan) {
        this.getClans().remove(clan.getName().toLowerCase());
    }

    @Override
    public Clan getClanByName(final String name) {
        return this.getClans().getOrDefault(name.toLowerCase(), null);
    }

    @Override
    public Clan getClanByUUID(final UUID uuid) {
        for (final Clan clan : this.getClans().values()) {
            if (!(clan.isMemberByUUID(uuid))) {
                continue;
            }

            return clan;
        }

        return null;
    }

    @Override
    public Clan getClanByPlayer(final Player player) {
        return this.getClanByUUID(player.getUniqueId());
    }

    @Override
    public Clan getClanByChunk(final Chunk chunk) {
        for (final Clan clan : this.getClans().values()) {
            if (!(clan.isTerritoryByChunk(chunk))) {
                continue;
            }

            return clan;
        }

        return null;
    }

    @Override
    public Clan getClanByLocation(final Location location) {
        return this.getClanByChunk(location.getChunk());
    }

    @Override
    public boolean isClanByName(final String name) {
        return this.getClans().containsKey(name.toLowerCase());
    }

    @Override
    public boolean isClanByUUID(final UUID uuid) {
        return this.getClanByUUID(uuid) != null;
    }

    @Override
    public boolean isClanByPlayer(final Player player) {
        return this.isClanByUUID(player.getUniqueId());
    }

    @Override
    public boolean isClanByChunk(final Chunk chunk) {
        return this.getClanByChunk(chunk) != null;
    }

    @Override
    public boolean isClanByLocation(final Location location) {
        return this.getClanByLocation(location) != null;
    }

    @Override
    public List<Clan> getNearbyClans(final Chunk chunk, final int distance) {
        final List<Clan> list = new ArrayList<>();

        for (final Chunk nearbyChunk : UtilChunk.getNearbyChunks(chunk, distance)) {
            final Clan territoryClan = this.getClanByChunk(nearbyChunk);
            if (territoryClan == null) {
                continue;
            }

            list.add(territoryClan);
        }

        return list;
    }

    @Override
    public Clan searchClan(final CommandSender sender, final String name, final boolean inform) {
        final List<Predicate<Clan>> predicates = Arrays.asList(
                (clan -> clan.getName().equalsIgnoreCase(name)),
                (clan -> clan.getName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<Clan, String> function = (clan -> {
            ClanRelation clanRelation = ClanRelation.NEUTRAL;

            if (sender instanceof Player) {
                clanRelation = this.getClanRelationByClan(this.getClanByPlayer(UtilJava.cast(Player.class, sender)), clan);
            }

            return this.getClanShortName(clan, clanRelation);
        });

        final Consumer<List<Clan>> consumer = (list -> {
            if (!(sender instanceof Player)) {
                return;
            }

            final Client client = this.getInstance(Core.class).getManagerByClass(ClientManager.class).searchClient(sender, name, false);
            if (client == null) {
                return;
            }

            final Clan clientClan = this.getClanByUUID(client.getUUID());
            if (clientClan == null) {
                return;
            }

            if (list.contains(clientClan)) {
                return;
            }

            list.add(clientClan);
        });

        return UtilJava.search(this.getClans().values(), predicates, consumer, function, "Clan Search", sender, name, inform);
    }

    @Override
    public Client searchMember(final CommandSender sender, final Clan clan, final String name, final boolean inform) {
        final ClientManager clientManager = this.getInstance(Core.class).getManagerByClass(ClientManager.class);

        final List<Client> list = clan.getMembers().values().stream().map(member -> clientManager.getClientByUUID(member.getUUID())).collect(Collectors.toList());

        final List<Predicate<Client>> predicates = Arrays.asList(
                (client -> client.getName().equalsIgnoreCase(name)),
                (client -> client.getName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<Client, String> function = (client -> ClanRelation.SELF.getSuffix() + client.getName());

        return UtilJava.search(list, predicates, null, function, "Member Search", sender, name, inform);
    }

    @Override
    public void messageClan(final Clan clan, final String prefix, final String message, final List<String> variables, final List<UUID> ignore) {
        UtilMessage.simpleMessage(clan.getOnlineMembers().keySet(), prefix, message, variables, null, ignore);
    }

    @Override
    public void messageAllies(final Clan clan, final String prefix, final String message, final List<String> variables, final List<UUID> ignore) {
        for (final Alliance alliance : clan.getAlliances().values()) {
            final Clan allianceClan = this.getClanByName(alliance.getName());
            if (allianceClan == null) {
                continue;
            }

            this.messageClan(allianceClan, prefix, message, variables, ignore);
        }
    }

    @Override
    public ClanRelation getClanRelationByClan(final Clan clan, final Clan target) {
        if (clan != null && target != null) {
            if (clan == target) {
                return ClanRelation.SELF;
            }

            if (clan.isAllianceByClan(target)) {
                if (clan.isTrustedByClan(target)) {
                    return ClanRelation.TRUSTED_ALLIANCE;
                }

                return ClanRelation.ALLIANCE;
            }

            if (clan.isEnemyByClan(target)) {
                return ClanRelation.ENEMY;
            }

            if (clan.isPillageByClan(target) || target.isPillageByClan(clan)) {
                return ClanRelation.PILLAGE;
            }
        }

        return ClanRelation.NEUTRAL;
    }

    @Override
    public ClanRelation getClanRelationByPlayer(final Player player, final Player target) {
        return this.getClanRelationByClan(this.getClanByPlayer(player), this.getClanByPlayer(target));
    }

    @Override
    public LinkedHashMap<String, String> getClanInformation(final Player player, final Client client, final Clan playerClan, final Clan targetClan) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();

        if (client.isAdministrating()) {
            map.put("Founder", targetClan.getFounderString());
        }

        map.put("Age", String.format("<yellow>%s", targetClan.getCreatedString()));
        map.put("Territory", String.format("<yellow>%s", targetClan.getTerritoryString(this)));

        if (playerClan == targetClan || client.isAdministrating()) {
            map.put("Home", targetClan.getHomeString());
        }

        map.put("Allies", targetClan.getAlliesString(this, playerClan));
        map.put("Enemies", targetClan.getEnemiesString(this, playerClan));
        map.put("Pillages", targetClan.getPillagesString(this, playerClan));

        map.put("Members", targetClan.getMembersString(player));

        if (!(targetClan.isAdmin())) {
            map.put("TNT Protected", targetClan.getTNTProtectionString(this, player));
        }

        return map;
    }

    @Override
    public String getClanFullName(final Clan clan, final ClanRelation clanRelation) {
        return clanRelation.getSuffix() + String.format("%s %s", clan.getType(), clan.getDisplayName());
    }

    @Override
    public String getClanShortName(final Clan clan, final ClanRelation clanRelation) {
        return clanRelation.getSuffix() + clan.getDisplayName();
    }

    @Override
    public String getClanName(final Clan clan, final ClanRelation clanRelation) {
        if (clan.isAdmin()) {
            ChatColor chatColor = ChatColor.WHITE;

            if (clan.getName().equals("Outskirts")) {
                chatColor = ChatColor.YELLOW;
            }

            return chatColor + clan.getName();
        }

        return this.getClanFullName(clan, clanRelation);
    }

    @Override
    public String getTerritoryClanNameForChat(final Clan playerClan, final Clan territoryClan, final Location location) {
        String name = "Wilderness";
        ChatColor chatColor = ChatColor.YELLOW;
        String description = "";

        if (territoryClan != null) {
            name = territoryClan.getDisplayName();
            chatColor = this.getClanRelationByClan(playerClan, territoryClan).getSuffix();

            if (playerClan != null) {
                if (territoryClan.isTrustedByClan(playerClan)) {
                    description = "<yellow>Trusted";
                } else if (territoryClan.isEnemyByClan(playerClan)) {
                    description = territoryClan.getShortDominanceString(playerClan);
                }
            }

            if (territoryClan.isAdmin()) {
                chatColor = (territoryClan.getName().equals("Outskirts") ? ChatColor.YELLOW : ChatColor.WHITE);

                boolean safe = UtilJava.cast(AdminClan.class, territoryClan).isSafe();

                if (territoryClan.getName().contains("Spawn") && safe) {
                    safe = location.getY() > 100.0D;
                } else if (territoryClan.getName().contains("Fields")) {
                    description = "<red>PvP Hotspot";
                }

                if (safe) {
                    description = "<aqua>Safe";
                }
            }
        }

        if (!(description.isEmpty())) {
            description = String.format(" <gray>(%s<gray>)", description);
        }

        return chatColor + name + description;
    }

    @Override
    public String getTerritoryClanNameForScoreboard(final Clan playerClan, final Clan territoryClan) {
        String name = "Wilderness";
        ChatColor chatColor = ChatColor.GRAY;

        if (territoryClan != null) {
            name = territoryClan.getDisplayName();
            chatColor = this.getClanRelationByClan(playerClan, territoryClan).getSuffix();

            if (territoryClan.isAdmin()) {
                chatColor = (territoryClan.getName().equals("Outskirts") ? ChatColor.YELLOW : ChatColor.WHITE);
            }
        }

        return chatColor + name;
    }

    @Override
    public Pair<String, String> getTerritoryClanNameForTitle(final Clan playerClan, final Clan territoryClan) {
        final Pair<String, ChatColor> title = new Pair<>();
        final Pair<String, ChatColor> subTitle = new Pair<>("Wilderness", ChatColor.YELLOW);

        if (territoryClan != null) {
            subTitle.setLeft(territoryClan.getDisplayName());
            subTitle.setRight(this.getClanRelationByClan(playerClan, territoryClan).getSuffix());

            if (territoryClan.isAdmin()) {
                subTitle.setRight((territoryClan.getName().equals("Outskirts") ? ChatColor.YELLOW : ChatColor.WHITE));

                if (territoryClan.getName().contains("Spawn") && UtilJava.cast(AdminClan.class, territoryClan).isSafe()) {
                    title.setLeft(territoryClan.getDisplayName());
                    title.setRight(ChatColor.WHITE);

                    subTitle.setLeft("Safe");
                    subTitle.setRight(ChatColor.AQUA);
                } else if (territoryClan.getName().contains("Fields")) {
                    title.setLeft(territoryClan.getDisplayName());
                    title.setRight(ChatColor.WHITE);

                    subTitle.setLeft("PvP Hotspot");
                    subTitle.setRight(ChatColor.RED);
                }
            }
        }

        return new Pair<>(title.isEmpty() ? " " : title.getRight() + title.getLeft(), subTitle.getRight() + subTitle.getLeft());
    }

    @Override
    public boolean hasAccess(final Player player, final Clan playerClan, final Clan territoryClan, final AccessType accessType) {
        if (territoryClan != null) {
            if (this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
                return true;
            }

            if (playerClan != null) {
                if (playerClan == territoryClan) {
                    if (accessType == AccessType.DOOR_INTERACT) {
                        return true;
                    }

                    if (playerClan.getMemberByPlayer(player).getRole() != MemberRole.RECRUIT) {
                        return true;
                    }
                } else if (playerClan.isTrustedByClan(territoryClan)) {
                    if (accessType == AccessType.DOOR_INTERACT) {
                        return true;
                    }
                } else if (playerClan.isPillageByClan(territoryClan)) {
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public boolean isSafeByLocation(final Location location) {
        return false;
    }

    @Override
    public boolean isSafeByPlayer(final Player player) {
        return false;
    }

    @Override
    public void removeClanChat(final Player player) {
        final Gamer gamer = this.getInstance(Core.class).getManagerByClass(GamerManager.class).getGamerByPlayer(player);
        if (gamer == null) {
            return;
        }

        if (Arrays.stream(ChatType.values()).noneMatch(chatType -> gamer.getChatType().equals(chatType))) {
            return;
        }

        gamer.resetChatType();
    }

    @Override
    public void outlineChunk(final Clan clan, final Chunk chunk) {
        final BlockRestoreManager blockRestoreManager = this.getInstance(Core.class).getManagerByClass(BlockRestoreManager.class);

        blockRestoreManager.outlineChunk(clan.getName(), chunk, Material.GLOWSTONE, (byte) 0, this.chunkOutlineDuration, true);
    }

    @Override
    public void unOutlineChunk(final Clan clan, final Chunk chunk) {
        final BlockRestoreManager blockRestoreManager = this.getInstance(Core.class).getManagerByClass(BlockRestoreManager.class);

        blockRestoreManager.unOutlineChunk(clan.getName(), chunk);
    }

    @Override
    public void disbandClan(final Clan clan) {
        for (final Player player : clan.getOnlineMembers().keySet()) {
            this.removeClanChat(player);
        }

        for (final Chunk chunk : clan.getTerritoryChunks()) {
            this.unOutlineChunk(clan, chunk);
        }

        for (final Clan targetClan : this.getClans().values()) {
            if (targetClan.isAllianceByClan(clan)) {
                targetClan.removeAlliance(targetClan.getAllianceByClan(clan));
                this.getRepository().updateData(targetClan, ClanProperty.ALLIANCES);
            }

            if (targetClan.isEnemyByClan(clan)) {
                targetClan.removeEnemy(targetClan.getEnemyByClan(clan));
                this.getRepository().updateData(targetClan, ClanProperty.ENEMIES);
            }

            if (targetClan.isPillageByClan(clan)) {
                targetClan.removePillage(targetClan.getPillageByClan(clan));
                this.getRepository().updateData(targetClan, ClanProperty.PILLAGES);
            }
        }

        clan.getOnlineMembers().keySet().forEach(player -> UtilServer.callEvent(new ScoreboardUpdateEvent(player)));

        this.removeClan(clan);
        this.getRepository().deleteData(clan);
    }
}