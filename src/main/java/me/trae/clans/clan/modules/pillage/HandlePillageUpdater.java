package me.trae.clans.clan.modules.pillage;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.events.ClanUpdaterEvent;
import me.trae.clans.clan.events.pillage.PillageStopEvent;
import me.trae.clans.clan.events.pillage.PillageUpdaterEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilTime;
import org.bukkit.event.EventHandler;

public class HandlePillageUpdater extends SpigotListener<Clans, ClanManager> {

    public HandlePillageUpdater(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onClanUpdater(final ClanUpdaterEvent event) {
        final Clan playerClan = event.getClan();

        playerClan.getPillages().values().removeIf(pillage -> {
            final Clan pillageClan = this.getManager().getClanByName(pillage.getName());
            if (pillageClan == null) {
                return false;
            }

            UtilServer.callEvent(new PillageUpdaterEvent(playerClan, pillageClan));

            if (!(UtilTime.elapsed(pillage.getSystemTime(), this.getManager().pillageLength))) {
                return false;
            }

            UtilServer.callEvent(new PillageStopEvent(playerClan, pillageClan));

            this.getManager().getRepository().updateData(playerClan, ClanProperty.PILLAGES);
            return true;
        });
    }
}