package me.trae.clans.fishing.modules.caught;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerFishingCaughtEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMath;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class HandlePlayerCaughtItem extends SpigotListener<Clans, FishingManager> {

    private final List<Material> MATERIALS = Arrays.asList(Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.IRON_BLOCK);

    @ConfigInject(type = Integer.class, path = "Min-Chance", defaultValue = "1")
    private int minChance;

    @ConfigInject(type = Integer.class, path = "Max-Chance", defaultValue = "1000")
    private int maxChance;

    @ConfigInject(type = Integer.class, path = "Base-Chance", defaultValue = "950")
    private int baseChance;

    @ConfigInject(type = Integer.class, path = "Frenzy-Luck-Percentage", defaultValue = "10")
    private int frenzyLuckPercentage;

    public HandlePlayerCaughtItem(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCaughtFish(final PlayerFishingCaughtEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.isCaught()) {
            return;
        }

        if (!(event.isInFields())) {
            return;
        }

        int baseChance = this.baseChance;

        if (event.isFishingFrenzy()) {
            final double frenzyChance = this.frenzyLuckPercentage / 100.0;

            baseChance = (int) (this.maxChance - (this.maxChance * frenzyChance));
        }

        if (!(event.isChance(this.minChance, this.maxChance, baseChance))) {
            return;
        }

        Material material = this.MATERIALS.get(UtilMath.getRandomNumber(Integer.class, 0, this.MATERIALS.size()));

        if (event.isChance(1, 100, 95)) {
            material = Material.TNT;
        }

        final ItemStack itemStack = UtilItem.updateItemStack(new ItemStack(material));

        event.setCaughtItemStack(itemStack);

        event.setCaughtType("Item");

        event.setCaughtName(UtilItem.getDisplayName(itemStack, false));
    }
}