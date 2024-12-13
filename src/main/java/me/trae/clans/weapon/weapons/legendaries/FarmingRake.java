package me.trae.clans.weapon.weapons.legendaries;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.utility.*;
import me.trae.core.utility.enums.ActionType;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.ActiveLegendary;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.NetherWarts;

public class FarmingRake extends ActiveLegendary<Clans, WeaponManager, WeaponData> {

    @ConfigInject(type = Float.class, path = "Energy", defaultValue = "25.0")
    private float energy;

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "3_000")
    private long recharge;

    @ConfigInject(type = Integer.class, path = "Radius", defaultValue = "5")
    private int radius;

    public FarmingRake(final WeaponManager manager) {
        super(manager, new ItemStack(Material.IRON_HOE), ActionType.RIGHT_CLICK);
    }

    @Override
    public int getModel() {
        return 374728;
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public boolean cancelOriginalPlayerInteractEvent() {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "This mysterious tool will",
                "automatically harvest and replant any",
                "crops in your vicinity.",
                "",
                UtilString.pair("<gray>Active", "<yellow>Harvest")
        };
    }

    @Override
    public void onActivate(final Player player) {
        final Block targetBlock = UtilBlock.getBlockTarget(player);
        if (targetBlock == null) {
            return;
        }

        for (int x = -this.radius; x < this.radius; x++) {
            for (int z = -this.radius; z < this.radius; z++) {
                final Location location = new Location(targetBlock.getWorld(), targetBlock.getLocation().getBlockX() + x, targetBlock.getLocation().getBlockY(), targetBlock.getLocation().getBlockZ() + z);

                final Block block = location.getBlock();
                if (block == null || block.getType() == Material.AIR) {
                    continue;
                }

                switch (block.getType()) {
                    case CROPS:
                    case POTATO:
                    case CARROT:
                    case NETHER_WARTS:
                        this.handleBlock(player, block);
                        break;
                }
            }
        }
    }

    private void handleBlock(final Player player, final Block block) {
        if (!(this.isRipe(block))) {
            return;
        }

        final Material material = this.getItemByBlock(block.getType());
        if (material == null) {
            return;
        }

        UtilItem.insert(player, new ItemStack(material, UtilMath.getRandomNumber(Integer.class, 1, 3)));

        block.setType(block.getType());

        for (int i = 0; i < 3; i++) {
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
        }

        UtilItem.remove(player, new ItemStack(material), 1);
    }

    private boolean isRipe(final Block block) {
        if (block.getType() == Material.NETHER_WARTS) {
            return UtilJava.cast(NetherWarts.class, block.getState().getData()).getState() == NetherWartsState.RIPE;
        }

        return block.getData() == CropState.RIPE.getData();
    }

    private Material getItemByBlock(final Material material) {
        switch (material) {
            case POTATO:
                return Material.POTATO_ITEM;
            case CARROT:
                return Material.CARROT_ITEM;
            case CROPS:
                return Material.WHEAT;
            case NETHER_WARTS:
                return Material.NETHER_STALK;
        }

        return null;
    }

    @Override
    public float getEnergy() {
        return this.energy;
    }

    @Override
    public long getRecharge() {
        return this.recharge;
    }
}