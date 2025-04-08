package me.trae.clans.fields.events;

import me.trae.clans.fields.events.interfaces.IFieldsLootEvent;
import me.trae.core.event.CustomEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FieldsLootEvent extends CustomEvent implements IFieldsLootEvent {

    private final Material blockMaterial;
    private final Player player;

    private List<ItemStack> loot;
    private int multiplier;

    public FieldsLootEvent(final Material blockMaterial, final Player player) {
        this.blockMaterial = blockMaterial;
        this.player = player;
        this.loot = new ArrayList<>();
        this.multiplier = 1;
    }

    @Override
    public Material getBlockMaterial() {
        return this.blockMaterial;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public List<ItemStack> getLoot() {
        return this.loot;
    }

    @Override
    public void setLoot(final List<ItemStack> loot) {
        this.loot = loot;
    }

    @Override
    public void addLoot(final ItemStack itemStack) {
        this.getLoot().add(itemStack);
    }

    @Override
    public int getMultiplier() {
        return this.multiplier;
    }

    @Override
    public void setMultiplier(final int multiplier) {
        this.multiplier = multiplier;
    }
}