package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.Plugin;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.framework.updater.UpdateEvent;
import net.clansau.core.framework.updater.Updater;
import net.clansau.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class PillageListener extends CoreListener<ClanManager> {

    public PillageListener(final ClanManager manager) {
        super(manager, "Pillage Listener");
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (!(e.getType().equals(Updater.UpdateType.TICK_50))) {
            return;
        }
        if (!(getInstance().isSettingTrue(Plugin.SettingType.STARTED))) {
            return;
        }
        for (final Clan clan : getManager().getClans().values()) {
            if (clan.getPillagingMap().size() <= 0) {
                continue;
            }
            for (final Map.Entry<String, Long> entry : clan.getPillagingMap().entrySet()) {
                final Clan pillaged = getManager().getClan(entry.getKey());
                if (entry.getValue() <= System.currentTimeMillis()) {
                    clan.getPillagingMap().remove(pillaged.getName());
                    this.alertEnded(clan, pillaged);
                    continue;
                }
                if (UtilTime.elapsed(entry.getValue(), 60000L)) {
                    this.alertInterval(clan, pillaged, entry.getValue());
                }
            }
        }
    }

    private void alertInterval(final Clan clan, final Clan pillaged, final long interval) {
        clan.soundClan(Sound.NOTE_PLING, 1.0F, 1.0F, null);
        pillaged.soundClan(Sound.NOTE_PLING, 1.0F, 1.0F, null);
        clan.messageClan("Clans", "The Pillage you have on " + getManager().getClanRelation(clan, pillaged).getSuffix() + getManager().getName(pillaged, true) + ChatColor.GRAY + " will end in " + ChatColor.GREEN + UtilTime.getTime(interval - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ".", null);
        pillaged.messageClan("Clans", "The Pillage " + getManager().getClanRelation(pillaged, clan).getSuffix() + getManager().getName(clan, true) + ChatColor.GRAY + " has on you, will end in " + ChatColor.GREEN + UtilTime.getTime(interval - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ".", null);
    }

    private void alertEnded(final Clan clan, final Clan pillaged) {
        clan.soundClan(Sound.NOTE_PLING, 1.0F, 1.0F, null);
        pillaged.soundClan(Sound.NOTE_PLING, 1.0F, 1.0F, null);
        clan.messageClan("Clans", "The Pillage you have on " + getManager().getClanRelation(clan, pillaged).getSuffix() + getManager().getName(pillaged, true) + ChatColor.GRAY + " has ended!", null);
        pillaged.messageClan("Clans", "The Pillage " + getManager().getClanRelation(pillaged, clan).getSuffix() + getManager().getName(clan, true) + ChatColor.GRAY + " has on you, has ended!", null);
    }
}