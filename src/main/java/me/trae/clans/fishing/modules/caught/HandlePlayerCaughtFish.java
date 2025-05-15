package me.trae.clans.fishing.modules.caught;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.enums.FishName;
import me.trae.clans.fishing.events.PlayerFishingCaughtEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
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
        if (!(event.isInFields())) {
            weight = weight / 4;

            UtilMessage.simpleMessage(event.getPlayer(), "Fishing", "It is recommended to fish at <white>Fields</white> for better luck.");
        }

        if (event.isFishingFrenzy() && event.isInFields()) {
            final double frenzyChance = this.frenzyLuckPercentage / 100.0;

            weight += (int) (weight * frenzyChance);
            bigCatchWeight += (int) (bigCatchWeight * frenzyChance);
        }

        final FishName fishName = FishName.getRandom();

        event.setCaughtItemStack(new ItemBuilder(new ItemStack(Material.RAW_FISH, weight), fishName.getName()).toItemStack());

        event.setCaughtType("Fish");

        event.setCaughtName(UtilString.format("<green>%sx Pound <gray>of <green>%s", weight, fishName.getName()));

        if (weight > bigCatchWeight) {
            event.setInformPrefix("Big Catch");
            event.setBroadcastInform(this.broadcastBigCaught);
        }
    }
}