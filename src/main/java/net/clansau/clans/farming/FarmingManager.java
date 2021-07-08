package net.clansau.clans.farming;

import net.clansau.clans.Clans;
import net.clansau.clans.config.OptionsManager;
import net.clansau.clans.farming.listeners.FarmingListener;
import net.clansau.core.framework.Manager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FarmingManager extends Manager {

    public FarmingManager(final Clans instance) {
        super(instance, "Farming Manager");
    }

    @Override
    protected void registerModules() {
        addModule(new FarmingListener(this));
    }

    public final boolean isAgriculture(final Material m, final boolean blocks) {
        if (blocks) {
            return (m.equals(Material.SUGAR) || m.equals(Material.SUGAR_CANE_BLOCK));
        }
        return (m.equals(Material.SEEDS) || m.equals(Material.MELON_SEEDS) || m.equals(Material.PUMPKIN_SEEDS) || m.equals(Material.NETHER_WARTS) || m.equals(Material.POTATO) || m.equals(Material.POTATO_ITEM) || m.equals(Material.CARROT) || m.equals(Material.CARROT_ITEM));
    }

    public final boolean isFarming(final Player player, final Block block) {
        return ((player.getInventory().getItemInHand().getType().name().endsWith("_HOE") && (block.getType() == Material.GRASS || block.getType().name().contains("DIRT"))) || this.isAgriculture(player.getInventory().getItemInHand().getType(), false) && (block.getType() == Material.SOIL || block.getType() == Material.SOUL_SAND));
    }

    public final boolean isFarmingEnabled() {
        return getInstance().getManager(OptionsManager.class).isGameFarmingEnabled();
    }

    public final int getMaxYLevel() {
        return getInstance().getManager(OptionsManager.class).getGameMaxFarmingLevel();
    }

    public final int getMinYLevel() {
        return getInstance().getManager(OptionsManager.class).getGameMinFarmingLevel();
    }
}