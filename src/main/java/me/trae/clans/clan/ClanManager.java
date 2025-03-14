package me.trae.clans.clan;

import me.trae.api.combat.CombatManager;
import me.trae.api.damage.utility.UtilDamage;
import me.trae.clans.Clans;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.DominanceCommand;
import me.trae.clans.clan.commands.EnergyCommand;
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
import me.trae.clans.clan.modules.combat.HandleCombatTagTitleInSafeZones;
import me.trae.clans.clan.modules.damage.DisableSafeZoneDamage;
import me.trae.clans.clan.modules.damage.DisableTeammateDamage;
import me.trae.clans.clan.modules.energy.HandleClanEnergyUpdater;
import me.trae.clans.clan.modules.pillage.HandleDominancePointsOnPlayerDeath;
import me.trae.clans.clan.modules.pillage.HandlePillageAlerts;
import me.trae.clans.clan.modules.pillage.HandlePillageUpdater;
import me.trae.clans.clan.modules.protection.DisableProtectionTimerInSafeZones;
import me.trae.clans.clan.modules.scoreboard.HandleClansScoreboardSetup;
import me.trae.clans.clan.modules.scoreboard.HandleClansScoreboardUpdate;
import me.trae.clans.clan.modules.skill.HandleSkillFriendlyFireForSafeZones;
import me.trae.clans.clan.modules.skill.HandleSkillFriendlyFireForTeammates;
import me.trae.clans.clan.modules.skill.HandleSkillLocation;
import me.trae.clans.clan.modules.skill.HandleSkillPreActivate;
import me.trae.clans.clan.modules.spawn.HandleClansSpawnDuration;
import me.trae.clans.clan.modules.spawn.HandleClansSpawnLocation;
import me.trae.clans.clan.modules.territory.DisplayTerritoryOwner;
import me.trae.clans.clan.modules.territory.HandleAdventureModeInClanTerritory;
import me.trae.clans.clan.modules.territory.interaction.HandleClanTerritoryBlockBreak;
import me.trae.clans.clan.modules.territory.interaction.HandleClanTerritoryBlockInteract;
import me.trae.clans.clan.modules.territory.interaction.HandleClanTerritoryBlockPlace;
import me.trae.clans.clan.modules.territory.interaction.HandleClanTerritoryDoorInteract;
import me.trae.clans.clan.modules.tnt.DisablePlacingTntWhileSOTW;
import me.trae.clans.clan.modules.tnt.HandleAlertClanOnTntExplosion;
import me.trae.clans.clan.modules.tnt.HandleClanTerritoryTntProtection;
import me.trae.clans.clan.modules.weapon.HandleWeaponFriendlyFireForSafeZones;
import me.trae.clans.clan.modules.weapon.HandleWeaponFriendlyFireForTeammates;
import me.trae.clans.clan.modules.weapon.HandleWeaponLocation;
import me.trae.clans.clan.modules.weapon.HandleWeaponPreActivate;
import me.trae.clans.clan.modules.world.DisableBlockSpreadInAdminClanTerritory;
import me.trae.clans.clan.modules.world.DisableLeavesDecayInAdminClanTerritory;
import me.trae.clans.clan.modules.world.DisableNaturalCreatureSpawningInAdminClanTerritory;
import me.trae.clans.clan.modules.world.DisableShootingArrowsInSafeZones;
import me.trae.clans.clan.types.AdminClan;
import me.trae.clans.utility.UtilClans;
import me.trae.core.blockrestore.BlockRestoreManager;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.database.repository.containers.RepositoryContainer;
import me.trae.core.framework.SpigotManager;
import me.trae.core.gamer.Gamer;
import me.trae.core.gamer.GamerManager;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.*;
import me.trae.core.utility.injectors.annotations.Inject;
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

    private final Map<String, Clan> CLANS_BY_NAME = new HashMap<>();
    private final Map<UUID, Clan> CLANS_BY_UUID = new HashMap<>();
    private final Map<Chunk, Clan> CLANS_BY_CHUNK = new HashMap<>();

    @Inject
    private ClientManager clientManager;

    @Inject
    private GamerManager gamerManager;

    @Inject
    private BlockRestoreManager blockRestoreManager;

    @Inject
    private CombatManager combatManager;

    @ConfigInject(type = Long.class, path = "Chunk-Outline-Duration", defaultValue = "300_000")
    private long chunkOutlineDuration;

    @ConfigInject(type = Long.class, path = "Clan-Create-Recharge", defaultValue = "300_000")
    public long clanCreateRecharge;

    @ConfigInject(type = Long.class, path = "Pillage-Length", defaultValue = "600_000")
    public long pillageLength;

    @ConfigInject(type = Long.class, path = "Pillage-Cooldown", defaultValue = "43_200_000")
    public long pillageCooldown;

    @ConfigInject(type = Long.class, path = "TNT-Protection-Duration", defaultValue = "1_800_000")
    public long tntProtectionDuration;

    @ConfigInject(type = Long.class, path = "Last-Online-TNT-Protection-Duration", defaultValue = "600_000")
    public long lastOnlineTntProtectionDuration;

    @ConfigInject(type = Integer.class, path = "Required-Pillage-Points", defaultValue = "16")
    public int requiredPillagePoints;

    @ConfigInject(type = Integer.class, path = "Max-Squad-Count", defaultValue = "8")
    public int maxSquadCount;

    @ConfigInject(type = Integer.class, path = "Max-Claim-Limit", defaultValue = "8")
    public int maxClaimLimit;

    @ConfigInject(type = Double.class, path = "Spawn-Safe-Y-Level", defaultValue = "100.0")
    private double spawnSafeYLevel;

    @ConfigInject(type = Boolean.class, path = "Energy-Enabled", defaultValue = "true")
    public boolean energyEnabled;

    @ConfigInject(type = Long.class, path = "Default-Energy", defaultValue = "345_600_000")
    public long defaultEnergy;

    @ConfigInject(type = Double.class, path = "Cost-Per-Energy", defaultValue = "5.0")
    public double costPerEnergy;

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
        addModule(new DominanceCommand(this));
        addModule(new EnergyCommand(this));
        addModule(new MassClaimCommand(this));

        // Chat Commands
        addModule(new AllyChatCommand(this));
        addModule(new ClanChatCommand(this));

        // Champions Modules
        addModule(new HandleOverpoweredKits(this));

        // Chat Modules
        addModule(new HandleChatReceiver(this));
        addModule(new HandleClansPlayerDisplayNameFormat(this));

        // Combat Modules
        addModule(new HandleCombatTagTitleInSafeZones(this));

        // Damage Modules
        addModule(new DisableSafeZoneDamage(this));
        addModule(new DisableTeammateDamage(this));

        // Energy Modules
        addModule(new HandleClanEnergyUpdater(this));

        // Pillage Modules
        addModule(new HandleDominancePointsOnPlayerDeath(this));
        addModule(new HandlePillageAlerts(this));
        addModule(new HandlePillageUpdater(this));

        // Protection Modules
        addModule(new DisableProtectionTimerInSafeZones(this));

        // Scoreboard Modules
        addModule(new HandleClansScoreboardSetup(this));
        addModule(new HandleClansScoreboardUpdate(this));

        // Skill Modules
        addModule(new HandleSkillFriendlyFireForSafeZones(this));
        addModule(new HandleSkillFriendlyFireForTeammates(this));
        addModule(new HandleSkillLocation(this));
        addModule(new HandleSkillPreActivate(this));

        // Spawn Modules
        addModule(new HandleClansSpawnDuration(this));
        addModule(new HandleClansSpawnLocation(this));

        // Territory Modules
        addModule(new HandleClanTerritoryBlockBreak(this));
        addModule(new HandleClanTerritoryBlockInteract(this));
        addModule(new HandleClanTerritoryBlockPlace(this));
        addModule(new HandleClanTerritoryDoorInteract(this));
        addModule(new DisplayTerritoryOwner(this));
        addModule(new HandleAdventureModeInClanTerritory(this));

        // TNT Modules
        addModule(new DisablePlacingTntWhileSOTW(this));
        addModule(new HandleAlertClanOnTntExplosion(this));
        addModule(new HandleClanTerritoryTntProtection(this));

        // Weapon Modules
        addModule(new HandleWeaponFriendlyFireForSafeZones(this));
        addModule(new HandleWeaponFriendlyFireForTeammates(this));
        addModule(new HandleWeaponLocation(this));
        addModule(new HandleWeaponPreActivate(this));

        // World Modules
        addModule(new DisableBlockSpreadInAdminClanTerritory(this));
        addModule(new DisableLeavesDecayInAdminClanTerritory(this));
        addModule(new DisableNaturalCreatureSpawningInAdminClanTerritory(this));
        addModule(new DisableShootingArrowsInSafeZones(this));

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
        return this.CLANS_BY_NAME;
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
        if (this.CLANS_BY_UUID.containsKey(uuid)) {
            final Clan clan = this.CLANS_BY_UUID.get(uuid);
            if (clan != null && clan.isMemberByUUID(uuid) && this.isClanByName(clan.getName())) {
                return clan;
            }

            this.CLANS_BY_UUID.remove(uuid);
        }

        for (final Clan clan : this.getClans().values()) {
            if (!(clan.isMemberByUUID(uuid))) {
                continue;
            }

            this.CLANS_BY_UUID.put(uuid, clan);

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
        if (this.CLANS_BY_CHUNK.containsKey(chunk)) {
            final Clan clan = this.CLANS_BY_CHUNK.get(chunk);
            if (clan != null && clan.isTerritoryByChunk(chunk) && this.isClanByName(clan.getName())) {
                return clan;
            }

            this.CLANS_BY_CHUNK.remove(chunk);
        }

        for (final Clan clan : this.getClans().values()) {
            if (!(clan.isTerritoryByChunk(chunk))) {
                continue;
            }

            this.CLANS_BY_CHUNK.put(chunk, clan);

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

            final Client client = this.clientManager.searchClient(sender, name, false);
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
        final List<Client> list = clan.getMembers().values().stream().map(member -> this.clientManager.getClientByUUID(member.getUUID())).collect(Collectors.toList());

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

        map.put("Age", UtilString.format("<yellow>%s", targetClan.getCreatedString()));
        map.put("Territory", UtilString.format("<yellow>%s", targetClan.getTerritoryString(this)));

        if (this.energyEnabled) {
            map.put("Energy", targetClan.getEnergyRemainingString());
        }

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

        map.put("Points", UtilColor.bold(ChatColor.RED) + targetClan.getPoints());

        return map;
    }

    @Override
    public String getClanFullName(final Clan clan, final ClanRelation clanRelation) {
        return (clanRelation != null ? clanRelation.getSuffix() : "") + UtilString.format("%s %s", clan.getType(), clan.getDisplayName());
    }

    @Override
    public String getClanShortName(final Clan clan, final ClanRelation clanRelation) {
        ChatColor chatColor = clanRelation.getSuffix();

        if (clan.isAdmin() && clanRelation == ClanRelation.NEUTRAL) {
            chatColor = ChatColor.WHITE;
        }

        return chatColor + clan.getDisplayName();
    }

    @Override
    public String getClanName(final Clan clan, final ClanRelation clanRelation) {
        if (clan.isAdmin()) {
            ChatColor chatColor = ChatColor.WHITE;

            if (clan.getDisplayName().equals("Outskirts")) {
                chatColor = ChatColor.YELLOW;
            }

            return chatColor + clan.getDisplayName();
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
                } else if (playerClan.isPillageByClan(territoryClan)) {
                    description = UtilString.format("<green>%s", UtilTime.getTime(UtilTime.getRemaining(playerClan.getPillageByClan(territoryClan).getSystemTime(), this.pillageLength)));
                }
            }

            if (territoryClan.isAdmin()) {
                chatColor = (territoryClan.getDisplayName().equals("Outskirts") ? ChatColor.YELLOW : ChatColor.WHITE);

                if (territoryClan.getDisplayName().contains("Fields")) {
                    description = "<red>PvP Hotspot";
                } else if (this.isSafeByLocation(location)) {
                    description = "<aqua>Safe";
                }
            }
        }

        if (!(description.isEmpty())) {
            description = UtilString.format(" <gray>(%s<gray>)", description);
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
                chatColor = (territoryClan.getDisplayName().equals("Outskirts") ? ChatColor.YELLOW : ChatColor.WHITE);
            }
        }

        return chatColor + name;
    }

    @Override
    public Pair<String, String> getTerritoryClanNameForTitle(final Clan playerClan, final Clan territoryClan, final Location location) {
        final Pair<String, ChatColor> title = new Pair<>();
        final Pair<String, ChatColor> subTitle = new Pair<>("Wilderness", ChatColor.YELLOW);

        if (territoryClan != null) {
            subTitle.setLeft(territoryClan.getDisplayName());
            subTitle.setRight(this.getClanRelationByClan(playerClan, territoryClan).getSuffix());

            if (territoryClan.isAdmin()) {
                subTitle.setRight((territoryClan.getDisplayName().equals("Outskirts") ? ChatColor.YELLOW : ChatColor.WHITE));

                if (this.isSafeByLocation(location)) {
                    title.setLeft(territoryClan.getDisplayName());
                    title.setRight(ChatColor.WHITE);

                    subTitle.setLeft("Safe");
                    subTitle.setRight(ChatColor.AQUA);
                } else if (territoryClan.getDisplayName().contains("Fields")) {
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
            if (this.clientManager.getClientByPlayer(player).isAdministrating()) {
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
    public boolean canHurt(final Player damager, final Player damagee) {
        final Clan damagerClan = this.getClanByPlayer(damager);
        final Clan damageeClan = this.getClanByPlayer(damagee);

        if (damagerClan != null && damageeClan != null) {
            if (damagerClan == damageeClan) {
                return false;
            }

            if (damagerClan.isAllianceByClan(damageeClan)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canCast(final Location location) {
        return !(this.isSafeByLocation(location));
    }

    @Override
    public boolean canCast(final Player player) {
        if (this.clientManager.getClientByPlayer(player).isAdministrating()) {
            return true;
        }

        return this.canCast(player.getLocation());
    }

    @Override
    public boolean isSafeByLocation(final Location location) {
        final Clan territoryClan = this.getClanByLocation(location);
        if (territoryClan == null) {
            return false;
        }

        if (!(territoryClan.isAdmin())) {
            return false;
        }

        final AdminClan adminClan = UtilJava.cast(AdminClan.class, territoryClan);

        if (!(adminClan.isSafe())) {
            return false;
        }

        if (UtilClans.isSpawnClan(territoryClan)) {
            return location.getY() > this.spawnSafeYLevel;
        }

        return true;
    }

    @Override
    public boolean isSafeByPlayer(final Player player) {
        if (UtilDamage.isInvulnerable(player)) {
            return true;
        }

        if (this.clientManager.getClientByPlayer(player).isAdministrating()) {
            return true;
        }

        if (!(this.isSafeByLocation(player.getLocation()))) {
            return false;
        }

        if (this.combatManager.isCombatByPlayer(player)) {
            return false;
        }

        return true;
    }

    @Override
    public void removeClanChat(final Player player) {
        final Gamer gamer = this.gamerManager.getGamerByPlayer(player);
        if (gamer == null) {
            return;
        }

        if (Arrays.stream(ChatType.values()).noneMatch(gamer::isChatType)) {
            return;
        }

        gamer.resetChatType();
    }

    @Override
    public void outlineChunk(final Clan clan, final Chunk chunk) {
        this.blockRestoreManager.outlineChunk(clan.getName(), chunk, Material.GLOWSTONE, (byte) 0, this.chunkOutlineDuration, true);
    }

    @Override
    public void unOutlineChunk(final Clan clan, final Chunk chunk) {
        this.blockRestoreManager.unOutlineChunk(clan.getName(), chunk);
    }

    @Override
    public void resetEnergy(final Clan clan) {
        clan.setEnergy(this.defaultEnergy);
        this.getRepository().updateData(clan, ClanProperty.ENERGY);
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

        for (final Chunk territoryChunk : clan.getTerritoryChunks()) {
            UtilChunk.getChunkEntities(Player.class, territoryChunk).forEach(player -> {
                if (clan.isMemberByPlayer(player)) {
                    return;
                }

                UtilServer.callEvent(new ScoreboardUpdateEvent(player));
            });
        }

        this.removeClan(clan);
        this.getRepository().deleteData(clan);
    }
}