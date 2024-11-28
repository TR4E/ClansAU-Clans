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
import me.trae.core.utility.UtilTime;
import me.trae.core.weapon.registry.WeaponRegistry;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Koth extends SpigotModule<Clans, KothManager> implements IKoth {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "600_000")
    private long duration;

    @ConfigInject(type = Long.class, path = "Chest-Remove-Duration", defaultValue = "300_000")
    private long chestRemoveDuration;

    @ConfigInject(type = Long.class, path = "Chest-Locked-Duration", defaultValue = "30_000")
    public long chestLockedDuration;

    @ConfigInject(type = Long.class, path = "Chest-Opened-Duration", defaultValue = "10_000")
    public long chestOpenedDuration;

    private SupplyCrateData DATA;

    public long chestLockedSystemTime;

    private final Map<UUID, Long> CHEST_OPENER_MAP = new HashMap<>();

    public Koth(final KothManager manager) {
        super(manager);
    }

    @Override
    public boolean isActive() {
        return this.DATA != null;
    }

    @Override
    public boolean isChestLocked() {
        if (this.chestLockedSystemTime == 0L) {
            return true;
        }

        return !(UtilTime.elapsed(this.chestLockedSystemTime, this.chestLockedDuration));
    }

    @Override
    public Map<UUID, Long> getChestOpenerMap() {
        return this.CHEST_OPENER_MAP;
    }

    @Override
    public void addChestOpener(final Player player) {
        this.getChestOpenerMap().put(player.getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void removeChestOpener(final Player player) {
        this.getChestOpenerMap().remove(player.getUniqueId());
    }

    @Override
    public boolean canOpenChest(final Player player) {
        if (!(this.getChestOpenerMap().containsKey(player.getUniqueId()))) {
            return false;
        }

        final long systemTime = this.getChestOpenerMap().get(player.getUniqueId());
        if (systemTime == -1L) {
            return true;
        }

        return UtilTime.elapsed(systemTime, this.chestOpenedDuration);
    }

    @Override
    public boolean isChestOpener(final Player player) {
        return this.getChestOpenerMap().containsKey(player.getUniqueId());
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

        this.reset();

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

        this.reset();

        UtilMessage.simpleBroadcast("KoTH", "<red><bold>The KoTH has Ended!", null);
    }

    @Override
    public List<SupplyCrateData> getData() {
        if (this.DATA == null) {
            return new ArrayList<>();
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
    public long getChestRemoveDuration() {
        return this.duration + this.chestLockedDuration + this.chestRemoveDuration;
    }

    @Override
    public void onChestSpawn() {
        this.chestLockedSystemTime = System.currentTimeMillis();
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

    @Override
    public void stopSupplyCrate(final SupplyCrateData data) {
        IKoth.super.stopSupplyCrate(data);
        this.reset();
    }

    private void reset() {
        this.DATA = null;
        this.chestLockedSystemTime = 0L;
        this.CHEST_OPENER_MAP.clear();
    }
}