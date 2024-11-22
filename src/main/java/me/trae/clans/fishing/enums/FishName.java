package me.trae.clans.fishing.enums;

import me.trae.clans.fishing.enums.interfaces.IFishName;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilString;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum FishName implements IFishName {

    BARRAMUNDI,
    SNAPPER,
    FLATHEAD,
    SALMON,
    TREVALLY,
    WHITING,
    BREAM,
    MULLOWAY,
    TUNA,
    MARLIN,
    TROUT,
    COD,
    GARFISH,
    SHARK,
    KINGFISH,
    TAILOR,
    SWORDFISH,
    COBIA,
    CORAL_TROUT;

    private final String name;

    FishName() {
        this.name = UtilString.clean(this.name());
    }

    public static FishName getRandom() {
        final FishName[] values = values();

        return values[UtilMath.getRandomNumber(Integer.class, 0, values.length)];
    }

    public static boolean isItemStack(final ItemStack itemStack) {
        if (itemStack.getType() == Material.RAW_FISH && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            for (final FishName fishName : values()) {
                if (!(itemStack.getItemMeta().getDisplayName().contains(fishName.getName()))) {
                    continue;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }
}