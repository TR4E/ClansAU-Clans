package me.trae.clans.quest.data.interfaces;

import java.util.UUID;

public interface IQuestData {

    UUID getUUID();

    String getType();

    int getProgress();

    void setProgress(final int progress);

    void addProgress();
}