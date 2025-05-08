package me.trae.clans.utility;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.types.AdminClan;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.enums.TimeUnit;

public class UtilClans {

    public static boolean isSpawnClan(final Clan territoryClan) {
        if (territoryClan == null) {
            return false;
        }

        if (!(territoryClan.isAdmin())) {
            return false;
        }

        if (!(UtilJava.cast(AdminClan.class, territoryClan).isSafe())) {
            return false;
        }

        return territoryClan.getName().toLowerCase().endsWith("spawn");
    }

    public static boolean isShopsClan(final Clan territoryClan) {
        if (territoryClan == null) {
            return false;
        }

        if (!(territoryClan.isAdmin())) {
            return false;
        }

        if (!(UtilJava.cast(AdminClan.class, territoryClan).isSafe())) {
            return false;
        }

        return territoryClan.getName().toLowerCase().endsWith("shops");
    }

    public static boolean isFieldsClan(final Clan territoryClan) {
        if (territoryClan == null) {
            return false;
        }

        if (!(territoryClan.isAdmin())) {
            return false;
        }

        return territoryClan.getName().equalsIgnoreCase("Fields");
    }

    public static long formatClanEnergy(final Clan clan, final long duration) {
        final long millisPerMinute = TimeUnit.MINUTES.getDuration();
        final double depletionRatio = clan.getEnergyDepletionRatio();

        long rawEnergy = duration;

        if (clan.hasTerritory()) {
            rawEnergy = Math.round(((double) duration / millisPerMinute) * depletionRatio);
        }

        return rawEnergy;
    }
}