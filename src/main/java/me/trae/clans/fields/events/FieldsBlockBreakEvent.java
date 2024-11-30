package me.trae.clans.fields.events;

import me.trae.clans.fields.data.FieldsBlock;
import me.trae.clans.fields.events.abstracts.FieldsCancellableEvent;
import me.trae.core.event.types.IBlockEvent;
import me.trae.core.event.types.IPlayerEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FieldsBlockBreakEvent extends FieldsCancellableEvent implements IBlockEvent, IPlayerEvent {

    private final Block block;
    private final Player player;

    public FieldsBlockBreakEvent(final FieldsBlock fieldsBlock, final Block block, final Player player) {
        super(fieldsBlock);

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
}