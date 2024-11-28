package me.trae.clans.world.modules.interaction.item;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import me.trae.core.world.modules.shared.interfaces.DisableItemInteraction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HandleFlintAndSteelInteract extends SpigotListener<Clans, WorldManager> implements DisableItemInteraction {

    public HandleFlintAndSteelInteract(final WorldManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public boolean canUse(final Player player, final ItemStack itemStack, final Block block) {
        if (block != null) {
            switch (block.getType()) {
                case NETHERRACK:
                case TNT:
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean isInform() {
        return true;
    }

    @Override
    public void inform(final Player player, final ItemStack itemStack, final Block block) {
        UtilMessage.simpleMessage(player, "Game", "You cannot use <yellow><var></yellow> on <green><var></green>.", Arrays.asList(UtilString.clean(itemStack.getType().name()), UtilBlock.getDisplayName(block)));
    }
}