package me.trae.clans.koth.modules;

import me.trae.clans.Clans;
import me.trae.clans.koth.Koth;
import me.trae.clans.koth.KothManager;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HandleKothChestOpenUpdater extends SpigotUpdater<Clans, KothManager> {

    public HandleKothChestOpenUpdater(final KothManager manager) {
        super(manager);
    }

    @Update(delay = 100L)
    public void onUpdater() {
        final Koth koth = this.getManager().getModuleByClass(Koth.class);

        if (!(koth.isActive())) {
            return;
        }

        koth.getChestOpenerMap().entrySet().removeIf(entry -> {
            if (entry.getValue() == -1L) {
                return false;
            }

            final Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null) {
                return true;
            }

            if (!(koth.canOpenChest(player))) {
                return false;
            }

            entry.setValue(-1L);

            UtilMessage.message(player, "KoTH", "You can now open the chest!");
            return false;
        });
    }
}