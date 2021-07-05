package net.clansau.clans.clans.commands.framework;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.Module;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public abstract class IClanCommand extends Module<ClanManager> {

    public IClanCommand(final ClanManager manager) {
        super(manager, "IClanCommand");
        setName(this.getClass().getSimpleName());
    }

    @Override
    protected final Clans getInstance() {
        return (Clans) super.getInstance();
    }

    @Override
    protected void initializeModule() {
    }

    @Override
    protected void shutdownModule() {
    }

    public void execute(final Player player, final String[] args) {
        if (!(isEnabled())) {
            UtilMessage.message(player, "Clans", "This Command is not currently enabled.");
            return;
        }
        this.run(player, args);
    }

    protected abstract void run(final Player player, final String[] args);
}