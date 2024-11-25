package me.trae.clans.fields.modules.loot;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.clans.fields.events.FieldsLootEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilServer;
import me.trae.core.weapon.Weapon;
import me.trae.core.weapon.registry.WeaponRegistry;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class HandleFieldsEnderChestLoot extends SpigotListener<Clans, FieldsManager> {

    public HandleFieldsEnderChestLoot(final FieldsManager manager) {
        super(manager);
    }

    @EventHandler
    public void onFieldsLoot(final FieldsLootEvent event) {
        if (event.getBlockMaterial() != Material.ENDER_CHEST) {
            return;
        }

        this.handleOres(event);
        this.handleLegendary(event);
        this.handleChampionsPvPWeapon(event);
    }

    private void handleOres(final FieldsLootEvent event) {
        for (final Material material : Material.values()) {
            if (Arrays.asList(Material.GLOWING_REDSTONE_ORE, Material.LAPIS_ORE).contains(material)) {
                continue;
            }

            if (!(material.name().endsWith("_ORE"))) {
                continue;
            }

            final FieldsLootEvent fieldsLootEvent = new FieldsLootEvent(material);
            UtilServer.callEvent(fieldsLootEvent);

            for (int i = 0; i < UtilMath.getRandomNumber(Integer.class, 1, 3 + event.getMultiplier()); i++) {
                event.getLoot().addAll(fieldsLootEvent.getLoot());
            }
        }
    }

    private void handleLegendary(final FieldsLootEvent event) {
        for (int i = 0; i < event.getMultiplier(); i++) {
            for (final Weapon<?, ?, ?> weapon : WeaponRegistry.getWeapons()) {
                if (!(weapon instanceof Legendary<?, ?, ?>)) {
                    continue;
                }

                if (!(UtilMath.getRandomNumber(Integer.class, 0, 10000) > 9950)) {
                    continue;
                }

                event.addLoot(weapon.getFinalBuilder().toItemStack());
                break;
            }
        }
    }

    private void handleChampionsPvPWeapon(final FieldsLootEvent event) {
        for (int i = 0; i < event.getMultiplier(); i++) {
            final List<Material> materials = Arrays.asList(Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.GOLD_SWORD, Material.GOLD_AXE, Material.IRON_SWORD, Material.IRON_AXE);

            event.addLoot(new ItemStack(materials.get(UtilMath.getRandomNumber(Integer.class, 0, materials.size()))));
        }
    }
}