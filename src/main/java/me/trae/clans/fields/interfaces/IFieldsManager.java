package me.trae.clans.fields.interfaces;

import me.trae.clans.fields.FieldsBlock;
import me.trae.core.blockrestore.data.RestoreData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface IFieldsManager {

    Map<Location, FieldsBlock> getBlocks();

    void addBlock(final FieldsBlock fieldsBlock);

    void removeBlock(final FieldsBlock fieldsBlock);

    FieldsBlock getBlockByLocation(final Location location);

    boolean isBlockByLocation(final Location location);

    long getLastUpdated();

    void updateLastUpdated();

    void replenish();

    boolean isInFields(final Location location);

    boolean isInAdminMode(final Player player);

    boolean isFieldsBlock(final Block block);

    RestoreData getNewRestoreDataByMaterial(final Material material);

    List<ItemStack> getDropsByBlock(final FieldsBlock fieldsBlock, final Material material);
}