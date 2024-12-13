package me.trae.clans.clan.modules.energy;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanUpdaterEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Collections;

public class HandleClanEnergyUpdater extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Long.class, path = "Check-Duration", defaultValue = "60_000")
    private long checkDuration;

    @ConfigInject(type = Long.class, path = "Warning-Duration", defaultValue = "300_000")
    private long warningDuration;

    @ConfigInject(type = Long.class, path = "Warning-Threshold", defaultValue = "21_600_000")
    private long warningThreshold;

    private long checkSystemTime = System.currentTimeMillis();
    private long warningSystemTime = System.currentTimeMillis();

    public HandleClanEnergyUpdater(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onClanUpdater(final ClanUpdaterEvent event) {
        if (!(this.getManager().energyEnabled)) {
            return;
        }

        final Clan clan = event.getClan();

        if (clan.isAdmin() || !(clan.hasTerritory())) {
            return;
        }

        if (UtilTime.elapsed(this.warningSystemTime, this.warningDuration)) {
            this.handleWarning(clan);
            this.warningSystemTime = System.currentTimeMillis();
        }

        if (UtilTime.elapsed(this.checkSystemTime, this.checkDuration)) {
            this.handleCheck(clan);
            this.checkSystemTime = System.currentTimeMillis();
        }
    }

    private void handleWarning(final Clan clan) {
        if (clan.getEnergy() >= this.warningThreshold) {
            return;
        }

        final String title = "<red>CLAN ENERGY LOW";
        final String subTitle = UtilString.pair("<yellow>Time Remaining", clan.getEnergyRemainingString());

        for (final Player player : clan.getOnlineMembers().keySet()) {
            UtilTitle.sendTitle(player, title, subTitle, true, 2000L);
        }

        this.getManager().messageClan(clan, "Clans", title, null, null);
        this.getManager().messageClan(clan, "Clans", subTitle, null, null);
    }

    private void handleCheck(final Clan clan) {
        clan.updateEnergy();
        this.getManager().getRepository().updateData(clan, ClanProperty.ENERGY);
        clan.getOnlineMembers().keySet().forEach(player -> UtilServer.callEvent(new ScoreboardUpdateEvent(player)));

        if (clan.getEnergy() <= 0L) {
            for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
                final ClanRelation clanRelation = this.getManager().getClanRelationByClan(this.getManager().getClanByPlayer(targetPlayer), clan);

                UtilMessage.simpleMessage(targetPlayer, "Clans", "<var> has been disbanded due to no energy left!", Collections.singletonList(this.getManager().getClanFullName(clan, clanRelation)));
            }

            this.getManager().disbandClan(clan);
        }
    }
}