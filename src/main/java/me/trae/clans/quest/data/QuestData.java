package me.trae.clans.quest.data;

import me.trae.clans.quest.Quest;
import me.trae.clans.quest.data.enums.QuestDataProperty;
import me.trae.clans.quest.data.interfaces.IQuestData;
import me.trae.core.database.containers.DataContainer;
import me.trae.core.database.query.constants.DefaultProperty;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QuestData implements IQuestData, DataContainer<QuestDataProperty> {

    private final UUID uuid;
    private final String type;

    private int progress;

    private QuestData(final UUID uuid, final String type, final int progress) {
        this.uuid = uuid;
        this.type = type;
        this.progress = progress;
    }

    public QuestData(final Quest quest, final Player player) {
        this(player.getUniqueId(), quest.getClass().getSimpleName(), 0);
    }

    public QuestData(final EnumData<QuestDataProperty> data) {
        this(UUID.fromString(data.get(String.class, DefaultProperty.KEY)), data.get(String.class, DefaultProperty.TYPE), data.get(Integer.class, QuestDataProperty.PROGRESS));
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getProgress() {
        return this.progress;
    }

    @Override
    public void setProgress(final int progress) {
        this.progress = progress;
    }

    @Override
    public void addProgress() {
        this.setProgress(this.getProgress() + 1);
    }

    @Override
    public List<QuestDataProperty> getProperties() {
        return Arrays.asList(QuestDataProperty.values());
    }

    @Override
    public Object getValueByProperty(final QuestDataProperty property) {
        if (Objects.requireNonNull(property) == QuestDataProperty.PROGRESS) {
            return this.getProgress();
        }

        return null;
    }
}