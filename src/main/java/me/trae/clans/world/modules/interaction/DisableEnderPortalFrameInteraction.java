package me.trae.clans.world.modules.interaction;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.world.modules.shared.interfaces.DisableBlockInteraction;
import org.bukkit.Material;

public class DisableEnderPortalFrameInteraction extends SpigotListener<Clans, WorldManager> implements DisableBlockInteraction {

    public DisableEnderPortalFrameInteraction(final WorldManager manager) {
        super(manager);
    }

    @Override
    public Material getMaterial() {
        return Material.ENDER_PORTAL_FRAME;
    }

    @Override
    public boolean isInform() {
        return false;
    }
}