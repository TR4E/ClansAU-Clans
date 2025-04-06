package me.trae.clans.clan.commands.subcommands;

import me.trae.api.combat.CombatManager;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.events.command.ClanHomeEvent;
import me.trae.clans.utility.UtilClans;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.countdown.CountdownManager;
import me.trae.core.gamer.Gamer;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.teleport.Teleport;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import me.trae.core.utility.injectors.annotations.Inject;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class HomeCommand extends ClanSubCommand implements EventContainer<ClanHomeEvent> {

    @Inject
    private RechargeManager rechargeManager;

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "300_000")
    private long recharge;

    @ConfigInject(type = Long.class, path = "Wilderness-Duration", defaultValue = "30_000")
    private long wildernessDuration;

    @ConfigInject(type = Long.class, path = "Territory-Duration", defaultValue = "10_000")
    private long territoryDuration;

    @ConfigInject(type = Long.class, path = "Spawn-Duration", defaultValue = "0")
    private long spawnDuration;

    @ConfigInject(type = Boolean.class, path = "Spawn-Only", defaultValue = "false")
    private boolean spawnOnly;

    private final String RECHARGE_NAME = "Clan Home";

    public HomeCommand(final ClanCommand command) {
        super(command, "home");
    }

    @Override
    public String getShortcut() {
        return "home";
    }

    @Override
    public String getDescription() {
        return "Teleport to Clan Home";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.RECRUIT;
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(this.canTeleportHome(player, client, clan))) {
            return;
        }

        this.callEvent(new ClanHomeEvent(clan, player, client));
    }

    private boolean canTeleportHome(final Player player, final Client client, final Clan clan) {
        if (!(clan.hasHome())) {
            UtilMessage.message(player, "Clans", "Your Clan does not have a home set!");
            return false;
        }

        if (!(client.isAdministrating())) {
            final Clan territoryClan = this.getModule().getManager().getClanByLocation(player.getLocation());

            final boolean isSpawnClan = UtilClans.isSpawnClan(territoryClan);
            final boolean isTerritoryClan = territoryClan != null && territoryClan.isMemberByPlayer(player);

            if (!(isSpawnClan) && this.spawnOnly) {
                UtilMessage.simpleMessage(player, "Clans", "You can only teleport to Clan Home from <white>Spawn</white>!");
                return false;
            }

            if (!(isSpawnClan && isTerritoryClan)) {
                if (this.rechargeManager.isCooling(player, this.RECHARGE_NAME, true)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Class<ClanHomeEvent> getClassOfEvent() {
        return ClanHomeEvent.class;
    }

    @Override
    public void onEvent(final ClanHomeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        final CountdownManager countdownManager = this.getInstanceByClass(Core.class).getManagerByClass(CountdownManager.class);

        final Teleport oldTeleport = UtilJava.cast(Teleport.class, countdownManager.getCountdownByPlayer(player));
        if (oldTeleport != null && oldTeleport.getType().equals("Clan Home")) {
            UtilMessage.message(player, "Clans", "You are already attempting to teleport to Clan Home!");
            return;
        }

        countdownManager.addCountdown(this.getTeleport(player, event.getClan()));
    }

    private Teleport getTeleport(final Player player, final Clan playerClan) {
        long duration = this.wildernessDuration;

        final Clan territoryClan = this.getModule().getManager().getClanByLocation(player.getLocation());

        final boolean isSpawnClan = UtilClans.isSpawnClan(territoryClan);
        final boolean isTerritoryClan = territoryClan != null && territoryClan.isMemberByPlayer(player);

        if (isSpawnClan) {
            duration = this.spawnDuration;
        } else if (isTerritoryClan) {
            duration = this.territoryDuration;
        }

        return new Teleport(duration, player, playerClan.getHome()) {
            @Override
            public void onInitial(final Player player) {
                if (this.getDuration() > 0L) {
                    UtilMessage.simpleMessage(player, "Teleport", "Teleporting to <white><var></white> in <green><var></green>.", Arrays.asList(this.getType(), this.getDurationString()));
                }
            }

            @Override
            public String getType() {
                return "Clan Home";
            }

            @Override
            public boolean canTeleport(final Player player) {
                if (HomeCommand.this.getInstanceByClass(Core.class).getManagerByClass(CombatManager.class).isCombatByPlayer(player)) {
                    UtilMessage.message(player, "Clans", "You cannot teleport to Clan Home while in Combat!");
                    return false;
                }

                return true;
            }

            @Override
            public void onPreTeleport(final Player player) {
                if (!(isSpawnClan && isTerritoryClan)) {
                    HomeCommand.this.getInstanceByClass(Core.class).getManagerByClass(RechargeManager.class).add(player, HomeCommand.this.RECHARGE_NAME, HomeCommand.this.recharge, true, true);
                }
            }

            @Override
            public void onPostTeleport(final Player player) {
                UtilMessage.message(player, "Clans", "You teleported to Clan Home.");
            }
        };
    }
}