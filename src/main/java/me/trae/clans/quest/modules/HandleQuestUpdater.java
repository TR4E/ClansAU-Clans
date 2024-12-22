package me.trae.clans.quest.modules;

import me.trae.clans.Clans;
import me.trae.clans.config.ConfigManager;
import me.trae.clans.quest.QuestManager;
import me.trae.core.config.abstracts.AbstractConfig;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilTime;
import org.bukkit.configuration.file.YamlConfiguration;

public class HandleQuestUpdater extends SpigotUpdater<Clans, QuestManager> {

    private final String SYSTEM_TIME_PATH = "Daily-Quest-System-Time";

    private final AbstractConfig CONFIG;

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "86_400_000")
    private long duration;

    public HandleQuestUpdater(final QuestManager manager) {
        super(manager);

        this.CONFIG = this.getInstance().getManagerByClass(ConfigManager.class).getConfigurationByName(null, "Data");

        if (!(this.CONFIG.getYamlConfiguration().contains(this.SYSTEM_TIME_PATH))) {
            this.updateSystemTime();
        }
    }

    @Update(delay = 10_000L)
    public void onUpdater() {
        if (!(UtilTime.elapsed(this.getOrCreateSystemTime(), this.duration))) {
            return;
        }

        this.getManager().resetQuests();

        this.updateSystemTime();
    }

    private void updateSystemTime() {
        this.CONFIG.getYamlConfiguration().set(this.SYSTEM_TIME_PATH, System.currentTimeMillis());

        this.CONFIG.saveFile();
    }

    private long getOrCreateSystemTime() {
        final YamlConfiguration yamlConfiguration = this.CONFIG.getYamlConfiguration();

        if (!(yamlConfiguration.contains(this.SYSTEM_TIME_PATH))) {
            this.updateSystemTime();
        }

        return yamlConfiguration.getLong(this.SYSTEM_TIME_PATH);
    }
}