package me.trae.clans.clan.modules.pillage;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.pillage.PillageStopEvent;
import me.trae.clans.clan.events.pillage.PillageUpdaterEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilTime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.*;

public class HandlePillageAlerts extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "60_000")
    private long duration;

    private final Map<String, Map<String, Long>> ALERT_MAP = new HashMap<>();

    public HandlePillageAlerts(final ClanManager manager) {
        super(manager);
    }

    private void addAlert(final Clan pillagerClan, final Clan pillageeClan) {
        if (!(this.ALERT_MAP.containsKey(pillagerClan.getName()))) {
            this.ALERT_MAP.put(pillagerClan.getName(), new HashMap<>());
        }

        final Map<String, Long> map = this.ALERT_MAP.get(pillagerClan.getName());

        if (map.containsKey(pillageeClan.getName())) {
            return;
        }

        map.put(pillageeClan.getName(), System.currentTimeMillis());
    }

    private void updateAlert(final Clan pillagerClan, final Clan pillageeClan) {
        if (!(this.ALERT_MAP.containsKey(pillagerClan.getName()))) {
            return;
        }

        final Map<String, Long> map = this.ALERT_MAP.get(pillagerClan.getName());

        if (!(map.containsKey(pillageeClan.getName()))) {
            return;
        }

        map.put(pillageeClan.getName(), System.currentTimeMillis());
    }

    private Optional<Long> getAlert(final Clan pillagerClan, final Clan pillageeClan) {
        if (this.ALERT_MAP.containsKey(pillagerClan.getName())) {
            final Map<String, Long> map = this.ALERT_MAP.get(pillagerClan.getName());

            if (map.containsKey(pillageeClan.getName())) {
                return Optional.of(map.get(pillageeClan.getName()));
            }
        }

        return Optional.empty();
    }

    private boolean hasAlert(final Clan pillagerClan, final Clan pillageeClan) {
        return this.ALERT_MAP.getOrDefault(pillagerClan.getName(), new HashMap<>()).containsKey(pillageeClan.getName());
    }

    @EventHandler
    public void onPillageUpdater(final PillageUpdaterEvent event) {
        final Clan pillagerClan = event.getClan();
        final Clan pillageeClan = event.getTarget();

        boolean inital = false;

        if (!(this.hasAlert(pillagerClan, pillageeClan))) {
            inital = true;
            this.addAlert(pillagerClan, pillageeClan);
        }

        final Optional<Long> alertOptional = this.getAlert(pillagerClan, pillageeClan);
        if (!(alertOptional.isPresent())) {
            return;
        }

        if (!(inital) && !(UtilTime.elapsed(alertOptional.get(), this.duration))) {
            return;
        }

        final ClanRelation clanRelationByPillagerClan = this.getManager().getClanRelationByClan(pillagerClan, pillageeClan);
        final ClanRelation clanRelationByPillageeClan = this.getManager().getClanRelationByClan(pillageeClan, pillagerClan);

        String remainingString = UtilTime.getTimeZero(this.getManager().pillageLength);
        if (!(inital)) {
            remainingString = UtilTime.getTimeZero(UtilTime.getRemaining(event.getPillage().getSystemTime(), this.getManager().pillageLength));
        }

        this.getManager().messageClan(pillagerClan, "Clans", "The Pillage you have on <var>, ends in <green><var></green>!", Arrays.asList(this.getManager().getClanFullName(pillageeClan, clanRelationByPillagerClan), remainingString), null);
        this.getManager().messageClan(pillageeClan, "Clans", "The Pillage <var> has on you, ends in <green><var></green>!", Arrays.asList(this.getManager().getClanFullName(pillagerClan, clanRelationByPillageeClan), remainingString), null);

        this.updateAlert(pillagerClan, pillageeClan);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPillageStop(final PillageStopEvent event) {
        final Clan pillagerClan = event.getClan();
        final Clan pillageeClan = event.getTarget();

        final ClanRelation clanRelationByPillagerClan = this.getManager().getClanRelationByClan(pillagerClan, pillageeClan);
        final ClanRelation clanRelationByPillageeClan = this.getManager().getClanRelationByClan(pillageeClan, pillagerClan);

        this.getManager().messageClan(pillagerClan, "Clans", "The Pillage you have on <var>, has ended!", Collections.singletonList(this.getManager().getClanFullName(pillageeClan, clanRelationByPillagerClan)), null);
        this.getManager().messageClan(pillageeClan, "Clans", "The Pillage <var> has on you, has ended!", Collections.singletonList(this.getManager().getClanFullName(pillagerClan, clanRelationByPillageeClan)), null);
    }
}