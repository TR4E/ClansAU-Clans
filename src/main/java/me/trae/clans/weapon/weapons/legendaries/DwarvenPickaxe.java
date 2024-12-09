package me.trae.clans.weapon.weapons.legendaries;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.energy.EnergyManager;
import me.trae.core.utility.*;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.events.WeaponLocationEvent;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DwarvenPickaxe extends Legendary<Clans, WeaponManager, WeaponData> implements Listener {

    @ConfigInject(type = Float.class, path = "Energy-Needed", defaultValue = "30.0")
    private float energyNeeded;

    @ConfigInject(type = Float.class, path = "Energy-Using", defaultValue = "3.0")
    private float energyUsing;

    @ConfigInject(type = Integer.class, path = "Durability-Min-Chance", defaultValue = "1")
    private int durabilityMinChance;

    @ConfigInject(type = Integer.class, path = "Durability-Max-Chance", defaultValue = "100")
    private int durabilityMaxChance;

    @ConfigInject(type = Integer.class, path = "Durability-Base-Chance", defaultValue = "80")
    private int durabilityBaseChance;

    @ConfigInject(type = Integer.class, path = "Take-Durability", defaultValue = "2")
    private int takeDurability;

    public DwarvenPickaxe(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_PICKAXE));
    }

    @Override
    public int getModel() {
        return 254875;
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "This pickaxe will instantly",
                "break any stone related blocks.",
                "",
                "Can only be used in your own territory.",
                "",
                UtilString.pair("<gray>Ability", "<yellow>Instant Mine")
        };
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Player player = event.getPlayer();

        if (!(this.hasWeaponByPlayer(player))) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (!(UtilBlock.isStoneRelatedBlock(block))) {
            return;
        }

        if (!(this.canBreakBlock(player, block))) {
            return;
        }

        final EnergyManager energyManager = this.getInstance(Core.class).getManagerByClass(EnergyManager.class);

        if (!(this.isUserByPlayer(player))) {
            if (energyManager.isExhausted(player, this.getName(), this.energyNeeded, true)) {
                return;
            }
        }

        if (!(energyManager.use(player, this.getName(), this.energyUsing, false))) {
            this.removeUser(player);
            return;
        }

        if (this.takeDurability > 0) {
            if (UtilMath.getRandomNumber(Integer.class, this.durabilityMinChance, this.durabilityMaxChance) > this.durabilityBaseChance) {
                UtilItem.takeDurability(player, player.getEquipment().getItemInHand(), this.takeDurability, false, true);
            }
        }

        this.addUser(new WeaponData(player, 1000L));

        UtilBlock.playBrokenEffect(block);

        block.breakNaturally();
    }

    private boolean canBreakBlock(final Player player, final Block block) {
        if (UtilServer.getEvent(new WeaponLocationEvent(this, block.getLocation())).isCancelled()) {
            return false;
        }

        final Clan territoryClan = this.getInstance().getManagerByClass(ClanManager.class).getClanByLocation(block.getLocation());
        if (territoryClan == null || !(territoryClan.isMemberByPlayer(player))) {
            return false;
        }

        return true;
    }
}