package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.events.ClanHomeEvent;
import me.trae.clans.clan.types.AdminClan;
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
import me.trae.core.utility.enums.TimeUnit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeCommand extends ClanSubCommand implements EventContainer<ClanHomeEvent> {

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "600_000")
    private long recharge;

    @ConfigInject(type = Boolean.class, path = "Spawn-Only", defaultValue = "false")
    private boolean spawnOnly;

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
            if (this.spawnOnly) {
                if (!(this.isSpawnByLocation(player.getLocation()))) {
                    UtilMessage.simpleMessage(player, "Clans", "You can only teleport to Clan Home from <white>Spawn</white>!");
                    return false;
                }
            }

            return !(this.getInstance(Core.class).getManagerByClass(RechargeManager.class).isCooling(player, "Clan Home", true));
        }

        return true;
    }

    private boolean isSpawnByLocation(final Location location) {
        final Clan territoryClan = this.getModule().getManager().getClanByLocation(location);

        return territoryClan instanceof AdminClan && UtilJava.cast(AdminClan.class, territoryClan).isSafe() && territoryClan.getName().toLowerCase().contains("spawn");
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

        this.getInstance(Core.class).getManagerByClass(CountdownManager.class).addCountdown(this.getTeleport(event.getPlayer(), event.getClan().getHome()));
    }

    private Teleport getTeleport(final Player player, final Location location) {
        final long duration = this.isSpawnByLocation(location) ? 0L : TimeUnit.SECONDS.getDuration() * 10;

        return new Teleport(duration, player, location) {
            @Override
            public void onTeleport(final Player player) {
                HomeCommand.this.getInstance(Core.class).getManagerByClass(RechargeManager.class).add(player, "Clan Home", HomeCommand.this.recharge, true);

                UtilMessage.message(player, "Clans", "You teleported to Clan Home.");
            }
        };
    }
}