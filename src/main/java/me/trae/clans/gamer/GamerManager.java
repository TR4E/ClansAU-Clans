package me.trae.clans.gamer;

import me.trae.clans.Clans;
import me.trae.clans.gamer.commands.EconomyCommand;
import me.trae.clans.gamer.commands.ProtectionCommand;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.gamer.interfaces.IGamerManager;
import me.trae.clans.gamer.modules.HandleGamerDelayedSaves;
import me.trae.clans.gamer.modules.blocks.HandleGamerBlockBreak;
import me.trae.clans.gamer.modules.blocks.HandleGamerBlockPlace;
import me.trae.clans.gamer.modules.coins.HandleCoinsOnEntityDeath;
import me.trae.clans.gamer.modules.coins.HandleCoinsOnPlayerDeath;
import me.trae.clans.gamer.modules.coins.HandleOnlineRewardCoins;
import me.trae.clans.gamer.modules.death.HandleGamerPlayerDeath;
import me.trae.clans.gamer.modules.protection.HandleProtectionDamage;
import me.trae.clans.gamer.modules.protection.HandleProtectionSkillFriendlyFire;
import me.trae.clans.gamer.modules.protection.HandleProtectionUpdate;
import me.trae.clans.gamer.modules.protection.HandleProtectionWeaponFriendlyFire;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.gamer.abstracts.AbstractGamerManager;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GamerManager extends AbstractGamerManager<Clans, Gamer, GamerProperty, GamerRepository> implements IGamerManager {

    @ConfigInject(type = Integer.class, path = "Starter-Coins-Amount", defaultValue = "5000")
    public int starterCoinsAmount;

    @ConfigInject(type = Long.class, path = "Starter-Protection-Duration", defaultValue = "3_600_000")
    public long starterProtectionDuration;

    private final Map<Gamer, Set<GamerProperty>> DELAYED_SAVES = new HashMap<>();

    public GamerManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();

        // Commands
        addModule(new EconomyCommand(this));
        addModule(new ProtectionCommand(this));

        // Blocks Modules
        addModule(new HandleGamerBlockBreak(this));
        addModule(new HandleGamerBlockPlace(this));

        // Coins Modules
        addModule(new HandleCoinsOnEntityDeath(this));
        addModule(new HandleCoinsOnPlayerDeath(this));
        addModule(new HandleOnlineRewardCoins(this));

        // Death Modules
        addModule(new HandleGamerPlayerDeath(this));

        // Protection Modules
        addModule(new HandleProtectionDamage(this));
        addModule(new HandleProtectionSkillFriendlyFire(this));
        addModule(new HandleProtectionUpdate(this));
        addModule(new HandleProtectionWeaponFriendlyFire(this));

        // Other Modules
        addModule(new HandleGamerDelayedSaves(this));
    }

    @Override
    public Class<GamerRepository> getClassOfRepository() {
        return GamerRepository.class;
    }

    @Override
    public Gamer createGamer(final Player player) {
        return new Gamer(player);
    }

    @Override
    public Gamer createGamer(final EnumData<GamerProperty> data) {
        return new Gamer(data);
    }

    @Override
    public Map<Gamer, Set<GamerProperty>> getDelayedSaves() {
        return this.DELAYED_SAVES;
    }

    @Override
    public void addDelayedSave(final Gamer gamer, final GamerProperty property) {
        if (!(this.getDelayedSaves().containsKey(gamer))) {
            this.getDelayedSaves().put(gamer, new HashSet<>());
        }

        this.getDelayedSaves().get(gamer).add(property);
    }

    @Override
    public void giveCoins(final Player player, final int coins) {
        final Gamer gamer = this.getGamerByPlayer(player);
        if (gamer == null) {
            return;
        }

        gamer.setCoins(gamer.getCoins() + coins);
        this.getRepository().updateData(gamer, GamerProperty.COINS);

        UtilServer.callEvent(new ScoreboardUpdateEvent(player));
    }
}