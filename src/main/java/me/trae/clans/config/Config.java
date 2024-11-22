package me.trae.clans.config;

import me.trae.clans.Clans;
import me.trae.core.config.abstracts.AbstractConfig;
import me.trae.core.utility.UtilPlugin;

public class Config extends AbstractConfig {

    public Config(final String folderName, final String name) {
        super(UtilPlugin.getInstance(Clans.class), folderName, name);
    }
}