package me.trae.clans.perk.perks;

import me.trae.champions.utility.UtilRole;
import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.clans.perk.perks.interfaces.IAgilityHelmet;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.perk.Perk;
import me.trae.core.updater.interfaces.Updater;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AgilityHelmet extends Perk<Clans, PerkManager> implements IAgilityHelmet, Listener, Updater {

    private final Map<UUID, Boolean> ACTIVE = new HashMap<>();

    @ConfigInject(type = String.class, path = "Material", defaultValue = "MOB_SPAWNER")
    private String material;

    public AgilityHelmet(final PerkManager manager) {
        super(manager);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Allow quick mobility.",
                "Left-Click with an empty hand to Leap."
        };
    }

    @Override
    public Map<UUID, Boolean> getActive() {
        return this.ACTIVE;
    }

    @Override
    public Material getMaterial() {
        try {
            return Material.valueOf(this.material);
        } catch (final Exception ignored) {
        }

        return Material.MOB_SPAWNER;
    }

    @Override
    public boolean isEquipped(final Player player) {
        final ItemStack helmetItemStack = player.getEquipment().getHelmet();
        if (helmetItemStack == null || helmetItemStack.getType() != this.getMaterial()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isUsing(final Player player) {
        if (!(this.isUserByPlayer(player))) {
            return false;
        }

        if (!(this.ACTIVE.containsKey(player.getUniqueId()))) {
            return false;
        }

        if (!(this.isEquipped(player))) {
            return false;
        }

        return true;
    }

    @Override
    public void unEquip(final Player player) {
        final ItemStack helmetItemStack = player.getEquipment().getHelmet();
        if (helmetItemStack == null || helmetItemStack.getType() != this.getMaterial()) {
            return;
        }

        player.getEquipment().setHelmet(null);

        UtilRole.playEffect(player, "None", false);
    }
}