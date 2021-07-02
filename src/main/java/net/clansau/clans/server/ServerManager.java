package net.clansau.clans.server;

import net.clansau.clans.Clans;
import net.clansau.clans.server.listeners.CustomTablist;
import net.clansau.clans.server.listeners.ServerListener;
import net.clansau.core.framework.Manager;

public class ServerManager extends Manager {

    public ServerManager(final Clans instance) {
        super(instance, "Server Manager");
    }

    @Override
    protected void registerModules() {
        addModule(new ServerListener(this));
        addModule(new CustomTablist(this));
    }
}