package me.trae.clans.worldevent.interfaces;

import me.trae.core.utility.components.ExpiredComponent;
import me.trae.core.utility.components.GetDurationComponent;
import me.trae.core.utility.components.GetSystemTimeComponent;
import me.trae.core.utility.components.RemainingComponent;

public interface IWorldEvent extends GetSystemTimeComponent, GetDurationComponent, RemainingComponent, ExpiredComponent {

    String getDisplayName();

    boolean isActive();

    void start();

    void end();
}