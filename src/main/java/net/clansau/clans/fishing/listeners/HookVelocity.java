package net.clansau.clans.fishing.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.fishing.FishingManager;
import net.clansau.core.framework.Primitive;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

public class HookVelocity extends CoreListener<FishingManager> {

    public HookVelocity(final FishingManager manager) {
        super(manager, "Hook Velocity");
        addPrimitive("Velocity", new Primitive<>(-1.0));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerFish(final PlayerFishEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Entity caught = e.getCaught();
        if (caught == null) {
            return;
        }
        if (caught instanceof Player) {
            final Player player = (Player) caught;
            final Clan land = getInstance().getManager(ClanManager.class).getClan(player.getLocation());
            if (land instanceof AdminClan && ((AdminClan) land).isSafe()) {
                e.setCancelled(true);
                return;
            }
        }
        caught.setVelocity(caught.getLocation().toVector().subtract(e.getPlayer().getLocation().toVector()).multiply(getPrimitiveCasted(Double.class, "Velocity")).normalize());
    }
}