package net.clansau.clans.world;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.world.commands.SpawnCommand;
import net.clansau.clans.world.listeners.*;
import net.clansau.core.world.interfaces.IWorldManager;
import org.bukkit.Location;

public class WorldManager extends IWorldManager {

    private final ClanManager clanManager;

    public WorldManager(final Clans instance) {
        super(instance);
        this.clanManager = getInstance().getManager(ClanManager.class);
    }

    @Override
    protected void registerModules() {
        addModule(new SpawnCommand(this));

        addModule(new ClanBlockBreak(this));
        addModule(new ClanBlockInteract(this));
        addModule(new ClanBlockPlace(this));
        addModule(new ClanDisableLeavesDecay(this));
        addModule(new ClanDoorInteract(this));
        addModule(new ClanGamemodeHandler(this));
        addModule(new DisableArmorDurabilityInFields(this));
        addModule(new DisableArmorStandInteract(this));
        addModule(new DisableShootingArrowsInSafeZones(this));
        addModule(new DisableSoilChange(this));
        addModule(new ReceiveKitCooldown(this));
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    public final ClanManager getClanManager() {
        return this.clanManager;
    }
}