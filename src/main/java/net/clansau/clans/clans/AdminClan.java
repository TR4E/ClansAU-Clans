package net.clansau.clans.clans;

import net.clansau.clans.Clans;

public class AdminClan extends Clan {

    private boolean safe;

    public AdminClan(final Clans instance, final String name) {
        super(instance, name);
    }

    public final boolean isSafe() {
        return this.safe;
    }

    public void setSafe(final boolean safe) {
        this.safe = safe;
    }
}