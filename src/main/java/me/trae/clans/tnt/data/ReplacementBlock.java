package me.trae.clans.tnt.data;

import me.trae.clans.tnt.data.interfaces.IReplacementBlock;
import org.bukkit.Material;

public class ReplacementBlock implements IReplacementBlock {

    private final Material material;
    private final byte data;

    public ReplacementBlock(final Material material, final int data) {
        this.material = material;
        this.data = (byte) data;
    }

    public ReplacementBlock(final Material material) {
        this(material, 0);
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public byte getData() {
        return this.data;
    }
}