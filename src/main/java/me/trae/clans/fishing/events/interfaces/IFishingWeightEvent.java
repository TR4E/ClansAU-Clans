package me.trae.clans.fishing.events.interfaces;

public interface IFishingWeightEvent extends IFishingEvent {

    int getWeight();

    void setWeight(final int weight);

    boolean hasWeight();
}