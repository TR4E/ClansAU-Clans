package me.trae.clans.fishing.modules.bite;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerFishingBiteEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class HandlePlayerBiteFishSound extends SpigotListener<Clans, FishingManager> {

    public HandlePlayerBiteFishSound(final FishingManager manager) {
        super(manager);
    }

    @EventHandler
    public void onPlayerFishBite(final PlayerFishingBiteEvent event) {
        new SoundCreator(Sound.LEVEL_UP).play(event.getPlayer());
    }
}