package net.clansau.clans.fishing.listeners;

import net.clansau.clans.fishing.FishingManager;
import net.clansau.core.framework.Primitive;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.energy.EnergyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

public class HookEnergy extends CoreListener<FishingManager> {

    public HookEnergy(final FishingManager manager) {
        super(manager, "Hook Energy");
        addPrimitive("Energy", new Primitive<>(25.0));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFish(final PlayerFishEvent e) {
        if (e.getState().equals(PlayerFishEvent.State.FISHING)) {
            return;
        }
        if (getInstance().getManager(EnergyManager.class).use(e.getPlayer(), "Hook", getPrimitiveCasted(Double.class, "Energy"), true)) {
            return;
        }
        if (e.getCaught() == null) {
            return;
        }
        e.setCancelled(true);
    }
}