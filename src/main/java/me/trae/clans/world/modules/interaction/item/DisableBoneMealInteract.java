package me.trae.clans.world.modules.interaction.item;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.constants.ItemConstants;
import me.trae.core.world.modules.shared.interfaces.DisableItemInteraction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DisableBoneMealInteract extends SpigotListener<Clans, WorldManager> implements DisableItemInteraction {

    public DisableBoneMealInteract(final WorldManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return ItemConstants.BONE_MEAL;
    }

    @Override
    public boolean canUse(final Player player, final ItemStack itemStack, final Block block) {
        if (block != null) {
            switch (block.getType()) {
                case GRASS:
                case LONG_GRASS:
                case CROPS:
                case PUMPKIN_STEM:
                case MELON_STEM:
                case CARROT:
                case POTATO:
                case SAPLING:
                case BROWN_MUSHROOM:
                case RED_MUSHROOM:
                    return false;
            }
        }

        return true;
    }

    @Override
    public boolean isInform() {
        return true;
    }
}