package me.trae.clans.fields.modules.loot;

import me.trae.api.champions.weapon.ChampionsPvPWeapon;
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
            if (material == Material.GLOWING_REDSTONE_ORE) {
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
            final ChampionsPvPWeapon championsPvPWeapon = WeaponRegistry.getRandomWeaponByClass(ChampionsPvPWeapon.class);

            event.addLoot(championsPvPWeapon.getFinalBuilder().toItemStack());
        }
    }
}