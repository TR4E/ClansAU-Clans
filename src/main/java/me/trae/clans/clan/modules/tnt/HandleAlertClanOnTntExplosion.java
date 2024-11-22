package me.trae.clans.clan.modules.tnt;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.tnt.events.TNTExplodeEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilTime;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.Map;

public class HandleAlertClanOnTntExplosion extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "5_000")
    private long duration;

    private final Map<String, Long> MAP = new HashMap<>();

    public HandleAlertClanOnTntExplosion(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTNTExplode(final TNTExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        for (final Block block : event.getBlocks()) {
            final Clan territoryClan = this.getManager().getClanByLocation(block.getLocation());
            if (territoryClan == null) {
                continue;
            }

            if (!(territoryClan.isOnline())) {
                continue;
            }

            if (this.MAP.containsKey(territoryClan.getName()) && !(UtilTime.elapsed(this.MAP.get(territoryClan.getName()), this.duration))) {
                continue;
            }

            this.MAP.put(territoryClan.getName(), System.currentTimeMillis());

            for (int i = 0; i < 3; i++) {
                for (final Player player : territoryClan.getOnlineMembers().keySet()) {
                    new SoundCreator(Sound.NOTE_PLING).play(player);
                }

                this.getManager().messageClan(territoryClan, "Clans", "<red><bold>YOUR TERRITORY IS UNDER ATTACK!!!", null, null);
            }
        }
    }
}