package me.trae.clans.fields;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.fields.commands.FieldsCommand;
import me.trae.clans.fields.enums.FieldsBlockProperty;
import me.trae.clans.fields.interfaces.IFieldsManager;
import me.trae.clans.fields.modules.HandleFieldsBlockBreak;
import me.trae.clans.fields.modules.HandleFieldsBlockUpdater;
import me.trae.clans.fields.modules.admin.HandleAdminFieldsBlockBreak;
import me.trae.clans.fields.modules.admin.HandleAdminFieldsBlockPlace;
import me.trae.clans.fields.modules.loot.HandleFieldsClanEnergyLoot;
import me.trae.clans.fields.modules.loot.HandleFieldsEnderChestLoot;
import me.trae.clans.fields.modules.loot.HandleFieldsOreLoot;
import me.trae.core.Core;
import me.trae.core.blockrestore.data.RestoreData;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.database.repository.containers.RepositoryContainer;
import me.trae.core.framework.SpigotManager;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FieldsManager extends SpigotManager<Clans> implements IFieldsManager, RepositoryContainer<FieldsRepository> {

    private final Map<Location, FieldsBlock> BLOCKS = new HashMap<>();

    private long lastUpdated = System.currentTimeMillis();

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "1_800_000")
    public long duration;

    public FieldsManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new FieldsCommand(this));

        // Admin Modules
        addModule(new HandleAdminFieldsBlockBreak(this));
        addModule(new HandleAdminFieldsBlockPlace(this));

        // Loot Modules
        addModule(new HandleFieldsClanEnergyLoot(this));
        addModule(new HandleFieldsEnderChestLoot(this));
        addModule(new HandleFieldsOreLoot(this));

        // Modules
        addModule(new HandleFieldsBlockBreak(this));
        addModule(new HandleFieldsBlockUpdater(this));
    }

    @Override
    public Class<FieldsRepository> getClassOfRepository() {
        return FieldsRepository.class;
    }

    @Override
    public Map<Location, FieldsBlock> getBlocks() {
        return this.BLOCKS;
    }

    @Override
    public void addBlock(final FieldsBlock fieldsBlock) {
        this.getBlocks().put(fieldsBlock.getLocation(), fieldsBlock);
    }

    @Override
    public void removeBlock(final FieldsBlock fieldsBlock) {
        this.getBlocks().remove(fieldsBlock.getLocation());
    }

    @Override
    public FieldsBlock getBlockByLocation(final Location location) {
        return this.getBlocks().getOrDefault(location, null);
    }

    @Override
    public boolean isBlockByLocation(final Location location) {
        return this.getBlocks().containsKey(location);
    }

    @Override
    public long getLastUpdated() {
        return this.lastUpdated;
    }

    @Override
    public void updateLastUpdated() {
        this.lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void replenish() {
        final boolean replenished = this.getBlocks().values().stream().anyMatch(FieldsBlock::isBroken);
        if (!(replenished)) {
            return;
        }

        for (final FieldsBlock fieldBlock : this.getBlocks().values()) {
            fieldBlock.restore();

            fieldBlock.setBroken(false);
            this.getRepository().updateData(fieldBlock, FieldsBlockProperty.BROKEN);
        }

        new SoundCreator(Sound.ORB_PICKUP).play();

        UtilMessage.simpleBroadcast("Fields", "The <white>Fields</white> has been replenished!", null);
    }

    @Override
    public boolean isInFields(final Location location) {
        final Clan territoryClan = this.getInstance().getManagerByClass(ClanManager.class).getClanByLocation(location);
        if (territoryClan == null) {
            return false;
        }

        if (!(territoryClan.isAdmin())) {
            return false;
        }

        if (!(territoryClan.getDisplayName().equals("Fields"))) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isInAdminMode(final Player player) {
        return this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating();
    }

    @Override
    public boolean isFieldsBlock(final Block block) {
        final Material material = block.getType();

        if (material.name().endsWith("_ORE")) {
            return true;
        }

        if (material == Material.ENDER_CHEST) {
            return true;
        }

        return false;
    }

    @Override
    public RestoreData getNewRestoreDataByMaterial(final Material material) {
        if (material.name().endsWith("_ORE")) {
            return new RestoreData(Material.BEDROCK);
        }

        return new RestoreData(Material.AIR);
    }
}