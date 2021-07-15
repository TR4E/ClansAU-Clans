package net.clansau.clans.clans;

import net.clansau.core.framework.Module;
import net.clansau.core.framework.Primitive;

public class ClanModule extends Module<ClanManager> {

    public ClanModule(final ClanManager manager) {
        super(manager, "Clan Module");
        addPrimitive("FirstDay", new Primitive<>(false));
        addPrimitive("LastDay", new Primitive<>(false));
        addPrimitive("PillageLength", new Primitive<>(10));
        addPrimitive("TNTProtection", new Primitive<>(30));
        addPrimitive("MaxNameLength", new Primitive<>(14));
        addPrimitive("MinNameLength", new Primitive<>(3));
        addPrimitive("MaxClanMembers", new Primitive<>(8));
        addPrimitive("MaxClanClaims", new Primitive<>(8));
    }

    @Override
    protected void initializeModule() {
    }

    @Override
    protected void shutdownModule() {
    }
}