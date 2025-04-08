package me.trae.clans.fields.modules.loot;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.clans.fields.events.FieldsLootEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.weapon.Weapon;
import me.trae.core.weapon.registry.WeaponRegistry;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class HandleFieldsEnderChestLoot extends SpigotListener<Clans, FieldsManager> {

    @ConfigInject(type = Integer.class, path = "Legendary-Min-Chance", defaultValue = "0")
    private int legendaryMinChance;

    @ConfigInject(type = Integer.class, path = "Legendary-Max-Chance", defaultValue = "10_000")
    private int legendaryMaxChance;

    @ConfigInject(type = Integer.class, path = "Legendary-Base-Chance", defaultValue = "9_950")
    private int legendaryBaseChance;

    @ConfigInject(type = Integer.class, path = "Legendary-Frenzy-Chance", defaultValue = "9_850")
    private int legendaryFrenzyChance;

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

            final FieldsLootEvent fieldsLootEvent = new FieldsLootEvent(material, event.getPlayer());
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

                final int legendaryBaseChance = event.isMiningMadness() ? this.legendaryFrenzyChance : this.legendaryBaseChance;

                if (!(UtilMath.getRandomNumber(Integer.class, this.legendaryMinChance, this.legendaryMaxChance) > legendaryBaseChance)) {
                    continue;
                }

                event.addLoot(weapon.getFinalBuilder().toItemStack());

                final Player player = event.getPlayer();

                for (final Player target : UtilServer.getOnlinePlayers()) {
                    final PlayerDisplayNameEvent playerDisplayNameEvent = UtilServer.getEvent(new PlayerDisplayNameEvent(player, target));

                    UtilMessage.simpleMessage(target, "Fields", "<var> has obtained a <var> from an Enderchest!", Arrays.asList(playerDisplayNameEvent.getPlayerName(), weapon.getDisplayName()));
                }
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