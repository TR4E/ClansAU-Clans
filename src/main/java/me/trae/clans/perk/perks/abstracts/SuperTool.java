package me.trae.clans.perk.perks.abstracts;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.perk.PerkManager;
import me.trae.clans.perk.perks.abstracts.interfaces.ISuperTool;
import me.trae.clans.preference.PreferenceManager;
import me.trae.clans.preference.types.SuperToolActivation;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.perk.Perk;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class SuperTool extends Perk<Clans, PerkManager> implements ISuperTool, Listener {

    @ConfigInject(type = String.class, path = "Material", defaultValue = "")
    private final String material;

    @ConfigInject(type = Integer.class, path = "Take-Durability", defaultValue = "1")
    private int takeDurability;

    public SuperTool(final PerkManager manager) {
        super(manager);

        this.material = this.getDefaultMaterial().name();
    }

    @Override
    public Material getMaterial() {
        try {
            return Material.valueOf(this.material);
        } catch (final Exception ignored) {
        }

        return this.getDefaultMaterial();
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

        final ItemStack itemStack = player.getEquipment().getItemInHand();
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() != this.getMaterial()) {
            return;
        }

        if (!(this.isUserByPlayer(player))) {
            return;
        }

        if (!(this.getInstanceByClass().getManagerByClass(PreferenceManager.class).getModuleByClass(SuperToolActivation.class).getUserByPlayer(player).getValue())) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (!(this.isValidBlock(block))) {
            return;
        }

        if (!(this.canBreakBlock(player, block))) {
            return;
        }

        if (this.takeDurability > 0) {
            UtilItem.takeDurability(player, player.getEquipment().getItemInHand(), this.takeDurability, false, true);
        }

        UtilBlock.playBrokenEffect(block);

        block.breakNaturally();
    }

    private boolean canBreakBlock(final Player player, final Block block) {
        final Clan territoryClan = this.getInstanceByClass().getManagerByClass(ClanManager.class).getClanByLocation(block.getLocation());
        if (territoryClan == null || !(territoryClan.isMemberByPlayer(player))) {
            return false;
        }

        return true;
    }
}