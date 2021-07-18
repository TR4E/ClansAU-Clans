package net.clansau.clans.fields;

import net.clansau.clans.Clans;
import net.clansau.core.database.Repository;
import net.clansau.core.database.queries.DeleteQuery;
import net.clansau.core.database.queries.InsertQuery;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.utility.UtilTime;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class FieldsRepository extends Repository {

    public FieldsRepository(final Clans instance) {
        super(instance, "Fields Repository", "Fields");
    }

    @Override
    protected void registerModules() {
    }

    public void saveBlock(final Block block) {
        final Document doc = new Document();
        doc.put("Location", UtilLocation.locToFile(block.getLocation()));
        doc.put("Material", block.getTypeId());
        doc.put("Data", (int) block.getData());
        getManager().addQuery(new InsertQuery(doc, this));
    }

    public void deleteBlock(final Block block) {
        getManager().addQuery(new DeleteQuery(new Document("Location", UtilLocation.locToFile(block.getLocation())), this));
    }

    @Override
    protected void loadData() {
        final long then = System.currentTimeMillis();
        final FieldsManager fieldsManager = getInstance().getManager(FieldsManager.class);
        fieldsManager.getSavedBlocks().clear();
        for (final Document doc : getCollection().find()) {
            final Location location = UtilLocation.fileToLoc(doc.getString("Location"));
            final Material material = Material.getMaterial(doc.getInteger("Material"));
            final byte data = doc.getInteger("Data").byteValue();
            final Fields fields = new Fields(location, material, data);
            fieldsManager.getSavedBlocks().put(fields.getLocation(), fields);
        }
        UtilMessage.log("Database", "Loaded " + ChatColor.YELLOW + fieldsManager.getSavedBlocks().size() + ChatColor.GRAY + " Fields Blocks. (" + ChatColor.GREEN + UtilTime.getTime(System.currentTimeMillis() - then, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ")");
        setDataLoaded(true);
        this.setupSavedBlocks(fieldsManager);
    }

    /*
        Adding a BukkitRunnable will stop placing blocks as Asynchronous
        Error: Asynchronous entity world add!
     */
    private void setupSavedBlocks(final FieldsManager fieldsManager) {
        new BukkitRunnable() {
            @Override
            public void run() {
                fieldsManager.setupSavedBlocks();
            }
        }.runTask(getInstance());
    }
}
