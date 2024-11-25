package me.trae.clans.worldevent.interfaces;

import me.trae.core.utility.components.time.ExpiredComponent;
import me.trae.core.utility.components.time.GetDurationComponent;
import me.trae.core.utility.components.time.GetSystemTimeComponent;
import me.trae.core.utility.components.time.RemainingComponent;

public interface IWorldEvent extends GetSystemTimeComponent, GetDurationComponent, RemainingComponent, ExpiredComponent {

    String getDisplayName();

    boolean isActive();

    void start();

    void end();
}