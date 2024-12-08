package me.trae.clans.weapon.weapons.items.components;

import me.trae.clans.Clans;
import me.trae.clans.weapon.weapons.items.data.SupplyCrateData;
import me.trae.core.Core;
import me.trae.core.blockrestore.BlockRestore;
import me.trae.core.blockrestore.BlockRestoreManager;
import me.trae.core.updater.annotations.Update;
import me.trae.core.updater.interfaces.Updater;
import me.trae.core.utility.*;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public interface SupplyCrateComponent extends Updater {

    List<SupplyCrateData> getData();

    String getName();

    String getDisplayName();

    Material getMaterial();

    Material getChestMaterial();

    DyeColor getFireworkColor();

    long getDuration();

    long getChestRemoveDuration();

    void onChestSpawn();

    void fillChest(final Inventory inventory);

    default void startSupplyCrate(final Block block, final Material material) {
        final BlockRestoreManager blockRestoreManager = UtilPlugin.getInstance(Core.class).getManagerByClass(BlockRestoreManager.class);

        final String blockRestoreName = String.format("%s-%s", this.getName(), UtilBlock.locationToFile(block.getLocation()));

        blockRestoreManager.addBlockRestore(new BlockRestore(blockRestoreName, block, Material.BEACON, (byte) 0, this.getDuration()));

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                blockRestoreManager.addBlockRestore(new BlockRestore(blockRestoreName, block.getLocation().add(x, -1, z).getBlock(), material, (byte) 0, this.getDuration() + this.getChestRemoveDuration()));
            }
        }
    }

    default void stopSupplyCrate(final SupplyCrateData data) {
        final Block block = data.getLocation().getBlock();
        if (block == null) {
            return;
        }

        final BlockRestoreManager blockRestoreManager = UtilPlugin.getInstance(Core.class).getManagerByClass(BlockRestoreManager.class);

        final String blockRestoreName = String.format("%s-%s", this.getName(), UtilBlock.locationToFile(block.getLocation()));

        for (final BlockRestore blockRestore : blockRestoreManager.getListOfBlockRestoreByName(blockRestoreName)) {
            blockRestore.restore();

            blockRestoreManager.removeBlockRestore(blockRestore);
        }

        block.setType(Material.AIR);
    }

    @Update(delay = 1000L)
    default void onSupplyCrateUpdater() {
        if (this.getData() == null || this.getData().isEmpty()) {
            return;
        }

        for (final SupplyCrateData data : this.getData()) {
            if (!(data.hasExpired())) {
                final Location location = new Location(data.getLocation().getWorld(), data.getLocation().getX() + 0.5D, data.getLocation().getY() + data.getCount(), data.getLocation().getZ() + 0.5D);

                UtilFirework.display(location, FireworkEffect.Type.BALL_LARGE, this.getFireworkColor());
                continue;
            }

            if (data.isFilled()) {
                continue;
            }

            final Block block = data.getLocation().getBlock();

            block.setType(this.getChestMaterial());

            UtilJava.cast(CraftChest.class, block.getState()).getTileEntity().a(this.getDisplayName());

            UtilServer.runTaskLater(Clans.class, false, 5L, () -> {
                if (block.getType() != this.getChestMaterial()) {
                    return;
                }

                final Chest chest = UtilJava.cast(Chest.class, block.getState());
                if (chest == null) {
                    return;
                }

                this.fillChest(chest.getInventory());
                data.setFilled(true);
                this.onChestSpawn();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (block.getType() != SupplyCrateComponent.this.getChestMaterial()) {
                            this.cancel();
                            return;
                        }

                        final Inventory inventory = chest.getInventory();

                        if (!(inventory.getViewers().isEmpty())) {
                            return;
                        }

                        if (Arrays.stream(inventory.getContents()).anyMatch(itemStack -> itemStack != null && itemStack.getType() != Material.AIR)) {
                            return;
                        }

                        SupplyCrateComponent.this.stopSupplyCrate(data);

                        new SoundCreator(Sound.ZOMBIE_WOODBREAK, 1.0F, 2.0F).play(block.getLocation());

                        block.setType(Material.AIR);

                        this.cancel();
                    }
                }.runTaskTimer(UtilPlugin.getInstance(Clans.class), 0L, 5L);

                UtilServer.runTaskLater(Clans.class, false, this.getChestRemoveDuration() / 50L, () -> {
                    if (block.getType() != this.getChestMaterial()) {
                        return;
                    }

                    this.stopSupplyCrate(data);

                    new SoundCreator(Sound.ZOMBIE_WOODBREAK, 1.0F, 2.0F).play(block.getLocation());

                    block.setType(Material.AIR);
                });
            });
        }
    }
}