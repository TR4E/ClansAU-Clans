package me.trae.clans.koth;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.koth.interfaces.IKoth;
import me.trae.clans.weapon.weapons.items.data.SupplyCrateData;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.SpigotModule;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilMessage;
import me.trae.core.weapon.registry.WeaponRegistry;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Koth extends SpigotModule<Clans, KothManager> implements IKoth {

    private SupplyCrateData DATA;

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "60_000")
    private long duration;

    @ConfigInject(type = Long.class, path = "Chest-Duration", defaultValue = "60_000")
    private long chestDuration;


    public Koth(final KothManager manager) {
        super(manager);
    }

    @Override
    public boolean isActive() {
        return this.DATA != null;
    }

    @Override
    public void start() {
        if (this.DATA != null) {
            return;
        }

        final Clan fieldsClan = this.getInstance().getManagerByClass(ClanManager.class).getClanByName("Fields");
        if (fieldsClan == null) {
            return;
        }

        if (!(fieldsClan.hasHome())) {
            return;
        }

        final Location location = fieldsClan.getHome();

        this.DATA = new SupplyCrateData(location, this.getDuration());

        this.startSupplyCrate(location.getBlock(), Material.EMERALD_BLOCK);

        UtilMessage.simpleBroadcast("KoTH", "<green><bold>The KoTH has Started!", null);
    }

    @Override
    public void stop() {
        if (this.DATA == null) {
            return;
        }

        this.stopSupplyCrate(this.DATA);

        this.DATA = null;

        UtilMessage.simpleBroadcast("KoTH", "<red><bold>The KoTH has Ended!", null);
    }

    @Override
    public List<SupplyCrateData> getData() {
        if (this.DATA == null) {
            return null;
        }

        return Collections.singletonList(this.DATA);
    }

    @Override
    public String getName() {
        return "KoTH";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED + this.getName();
    }

    @Override
    public Material getMaterial() {
        return Material.BEACON;
    }

    @Override
    public Material getChestMaterial() {
        return Material.CHEST;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public long getChestDuration() {
        return this.chestDuration;
    }

    @Override
    public void fillChest(final Inventory inventory) {
        final boolean eotw = this.getInstance().getManagerByClass(ClanManager.class).eotw;

        for (final Material material : Arrays.asList(Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK)) {
            final int amount = UtilMath.getRandomNumber(Integer.class, 16, 32 * (eotw ? 2 : 1));

            inventory.addItem(UtilItem.updateItemStack(new ItemStack(material, amount)));
        }

        for (final Legendary<?, ?, ?> legendary : WeaponRegistry.getWeaponsByClass(Legendary.class)) {
            if (!(eotw)) {
                if (UtilMath.getRandomNumber(Integer.class, 0, 10000) <= 9995) {
                    continue;
                }
            }

            inventory.addItem(legendary.getFinalBuilder().toItemStack());
        }

        inventory.addItem(UtilItem.updateItemStack(new ItemStack(Material.TNT, UtilMath.getRandomNumber(Integer.class, 4, 16) * (eotw ? 2 : 1))));
    }
}