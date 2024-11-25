package me.trae.clans.koth.modules;

import me.trae.api.damage.events.damage.CustomPostDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.koth.Koth;
import me.trae.clans.koth.KothManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class RemoveKothChestOpenerOnPlayerDamage extends SpigotListener<Clans, KothManager> {

    public RemoveKothChestOpenerOnPlayerDamage(final KothManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomPostDamage(final CustomPostDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getDamagee() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        final Player player = event.getDamageeByClass(Player.class);

        final Koth koth = this.getManager().getModuleByClass(Koth.class);
        if (!(koth.isActive())) {
            return;
        }

        if (!(koth.isChestOpener(player))) {
            return;
        }

        koth.removeChestOpener(player);

        UtilMessage.message(player, "KoTH", "Your Chest Timer has reset, because you took damage.");
    }
}