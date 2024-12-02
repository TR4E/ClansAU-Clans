package me.trae.clans.utility.constants;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Pillage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.constants.CoreArgumentType;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClansArgumentType {

    public static Function<String, List<String>> CLANS = (arg) -> CoreArgumentType.CUSTOM.apply(UtilPlugin.getInstance(Clans.class).getManagerByClass(ClanManager.class).getClans().values().stream().filter(clan -> !(clan.isAdmin())).map(Clan::getName).collect(Collectors.toList()), arg);
    public static Function<String, List<String>> ONLINE_CLANS = (arg) -> CoreArgumentType.CUSTOM.apply(UtilPlugin.getInstance(Clans.class).getManagerByClass(ClanManager.class).getClans().values().stream().filter(clan -> !(clan.isAdmin()) && clan.isOnline()).map(Clan::getName).collect(Collectors.toList()), arg);

    public static BiFunction<Clan, String, List<String>> CLAN_MEMBERS = ((clan, arg) -> CoreArgumentType.CUSTOM.apply(clan.getMembers().values().stream().map(member -> UtilPlayer.getPlayerNameByUUID(member.getUUID())).collect(Collectors.toList()), arg));
    public static BiFunction<Clan, String, List<String>> CLAN_NEUTRALS = ((clan, arg) -> CoreArgumentType.CUSTOM.apply(UtilPlugin.getInstance(Clans.class).getManagerByClass(ClanManager.class).getClans().values().stream().filter(targetClan -> !(targetClan.isAdmin()) && targetClan.isNeutralByClan(clan)).map(Clan::getName).collect(Collectors.toList()), arg));
    public static BiFunction<Clan, String, List<String>> CLAN_ALLIANCES = ((clan, arg) -> CoreArgumentType.CUSTOM.apply(clan.getAlliances().values().stream().map(Alliance::getName).collect(Collectors.toList()), arg));
    public static BiFunction<Clan, String, List<String>> CLAN_TRUSTED_ALLIANCES = ((clan, arg) -> CoreArgumentType.CUSTOM.apply(clan.getAlliances().values().stream().filter(Alliance::isTrusted).map(Alliance::getName).collect(Collectors.toList()), arg));
    public static BiFunction<Clan, String, List<String>> CLAN_ENEMIES = ((clan, arg) -> CoreArgumentType.CUSTOM.apply(clan.getEnemies().values().stream().map(Enemy::getName).collect(Collectors.toList()), arg));
    public static BiFunction<Clan, String, List<String>> CLAN_PILLAGES = ((clan, arg) -> CoreArgumentType.CUSTOM.apply(clan.getPillages().values().stream().map(Pillage::getName).collect(Collectors.toList()), arg));
}