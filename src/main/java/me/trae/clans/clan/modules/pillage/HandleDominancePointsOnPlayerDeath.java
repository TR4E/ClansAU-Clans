package me.trae.clans.clan.modules.pillage;

import me.trae.api.combat.events.PlayerClickCombatNpcEvent;
import me.trae.api.damage.events.PlayerSuicideEvent;
import me.trae.api.death.events.CustomDeathEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.Pillage;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.pillage.PillageStartEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Arrays;

public class HandleDominancePointsOnPlayerDeath extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Boolean.class, path = "Gain-On-Suicide", defaultValue = "true")
    private boolean gainOnSuicide;

    @ConfigInject(type = Boolean.class, path = "Gain-On-Combat-Log", defaultValue = "true")
    private boolean gainOnCombatLog;

    public HandleDominancePointsOnPlayerDeath(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomDeath(final CustomDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getKiller() instanceof Player)) {
            return;
        }

        final Player player = event.getEntityByClass(Player.class);
        final Player killer = event.getKillerByClass(Player.class);

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        final Clan killerClan = this.getManager().getClanByPlayer(killer);

        this.handleDeath(playerClan, killerClan);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerSuicide(final PlayerSuicideEvent event) {
        if (!(this.gainOnSuicide)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        final Clan territoryClan = this.getManager().getClanByLocation(player.getLocation());

        this.handleDeath(playerClan, territoryClan);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerClickCombatNpc(final PlayerClickCombatNpcEvent event) {
        if (!(this.gainOnCombatLog)) {
            return;
        }

        final Player player = event.getPlayer();
        final OfflinePlayer target = event.getTarget();

        final Clan killedClan = this.getManager().getClanByUUID(target.getUniqueId());
        final Clan killerClan = this.getManager().getClanByPlayer(player);

        this.handleDeath(killedClan, killerClan);
    }

    private void handleDeath(final Clan playerClan, final Clan killerClan) {
        if (playerClan == null || killerClan == null) {
            return;
        }

        final Enemy playerClanEnemy = playerClan.getEnemyByClan(killerClan);
        final Enemy killerClanEnemy = killerClan.getEnemyByClan(playerClan);

        if (playerClanEnemy == null || killerClanEnemy == null) {
            return;
        }

        if (killerClanEnemy.getDominancePoints() >= (this.getManager().requiredPillagePoints - 1)) {
            UtilServer.callEvent(new PillageStartEvent(killerClan, playerClan));
            return;
        }

        this.handleDominancePoints(playerClan, killerClan, playerClanEnemy, killerClanEnemy);
    }

    private void handleDominancePoints(final Clan playerClan, final Clan killerClan, final Enemy playerClanEnemy, final Enemy killerClanEnemy) {
        // Player Clan always loses a Point
        // Killer Clan rather recovers or gains a Point

        final ClanRelation clanRelationByPlayerClan = this.getManager().getClanRelationByClan(playerClan, killerClan);
        final ClanRelation clanRelationByKillerClan = this.getManager().getClanRelationByClan(killerClan, playerClan);

        if (playerClanEnemy.getDominancePoints() > killerClanEnemy.getDominancePoints()) {
            // Player Clan Lost a Gained Point
            // Killer Clan recovers a Lost Point

            playerClanEnemy.setDominancePoints(playerClanEnemy.getDominancePoints() - 1);
            this.getManager().getRepository().updateData(playerClan, ClanProperty.ENEMIES);

            this.getManager().messageClan(playerClan, "Clans", "You lost a gained Dominance by <var>. <var>", Arrays.asList(this.getManager().getClanFullName(killerClan, clanRelationByPlayerClan), playerClan.getDominanceString(killerClan)), null);
            this.getManager().messageClan(killerClan, "Clans", "You recovered a lost Dominance against <var>. <var>", Arrays.asList(this.getManager().getClanFullName(playerClan, clanRelationByKillerClan), killerClan.getDominanceString(playerClan)), null);
        } else {
            // Player Clan Lost a Point
            // Killer Clan Gains a Point

            killerClanEnemy.setDominancePoints(killerClanEnemy.getDominancePoints() + 1);
            this.getManager().getRepository().updateData(killerClan, ClanProperty.ENEMIES);

            this.getManager().messageClan(playerClan, "Clans", "You lost Dominance by <var>. <var>", Arrays.asList(this.getManager().getClanFullName(killerClan, clanRelationByPlayerClan), playerClan.getDominanceString(killerClan)), null);
            this.getManager().messageClan(killerClan, "Clans", "You gained Dominance against <var>. <var>", Arrays.asList(this.getManager().getClanFullName(playerClan, clanRelationByKillerClan), killerClan.getDominanceString(playerClan)), null);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPillageStart(final PillageStartEvent event) {
        final Clan pillagerClan = event.getClan();
        final Clan pillageeClan = event.getTarget();

        UtilJava.call(() -> {
            // Remove Enemy from Player Clan
            pillageeClan.removeEnemy(pillageeClan.getEnemyByClan(pillagerClan));
            this.getManager().getRepository().updateData(pillageeClan, ClanProperty.ENEMIES);

            // Remove Enemy from Killer Clan
            pillagerClan.removeEnemy(pillagerClan.getEnemyByClan(pillageeClan));
            this.getManager().getRepository().updateData(pillagerClan, ClanProperty.ENEMIES);

            // Save Pillage to Killer Clan
            pillagerClan.addPillage(new Pillage(pillageeClan));
            this.getManager().getRepository().updateData(pillagerClan, ClanProperty.PILLAGES);

            // Add a point to Pillager Clan
            pillagerClan.setPoints(pillagerClan.getPoints() + 1);
            this.getManager().getRepository().updateData(pillagerClan, ClanProperty.POINTS);
        });

        for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
            final Clan targetPlayerClan = this.getManager().getClanByPlayer(targetPlayer);

            final ClanRelation clanRelationForPillagerClan = this.getManager().getClanRelationByClan(targetPlayerClan, pillagerClan);
            final ClanRelation clanRelationForPillageeClan = this.getManager().getClanRelationByClan(targetPlayerClan, pillageeClan);

            UtilMessage.simpleMessage(targetPlayer, "Clans", "<var> has conquered <var>.", Arrays.asList(this.getManager().getClanFullName(pillagerClan, clanRelationForPillagerClan), this.getManager().getClanFullName(pillageeClan, clanRelationForPillageeClan)));
        }
    }
}