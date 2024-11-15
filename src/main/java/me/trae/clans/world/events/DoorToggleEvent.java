package me.trae.clans.world.events;

import me.trae.clans.world.events.interfaces.IDoorToggleEvent;
import me.trae.core.event.CustomCancellableEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DoorToggleEvent extends CustomCancellableEvent implements IDoorToggleEvent {

    private final Block block;
    private final Player player;

    public DoorToggleEvent(final Block block, final Player player) {
        this.block = block;
        this.player = player;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setOpen(final boolean open) {
        byte data = this.getBlock().getData();

        if (this.getBlock().getType() == Material.IRON_TRAPDOOR) {
            if (open) {
                data = (byte) (data | 4);
            } else {
                data &= -5;
            }
        } else {
            if (open) {
                data += 4;
            } else {
                data -= 4;
            }
        }

        this.getBlock().setData(data);
    }

    @Override
    public boolean isOpened() {
        final byte data = this.getBlock().getData();

        if (this.getBlock().getType() == Material.IRON_TRAPDOOR) {
            return (data & 4) == 4;
        }

        return data >= 4;
    }
}