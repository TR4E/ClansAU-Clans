package net.clansau.clans.fishing;

import net.clansau.clans.Clans;
import net.clansau.clans.fishing.commands.FishCommand;
import net.clansau.clans.fishing.enums.FishNames;
import net.clansau.clans.fishing.enums.TopRating;
import net.clansau.clans.fishing.listeners.*;
import net.clansau.core.framework.Manager;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMath;
import net.clansau.core.utility.UtilTime;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class FishingManager extends Manager {

    private final List<Fish> fishList;

    public FishingManager(final Clans instance) {
        super(instance, "Fishing Manager");
        this.fishList = new ArrayList<>();
    }

    @Override
    protected void registerModules() {
        addModule(new FishCommand(this));

        addModule(new CustomFishingHandler(this));
        addModule(new HookEnergy(this));
        addModule(new HookVelocity(this));
        addModule(new OnlyFishAtLake(this));
        addModule(new PreFishingHandler(this));
    }

    public final List<Fish> getFishList() {
        return this.fishList;
    }

    public void addFish(final Fish fish) {
        this.getFishList().add(fish);
    }

    public void sort() {
        this.getFishList().sort((o1, o2) -> o2.getSize() - o1.getSize());
    }

    public final List<Fish> getTop(final TopRating rating) {
        this.sort();
        final List<Fish> list = new ArrayList<>();
        if (rating.equals(TopRating.TOP_10)) {
            for (int i = 0; i < 10; i++) {
                if (this.getFishList().size() <= i) {
                    continue;
                }
                list.add(this.getFishList().get(i));
            }
            return list;
        }
        for (final Fish fish : this.getFishList()) {
            if (UtilTime.elapsed(fish.getSysTime(), rating.getDuration())) {
                continue;
            }
            list.add(fish);
        }
        return list;
    }

    public final boolean isTop(final Fish fish, final TopRating rating) {
        if (rating == null) {
            return this.getFishList().get(0).equals(fish);
        }
        return this.getTop(rating).contains(fish);
    }

    public final String getRandomFish() {
        return UtilFormat.cleanString(FishNames.values()[UtilMath.randomInt(FishNames.values().length - 1)].name());
    }

    public final EntityType getRandomEntityType() {
        final EntityType[] types = new EntityType[]{EntityType.CREEPER, EntityType.ZOMBIE, EntityType.WITCH, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.CAVE_SPIDER, EntityType.SPIDER, EntityType.GHAST};
        return types[UtilMath.randomInt(0, types.length - 1)];
    }
}