package net.clansau.clans.config;

import net.clansau.clans.Clans;
import net.clansau.core.config.Config;
import net.clansau.core.framework.Manager;
import net.clansau.core.framework.Module;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager extends Manager {

    private final Map<ConfigType, Config> configs;

    public ConfigManager(final Clans instance) {
        super(instance, "Config Manager");
        this.configs = new HashMap<>();
        this.loadConfigs();
    }

    @Override
    protected void registerModules() {
    }

    public void loadConfigs() {
        for (final ConfigType type : ConfigType.values()) {
            final Config config = new Config(getInstance().getDataFolder(), type.getName());
            if (!(config.fileExists())) {
                handleConfig(config.getConfig(), type);
                config.getConfig().options().copyDefaults(true);
                config.createFile();
            }
            config.loadFile();
            config.saveFile();
            this.getConfigs().put(type, config);
        }
    }

    public void updateModules() {
        final Config config = this.getConfig(ConfigType.MODULES_CONFIG);
        config.loadFile();
        config.saveFile();
        int count = 0;
        for (final String managerName : config.getConfig().getKeys(false)) {
            final Manager manager = getInstance().getPluginManager(managerName);
            if (manager == null) {
                config.getConfig().set(managerName, null);
                continue;
            }
            for (final String moduleName : config.getConfig().getConfigurationSection(managerName).getKeys(true)) {
                if (manager.getModule(moduleName) == null) {
                    config.getConfig().set(managerName + "." + moduleName, null);
                }
            }
        }
        for (final Manager manager : getInstance().getPluginManagers()) {
            for (final Module<?> module : manager.getModules()) {
                if (config.getConfig().contains(manager.getName() + "." + module.getName())) {
                    continue;
                }
                config.getConfig().set(manager.getName() + "." + module.getName(), true);
                count++;
            }
        }
        config.saveFile();
        if (count > 0) {
            UtilMessage.log("Config", "Added " + ChatColor.YELLOW + count + ChatColor.GRAY + " Modules to Configuration.");
        }
    }

    private void handleConfig(final YamlConfiguration yml, final ConfigType type) {
        switch (type) {
            case MAIN_CONFIG: {
                break;
            }
        }
    }

    public final Map<ConfigType, Config> getConfigs() {
        return this.configs;
    }

    public final Config getConfig(final ConfigType type) {
        return this.getConfigs().get(type);
    }

    public enum ConfigType {
        MAIN_CONFIG("Config"),
        MODULES_CONFIG("Modules");

        private final String name;

        ConfigType(final String name) {
            this.name = name;
        }

        public final String getName() {
            return this.name;
        }
    }
}