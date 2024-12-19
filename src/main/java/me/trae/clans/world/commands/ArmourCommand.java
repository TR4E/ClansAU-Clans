package me.trae.clans.world.commands;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.enums.ArmourMaterialType;
import me.trae.core.utility.enums.ArmourSlotType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ArmourCommand extends Command<Clans, WorldManager> implements PlayerCommandType {

    public ArmourCommand(final WorldManager manager) {
        super(manager, "armour", new String[]{"armor", "autoarmour", "autoarmor"}, Rank.DEFAULT);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        final ItemStack itemStack = player.getEquipment().getItemInHand();

        final ArmourMaterialType materialType = this.getArmourMaterialType(itemStack.getType());
        if (materialType == null) {
            UtilMessage.message(player, "Armour", "You are not holding a valid Material!");
            return;
        }

        final int requiredAmount = 24;

        if (!(UtilItem.contains(player, itemStack, requiredAmount))) {
            UtilMessage.simpleMessage(player, "Armour", "You have insufficient <yellow><var></yellow> to craft <green><var> Armour</green>.", Arrays.asList(UtilString.clean(itemStack.getType().name()), materialType.getName()));
            return;
        }

        UtilItem.remove(player, itemStack, requiredAmount);

        for (final ArmourSlotType slotType : ArmourSlotType.values()) {
            final ItemStack armourItemStack = UtilItem.toItemStackByArmourType(materialType, slotType);

            UtilItem.insert(player, armourItemStack);
        }

        UtilMessage.simpleMessage(player, "Armour", "You crafted <green><var> Armour</green> from <yellow><var>x <var></yellow>.", Arrays.asList(materialType.getName(), String.valueOf(requiredAmount), UtilString.clean(itemStack.getType().name())));
    }

    private ArmourMaterialType getArmourMaterialType(final Material material) {
        switch (material) {
            case LEATHER:
                return ArmourMaterialType.LEATHER;
            case GOLD_INGOT:
                return ArmourMaterialType.GOLD;
            case IRON_INGOT:
                return ArmourMaterialType.IRON;
            case EMERALD:
                return ArmourMaterialType.CHAINMAIL;
            case DIAMOND:
                return ArmourMaterialType.DIAMOND;
        }

        return null;
    }
}