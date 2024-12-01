package me.trae.clans.fishing.modules.caught;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.enums.FishName;
import me.trae.clans.fishing.events.PlayerFishingCaughtEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class HandlePlayerCaughtFish extends SpigotListener<Clans, FishingManager> {

    @ConfigInject(type = Integer.class, path = "Frenzy-Luck-Percentage", defaultValue = "20")
    private int frenzyLuckPercentage;

    @ConfigInject(type = Boolean.class, path = "Broadcast-Big-Caught", defaultValue = "true")
    private boolean broadcastBigCaught;

    public HandlePlayerCaughtFish(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCaughtFish(final PlayerFishingCaughtEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.isCaught()) {
            return;
        }

        int bigCatchWeight = this.getManager().getModuleByClass(HandleFishingCaughtWeight.class).bigCatchWeight;

        int weight = event.getWeight();

        if (event.isFishingFrenzy()) {
            final double frenzyChance = this.frenzyLuckPercentage / 100.0;

            weight += (int) (weight * frenzyChance);
            bigCatchWeight += (int) (bigCatchWeight * frenzyChance);
        }

        final FishName fishName = FishName.getRandom();

        event.setCaughtItemStack(new ItemBuilder(new ItemStack(Material.RAW_FISH, weight), fishName.getName()).toItemStack());

        event.setCaughtName(String.format("<green>%sx Pound <gray>of <green>%s", weight, fishName.getName()));

        if (weight > bigCatchWeight) {
            event.setInformPrefix("Big Catch");
            event.setBroadcastInform(this.broadcastBigCaught);
        }
    }
}