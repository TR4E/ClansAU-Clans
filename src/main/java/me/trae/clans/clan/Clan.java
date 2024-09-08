package me.trae.clans.clan;

import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.interfaces.IClan;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilPlugin;
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

    private UUID founder;
    private Location home;

    public Clan(final String name) {
        this.name = name;
    }

    public Clan(final String name, final Player player) {
        this(name);

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
    public UUID getFounder() {
        return this.founder;
    }

    @Override
    public String getFounderString() {
        return String.format("<yellow>%s", UtilPlugin.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByUUID(this.getFounder()).getName());
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