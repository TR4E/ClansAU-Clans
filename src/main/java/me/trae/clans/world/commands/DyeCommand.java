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
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DyeCommand extends Command<Clans, WorldManager> implements PlayerCommandType {

    private final List<Material> MATERIALS = Arrays.asList(Material.WOOL, Material.CARPET, Material.GLASS, Material.STAINED_GLASS, Material.THIN_GLASS, Material.STAINED_GLASS_PANE, Material.STAINED_CLAY, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);

    public DyeCommand(final WorldManager manager) {
        super(manager, "dye", new String[]{"color", "colour"}, Rank.DEFAULT);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        final ItemStack itemStack = player.getEquipment().getItemInHand();

        if (!(this.canUpdateItemStack(itemStack))) {
            UtilMessage.message(player, "Dye", "You are not holding a valid Material!");
            UtilMessage.simpleMessage(player, "Dye", "Available Materials: [<var><reset>]", Collections.singletonList(this.MATERIALS.stream().map(material -> String.format("<yellow>%s", UtilString.clean(material.name()))).collect(Collectors.joining("<gray>, "))));
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Dye", "You did not input a Color.");
            UtilMessage.simpleMessage(player, "Dye", "Available Colors: [<var><reset>]", Collections.singletonList(Arrays.stream(DyeColor.values()).map(dyeColor -> String.format("<yellow>%s", UtilString.clean(dyeColor.name()))).collect(Collectors.joining("<gray>, "))));
            return;
        }

        if (args.length == 1) {
            final DyeColor dyeColor = UtilItem.searchDyeColor(player, args[0], true);
            if (dyeColor == null) {
                return;
            }

            this.updateItemStack(itemStack, dyeColor);

            player.getEquipment().setItemInHand(UtilItem.updateItemStack(player.getEquipment().getItemInHand()));

            UtilMessage.simpleMessage(player, "Dye", "You dyed <yellow><var></yellow> to <green><var></green>.", Arrays.asList(UtilString.clean(itemStack.getType().name()), UtilString.clean(dyeColor.name())));
        }
    }

    private boolean canUpdateItemStack(final ItemStack itemStack) {
        return itemStack != null && this.MATERIALS.contains(itemStack.getType());
    }

    private void updateItemStack(final ItemStack itemStack, final DyeColor dyeColor) {
        if (ArmourMaterialType.LEATHER.isValid(itemStack.getType())) {
            UtilItem.updateColorItemStack(itemStack, dyeColor.getColor());
            return;
        }

        switch (itemStack.getType()) {
            case GLASS:
                itemStack.setType(Material.STAINED_GLASS);
                break;
            case THIN_GLASS:
                itemStack.setType(Material.STAINED_GLASS_PANE);
                break;
        }

        switch (itemStack.getType()) {
            case WOOL:
            case CARPET:
            case GLASS:
            case STAINED_GLASS:
            case THIN_GLASS:
            case STAINED_GLASS_PANE:
            case STAINED_CLAY:
                itemStack.setDurability(dyeColor.getWoolData());
                break;
        }
    }
}