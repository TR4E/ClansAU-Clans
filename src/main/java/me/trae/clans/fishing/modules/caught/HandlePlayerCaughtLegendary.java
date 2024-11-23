package me.trae.clans.fishing.modules.caught;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerFishingCaughtEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.weapon.registry.WeaponRegistry;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandlePlayerCaughtLegendary extends SpigotListener<Clans, FishingManager> {

    @ConfigInject(type = Integer.class, path = "Min-Chance", defaultValue = "1")
    private int minChance;

    @ConfigInject(type = Integer.class, path = "Max-Chance", defaultValue = "10000")
    private int maxChance;

    @ConfigInject(type = Integer.class, path = "Base-Chance", defaultValue = "9990")
    private int baseChance;

    @ConfigInject(type = Integer.class, path = "Frenzy-Luck-Percentage", defaultValue = "3")
    private int frenzyLuckPercentage;

    @ConfigInject(type = Boolean.class, path = "Broadcast-Caught", defaultValue = "true")
    private boolean broadcastCaught;

    public HandlePlayerCaughtLegendary(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCaughtFish(final PlayerFishingCaughtEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.isCaught()) {
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

        final Legendary<?, ?, ?> legendary = WeaponRegistry.getRandomWeaponByClass(Legendary.class);

        event.setCaughtItemStack(legendary.getFinalBuilder().toItemStack());

        event.setCaughtName(legendary.getDisplayName());

        event.setInformPrefix("Big Catch");

        event.setBroadcastInform(this.broadcastCaught);
    }
}