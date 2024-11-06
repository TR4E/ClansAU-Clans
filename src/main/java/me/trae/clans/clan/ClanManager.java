package me.trae.clans.clan;

import me.trae.clans.Clans;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.interfaces.IClanManager;
import me.trae.clans.clan.modules.HandleChatReceiver;
import me.trae.clans.clan.modules.HandleClansPlayerDisplayNameFormat;
import me.trae.clans.clan.types.AdminClan;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.SpigotManager;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.objects.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClanManager extends SpigotManager<Clans> implements IClanManager {

    private final Map<String, Clan> CLANS = new HashMap<>();

    public ClanManager(final Clans instance) {
        super(instance);

        this.addPrimitive("Max-Squad-Count", 8);
        this.addPrimitive("Max-Claim-Limit", 8);
        this.addPrimitive("SOTW", false);
        this.addPrimitive("EOTW", false);

        this.addClan(new Clan("Reckoning"));
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new ClanCommand(this));

        // Modules
        addModule(new HandleChatReceiver(this));
        addModule(new HandleClansPlayerDisplayNameFormat(this));
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
            if (!(clan.isTerritory(chunk))) {
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
    public ClanRelation getClanRelationByClan(final Clan clan, final Clan target) {
        if (clan != null && target != null) {
            if (clan == target) {
                return ClanRelation.SELF;
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
        map.put("TNT Protected", targetClan.getTNTProtectionString(this, player));

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
        return (clan.isAdmin() ? this.getClanShortName(clan, clanRelation) : this.getClanFullName(clan, clanRelation));
    }

    @Override
    public String getTerritoryClanNameForChat(final Clan playerClan, final Clan territoryClan, final Location location) {
        String name = "Wilderness";
        ChatColor chatColor = ChatColor.YELLOW;
        String description = null;

        if (territoryClan != null) {
            name = territoryClan.getDisplayName();
            chatColor = this.getClanRelationByClan(playerClan, territoryClan).getSuffix();

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

        if (description != null) {
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

        return new Pair<>(title.getLeft() + title.getRight(), subTitle.getLeft() + subTitle.getRight());
    }

    @Override
    public void disbandClan(final Clan clan) {
        this.removeClan(clan);
    }
}