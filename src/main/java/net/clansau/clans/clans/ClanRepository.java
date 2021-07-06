package net.clansau.clans.clans;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.database.Repository;
import net.clansau.core.database.queries.DeleteQuery;
import net.clansau.core.database.queries.InsertQuery;
import net.clansau.core.database.queries.UpdateQuery;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.utility.UtilTime;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClanRepository extends Repository {

    public ClanRepository(final Clans instance) {
        super(instance, "Clan Repository", "Clans");
    }

    @Override
    protected void registerModules() {
    }

    public void saveClan(final Clan clan) {
        final Document doc = new Document();
        doc.append("Name", clan.getName());
        doc.append("Founder", clan.getFounder().toString());
        doc.append("Created", clan.getCreated());
        doc.append("Members", clan.getMembersMap().entrySet().stream().map(m -> m.getKey().toString() + ":" + m.getValue().name()).collect(Collectors.toList()));
        doc.append("Allies", clan.getAlliesMap().entrySet().stream().map(a -> a.getKey() + ":" + UtilFormat.getNumberFromBoolean(a.getValue())).collect(Collectors.toList()));
        doc.append("Enemies", clan.getEnemiesMap().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList()));
        doc.append("Home", (clan.getHome() != null ? UtilLocation.locToFile(clan.getHome()) : "None"));
        doc.append("UUID", clan.getFounder().toString());
        doc.append("Territory", new ArrayList<>(clan.getTerritory()));
        doc.append("Admin", (clan instanceof AdminClan ? 1 : 0));
        doc.append("Safe", (clan instanceof AdminClan && ((AdminClan) clan).isSafe() ? 1 : 0));
        doc.append("Last-Online", clan.getLastOnline());
        doc.append("Last-TNTed", clan.getLastTNTed());
        doc.append("Points", clan.getPoints());
        getManager().addQuery(new InsertQuery(doc, this));
    }

    public void deleteClan(final Clan clan) {
        getManager().addQuery(new DeleteQuery(new Document("Name", clan.getName()), this));
    }

    public void updateMembers(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Members", clan.getMembersMap().entrySet().stream().map(m -> m.getKey().toString() + ":" + m.getValue().name()).collect(Collectors.toList())), this, new Document("Name", clan.getName())));
    }

    public void updateAllies(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Allies", clan.getAlliesMap().entrySet().stream().map(a -> a.getKey() + ":" + UtilFormat.getNumberFromBoolean(a.getValue())).collect(Collectors.toList())), this, new Document("Name", clan.getName())));
    }

    public void updateEnemies(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Enemies", clan.getEnemiesMap().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList())), this, new Document("Name", clan.getName())));
    }

    public void updateHome(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Home", UtilLocation.locToFile(clan.getHome())), this, new Document("Name", clan.getName())));
    }

    public void updateTerritory(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Territory", new ArrayList<>(clan.getTerritory())), this, new Document("Name", clan.getName())));
    }

    public void updateAdmin(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Admin", (clan instanceof AdminClan ? 1 : 0)), this, new Document("Name", clan.getName())));
    }

    public void updateSafe(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Safe", (clan instanceof AdminClan && ((AdminClan) clan).isSafe() ? 1 : 0)), this, new Document("Name", clan.getName())));
    }

    public void updateLastOnline(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Last-Online", clan.getLastOnline()), this, new Document("Name", clan.getName())));
    }

    public void updateLastTNTed(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Last-TNTed", clan.getLastTNTed()), this, new Document("Name", clan.getName())));
    }

    public void updatePoints(final Clan clan) {
        getManager().addQuery(new UpdateQuery(new Document().append("Points", clan.getPoints()), this, new Document("Name", clan.getName())));
    }

    @Override
    protected void loadData() {
        final long then = System.currentTimeMillis();
        final ClanManager clanManager = getInstance().getManager(ClanManager.class);
        clanManager.getClans().clear();
        for (final Document doc : getCollection().find()) {
            Clan clan = new Clan((Clans) getInstance(), doc.getString("Name"));
            if (doc.getInteger("Admin") == 1) {
                clan = new AdminClan((Clans) getInstance(), doc.getString("Name"));
                ((AdminClan) clan).setSafe(doc.getInteger("Safe") == 1);
            }
            clan.setCreated(doc.getLong("Created"));
            this.addMembers(clan, doc);
            this.addAllies(clan, doc);
            this.addEnemies(clan, doc);
            clan.setHome(doc.getString("Home").equals("None") ? null : UtilLocation.fileToLoc(doc.getString("Home")));
            clan.setFounder(UUID.fromString(doc.getString("Founder")));
            clan.setTerritory(new HashSet<>(doc.getList("Territory", String.class)));
            clan.setLastOnline(doc.getLong("Last-Online"));
            clan.setLastTNTed(doc.getLong("Last-TNTed"));
            clan.setPoints(doc.getInteger("Points"));
            clanManager.addClan(clan);
        }
        UtilMessage.log("Database", "Loaded " + ChatColor.YELLOW + clanManager.getClans().size() + ChatColor.GRAY + " Clans. (" + ChatColor.GREEN + UtilTime.getTime(System.currentTimeMillis() - then, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ")");
        setDataLoaded(true);
    }

    private void addMembers(final Clan clan, final Document doc) {
        for (final String members : doc.getList("Members", String.class)) {
            final UUID uuid = UUID.fromString(members.split(":")[0]);
            final ClanRole clanRole = ClanRole.valueOf(members.split(":")[1]);
            clan.getMembersMap().put(uuid, clanRole);
        }
    }

    private void addAllies(final Clan clan, final Document doc) {
        for (final String allies : doc.getList("Allies", String.class)) {
            final String clanName = allies.split(":")[0];
            final boolean trusted = (Integer.parseInt(allies.split(":")[1]) == 1);
            clan.getAlliesMap().put(clanName, trusted);
        }
    }

    private void addEnemies(final Clan clan, final Document doc) {
        for (final String enemies : doc.getList("Enemies", String.class)) {
            final String clanName = enemies.split(":")[0];
            final int domPoints = Integer.parseInt(enemies.split(":")[1]);
            clan.getEnemiesMap().put(clanName, domPoints);
        }
    }
}