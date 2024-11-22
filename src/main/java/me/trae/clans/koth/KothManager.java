package me.trae.clans.koth;

import me.trae.clans.Clans;
import me.trae.clans.koth.commands.KothCommand;
import me.trae.clans.koth.interfaces.IKothManager;
import me.trae.clans.koth.modules.HandleKothChestOpenUpdater;
import me.trae.clans.koth.modules.HandleKothChestPlayerOpen;
import me.trae.clans.koth.modules.RemoveKothChestOpenerOnPlayerDamage;
import me.trae.core.framework.SpigotManager;
import org.bukkit.block.Block;

public class KothManager extends SpigotManager<Clans> implements IKothManager {

    public KothManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new KothCommand(this));

        // KoTH
        addModule(new Koth(this));

        // Modules
        addModule(new HandleKothChestOpenUpdater(this));
        addModule(new HandleKothChestPlayerOpen(this));
        addModule(new RemoveKothChestOpenerOnPlayerDamage(this));
    }

    @Override
    public boolean isKothChest(final Block block) {
        final Koth koth = this.getModuleByClass(Koth.class);

        if (!(koth.isActive())) {
            return false;
        }

        return koth.getData().get(0).getLocation().getBlock().equals(block);
    }
}