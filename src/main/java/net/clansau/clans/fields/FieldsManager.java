package net.clansau.clans.fields;

import net.clansau.clans.Clans;
import net.clansau.clans.fields.commands.FieldsCommand;
import net.clansau.clans.fields.listeners.GameBlockBreak;
import net.clansau.clans.fields.listeners.RestoreUpdater;
import net.clansau.clans.fields.listeners.SaveBlockBreak;
import net.clansau.clans.fields.listeners.SaveBlockPlace;
import net.clansau.core.framework.Manager;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.utility.UtilPlayer;
import net.clansau.core.utility.UtilTime;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FieldsManager extends Manager {

    private final Map<Fields, Long> gameBlocks;
    private final Map<Location, Fields> savedBlocks;
    private long lastReplenished;

    public FieldsManager(final Clans instance) {
        super(instance, "Fields Manager");
        this.gameBlocks = new HashMap<>();
        this.savedBlocks = new HashMap<>();
    }

    @Override
    protected void registerModules() {
        addModule(new FieldsCommand(this));

        addModule(new FieldsModule(this));
        addModule(new GameBlockBreak(this));
        addModule(new RestoreUpdater(this));
        addModule(new SaveBlockBreak(this));
        addModule(new SaveBlockPlace(this));
    }

    public final FieldsRepository getRepository() {
        return getInstance().getManager(FieldsRepository.class);
    }

    public final Map<Fields, Long> getGameBlocks() {
        return this.gameBlocks;
    }

    public final Map<Location, Fields> getSavedBlocks() {
        return this.savedBlocks;
    }

    public void addSavedBlock(final Block block) {
        final FieldsRepository repository = this.getRepository();
        repository.deleteBlock(block);
        repository.saveBlock(block);
        this.getSavedBlocks().put(block.getLocation(), new Fields(block.getLocation(), block.getType(), block.getData()));
    }

    public void deleteSavedBlock(final Block block) {
        this.getSavedBlocks().remove(block.getLocation());
        this.getRepository().deleteBlock(block);
    }

    public final String getLastReplenished() {
        return UtilTime.getTime(System.currentTimeMillis() - this.lastReplenished, UtilTime.TimeUnit.BEST, 1);
    }

    public final Fields getFieldsBlock(final Location location) {
        return this.getSavedBlocks().get(location);
    }

    public void addFieldsBlock(final Block block, final long duration) {
        this.getGameBlocks().put(new Fields(block.getLocation(), block.getType(), block.getData()), duration);
    }

    public void setupSavedBlocks() {
        if (this.getSavedBlocks().size() <= 0) {
            return;
        }
        for (final Fields fields : this.getSavedBlocks().values()) {
            fields.getLocation().getBlock().setTypeIdAndData(fields.getMaterial().getId(), fields.getData(), true);
        }
        this.lastReplenished = System.currentTimeMillis();
        UtilMessage.log("Fields", "Placed " + ChatColor.YELLOW + this.getSavedBlocks().size() + ChatColor.GRAY + " Blocks.");
    }

    public void restore(final boolean forced) {
        final Iterator<Map.Entry<Fields, Long>> it = this.getGameBlocks().entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            final Map.Entry<Fields, Long> next = it.next();
            if (!(forced)) {
                if (!(UtilTime.elapsed(next.getValue(), this.getRecharge()))) {
                    continue;
                }
            }
            final Fields fields = next.getKey();
            if (!(this.isFieldsBlock(fields.getMaterial()))) {
                it.remove();
                continue;
            }
            count++;
            fields.getLocation().getBlock().setTypeIdAndData(fields.getMaterial().getId(), fields.getData(), true);
            it.remove();
        }
        if (count > 0) {
            this.lastReplenished = System.currentTimeMillis();
            if (!(forced)) {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    UtilPlayer.soundPlayer(player, Sound.ORB_PICKUP, 2.0F, 2.0F);
                    UtilMessage.message(player, "Fields", "The " + ChatColor.WHITE + "Fields" + ChatColor.GRAY + " have been replenished.");
                }
            }
            UtilMessage.log("Fields", ChatColor.YELLOW.toString() + count + ChatColor.GRAY + " Fields Blocks have been replenished.");
        }
    }

    public final boolean isFieldsBlock(final Material material) {
        return (material.name().endsWith("_ORE") || material.equals(Material.ENDER_CHEST));
    }

    public final int getBlocksLeft() {
        return (this.getSavedBlocks().size() - this.getGameBlocks().size());
    }

    public final long getRecharge() {
        final int size = Bukkit.getOnlinePlayers().size();
        if (size > 65) {
            return 600000L;
        } else if (size > 50) {
            return 750000L;
        } else if (size > 35) {
            return 900000L;
        } else if (size > 15) {
            return 1050000L;
        }
        return 1200000L;
    }
}