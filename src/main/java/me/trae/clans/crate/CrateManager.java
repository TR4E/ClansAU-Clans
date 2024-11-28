package me.trae.clans.crate;

import me.trae.clans.Clans;
import me.trae.clans.crate.commands.CrateCommand;
import me.trae.clans.crate.interfaces.ICrateManager;
import me.trae.clans.crate.modules.HandleCrateItemStackUpdate;
import me.trae.clans.crate.modules.HandleCrateOpen;
import me.trae.clans.crate.modules.HandleCratePreview;
import me.trae.clans.crate.modules.HandleVotingCrateReceiveOnPlayerVote;
import me.trae.clans.crate.types.VotingCrate;
import me.trae.core.framework.SpigotManager;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilJava;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CrateManager extends SpigotManager<Clans> implements ICrateManager {

    public CrateManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new CrateCommand(this));

        // Modules
        addModule(new HandleCrateItemStackUpdate(this));
        addModule(new HandleCrateOpen(this));
        addModule(new HandleCratePreview(this));
        addModule(new HandleVotingCrateReceiveOnPlayerVote(this));

        // Crates
        addModule(new VotingCrate(this));
    }

    @Override
    public Crate getCrateByItemStack(final ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.hasItemMeta()) {
                for (final Crate crate : this.getModulesByClass(Crate.class)) {
                    if (!(UtilItem.isSimilarWithItemMeta(itemStack, crate.getItemBuilder().toItemStack()))) {
                        continue;
                    }

                    return crate;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isCrateByItemStack(final ItemStack itemStack) {
        return this.getCrateByItemStack(itemStack) != null;
    }

    @Override
    public Crate searchCrate(final CommandSender sender, final String name, final boolean inform) {
        final List<Predicate<Crate>> predicates = Arrays.asList(
                (crate -> crate.getSlicedName().equalsIgnoreCase(name)),
                (crate -> crate.getSlicedName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<Crate, String> function = Crate::getName;

        return UtilJava.search(this.getModulesByClass(Crate.class), predicates, null, function, "Crate Search", sender, name, inform);
    }
}