package me.trae.clans.config;

import me.trae.clans.Clans;
import me.trae.core.config.abstracts.AbstractConfig;
import me.trae.core.utility.UtilPlugin;

public class Config extends AbstractConfig {

    public Config(final String folderName, final String name) {
        super(UtilPlugin.getInstanceByClass(Clans.class), folderName, name);
    }
}