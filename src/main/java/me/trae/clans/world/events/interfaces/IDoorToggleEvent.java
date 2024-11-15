package me.trae.clans.world.events.interfaces;

import me.trae.core.event.types.IBlockEvent;
import me.trae.core.event.types.IPlayerEvent;

public interface IDoorToggleEvent extends IBlockEvent, IPlayerEvent {

    void setOpen(final boolean open);

    boolean isOpened();
}