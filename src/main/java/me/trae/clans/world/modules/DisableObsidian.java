package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.world.modules.shared.interfaces.DisableBlockBreak;
import me.trae.core.world.modules.shared.interfaces.DisableBlockPlace;
import org.bukkit.Material;

public class DisableObsidian extends SpigotListener<Clans, WorldManager> implements DisableBlockBreak, DisableBlockPlace {

    public DisableObsidian(final WorldManager manager) {
        super(manager);
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
    }

    @Override
    public boolean isInform() {
        return true;
    }
}