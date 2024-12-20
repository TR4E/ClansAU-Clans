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
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Arrays;
import java.util.Collections;

public class HandleClanEnergyUpdater extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Long.class, path = "Check-Duration", defaultValue = "60_000")
    private long checkDuration;

    @ConfigInject(type = Long.class, path = "Warning-Duration", defaultValue = "300_000")
    private long warningDuration;

    @ConfigInject(type = Long.class, path = "Warning-Threshold", defaultValue = "21_600_000")
    private long warningThreshold;

    @ConfigInject(type = Long.class, path = "Mini-Warning-Threshold", defaultValue = "300_000")
    private long miniWarningThreshold;

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
        if (clan.getEnergyDuration() >= this.warningThreshold) {
            return;
        }

        final String title = "<red>CLAN ENERGY LOW";
        final String subTitle = UtilString.pair("<gray>Time Remaining", clan.getEnergyRemainingString());

        for (final Player player : clan.getOnlineMembers().keySet()) {
            new SoundCreator(Sound.NOTE_PLING).play(player);
            UtilTitle.sendTitle(player, title, subTitle, true, 2000L);
        }

        this.getManager().messageClan(clan, "Clans", title, null, null);
        this.getManager().messageClan(clan, "Clans", subTitle, null, null);
    }

    private void handleCheck(final Clan clan) {
        clan.updateEnergy();
        this.getManager().getRepository().updateData(clan, ClanProperty.ENERGY);
        clan.getOnlineMembers().keySet().forEach(player -> UtilServer.callEvent(new ScoreboardUpdateEvent(player)));

        if (clan.getEnergy() == this.miniWarningThreshold) {
            clan.getOnlineMembers().keySet().forEach(player -> new SoundCreator(Sound.NOTE_PLING).play(player));
            this.getManager().messageClan(clan, "Clans", "If you do not buy more energy, your clan will disband in <green><var></green>.", Collections.singletonList(UtilTime.getTime(this.miniWarningThreshold)), null);
            return;
        }

        if (clan.getEnergy() <= 0L) {
            this.handleDisband(clan);
        }
    }

    private void handleDisband(final Clan clan) {
        String locationString = "";
        if (clan.hasTerritory()) {
            if (clan.hasHome()) {
                locationString = clan.getHomeString();
            } else {
                locationString = UtilLocation.locationToString(UtilLocation.getLocationByChunk(clan.getTerritoryChunks().get(0)));
            }
        }

        if (!(locationString.isEmpty())) {
            locationString = UtilString.format(" %s", locationString);
        }

        for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
            final ClanRelation clanRelation = this.getManager().getClanRelationByClan(this.getManager().getClanByPlayer(targetPlayer), clan);

            UtilMessage.simpleMessage(targetPlayer, "Clans", "<var> was disbanded for running out of energy!<var>", Arrays.asList(this.getManager().getClanFullName(clan, clanRelation), locationString));
        }

        this.getManager().disbandClan(clan);
    }
}