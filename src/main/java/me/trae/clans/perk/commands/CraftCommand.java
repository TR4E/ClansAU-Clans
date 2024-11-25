package me.trae.clans.perk.commands;

import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.core.client.Client;
import me.trae.core.command.types.types.DonationPerkCommand;
import me.trae.core.gamer.Gamer;
import me.trae.core.perk.Perk;
import me.trae.core.perk.types.TitanRank;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CraftCommand extends DonationPerkCommand<Clans, PerkManager> {

    public CraftCommand(final PerkManager manager) {
        super(manager, "craft", new String[]{"workbench"});
    }

    @Override
    public List<Class<? extends Perk<?, ?>>> getRequiredPerkClasses() {
        return Collections.singletonList(TitanRank.class);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        player.openWorkbench(null, true);
    }
}