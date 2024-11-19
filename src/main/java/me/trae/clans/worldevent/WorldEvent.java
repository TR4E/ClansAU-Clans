package me.trae.clans.worldevent;

import me.trae.clans.Clans;
import me.trae.clans.worldevent.interfaces.IWorldEvent;
import me.trae.core.framework.SpigotModule;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Sound;

import java.util.Collections;

public abstract class WorldEvent extends SpigotModule<Clans, WorldEventManager> implements IWorldEvent {

    private long systemTime;

    public WorldEvent(final WorldEventManager manager) {
        super(manager);
    }

    @Override
    public String getDisplayName() {
        return String.format("<green>%s", this.getName());
    }

    @Override
    public long getSystemTime() {
        return this.systemTime;
    }

    @Override
    public boolean isActive() {
        return this.getSystemTime() > 0L;
    }

    @Override
    public void start() {
        this.getManager().setActiveWorldEvent(this);
        this.systemTime = System.currentTimeMillis();

        UtilServer.getOnlinePlayers().forEach(player -> UtilServer.callEvent(new ScoreboardUpdateEvent(player)));

        new SoundCreator(Sound.ENDERDRAGON_GROWL).play();

        UtilMessage.simpleBroadcast("World Event", "<var> has started!", Collections.singletonList(this.getDisplayName()));
    }

    @Override
    public void end() {
        this.getManager().setActiveWorldEvent(null);
        this.systemTime = -1L;

        UtilServer.getOnlinePlayers().forEach(player -> UtilServer.callEvent(new ScoreboardUpdateEvent(player)));

        new SoundCreator(Sound.WITHER_DEATH).play();

        UtilMessage.simpleBroadcast("World Event", "<var> has ended!", Collections.singletonList(this.getDisplayName()));
    }
}