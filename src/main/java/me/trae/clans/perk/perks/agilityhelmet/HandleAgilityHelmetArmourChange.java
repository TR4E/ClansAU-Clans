package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.clans.Clans;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotSubUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

public class HandleAgilityHelmetArmourChange extends SpigotSubUpdater<Clans, AgilityHelmet> {

    public HandleAgilityHelmetArmourChange(final AgilityHelmet module) {
        super(module);
    }

    @Update(delay = 200L)
    public void onUpdater() {
        for (final UUID uuid : this.getModule().getActive().keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }

            if (!(this.getModule().isUsing(player))) {
                continue;
            }

            if (this.isValid(player)) {
                continue;
            }

            final EntityEquipment equipment = player.getEquipment();

            if (equipment.getChestplate() != null) {
                UtilItem.insert(player, equipment.getChestplate());
                equipment.setChestplate(null);
            }

            if (equipment.getLeggings() != null) {
                UtilItem.insert(player, equipment.getLeggings());
                equipment.setLeggings(null);
            }

            if (equipment.getBoots() != null) {
                UtilItem.insert(player, equipment.getBoots());
                equipment.setBoots(null);
            }

            UtilMessage.message(player, this.getModule().getName(), "You cannot equip armour with agility helmet!");
        }
    }

    private boolean isValid(final Player player) {
        final EntityEquipment equipment = player.getEquipment();

        for (final ItemStack itemStack : Arrays.asList(equipment.getChestplate(), equipment.getLeggings(), equipment.getBoots())) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                return false;
            }
        }

        return true;
    }
}