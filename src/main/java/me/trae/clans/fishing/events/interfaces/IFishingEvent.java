package me.trae.clans.fishing.events.interfaces;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.clans.worldevent.types.FishingFrenzy;
import me.trae.core.event.types.IPlayerEvent;
import me.trae.core.utility.UtilPlugin;
import org.bukkit.entity.FishHook;

public interface IFishingEvent extends IPlayerEvent {

    State getState();

    FishHook getHook();

    default boolean isFishingFrenzy() {
        return UtilPlugin.getInstance(Clans.class).getManagerByClass(WorldEventManager.class).isActiveWorldEvent(FishingFrenzy.class);
    }

    default boolean isInFields() {
        if (this.getHook() == null) {
            return false;
        }

        return UtilPlugin.getInstance(Clans.class).getManagerByClass(FieldsManager.class).isInFields(this.getHook().getLocation());
    }
}