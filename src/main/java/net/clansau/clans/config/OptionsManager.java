package net.clansau.clans.config;

import net.clansau.clans.Clans;
import net.clansau.core.config.Config;
import net.clansau.core.config.IOptionsManager;
import net.clansau.core.framework.Module;
import org.bukkit.configuration.file.YamlConfiguration;

public class OptionsManager extends IOptionsManager {

    public OptionsManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void loadData() {
        this.getData().clear();
        for (final ConfigManager.ConfigType type : ConfigManager.ConfigType.values()) {
            final Config config = getInstance().getManager(ConfigManager.class).getConfig(type);
            config.loadFile();
            config.saveFile();
            this.handleConfig(config.getConfig(), type);
        }
    }

    private void handleConfig(final YamlConfiguration yml, final ConfigManager.ConfigType type) {
        switch (type) {
            case MAIN_CONFIG: {
                break;
            }
        }
    }

    @Override
    public final boolean isModuleEnabled(final Module<?> module) {
        final YamlConfiguration yml = getInstance().getManager(ConfigManager.class).getConfig(ConfigManager.ConfigType.MODULES_CONFIG).getConfig();
        String key = (module.getManager().getName() + "." + module.getName());
        if (!(yml.contains(key))) {
            yml.set(key, true);
        }
        key = key.replace(".", "-");
        if (!(getData().containsKey(key))) {
            addData(key, yml.getBoolean(key.replace("-", ".")));
        }
        return getData(key);
    }

    @Override
    public void setModuleEnabled(final Module<?> module, final boolean enabled) {
        final String key = (module.getManager().getName() + "-" + module.getName());
        addData(key, enabled);
        final Config config = getInstance().getManager(ConfigManager.class).getConfig(ConfigManager.ConfigType.MODULES_CONFIG);
        config.loadFile();
        config.getConfig().set(key.replace("-", "."), enabled);
        config.saveFile();
    }
}