package me.trae.clans.crate.modules;

import me.trae.clans.Clans;
import me.trae.clans.crate.CrateManager;
import me.trae.clans.crate.types.VotingCrate;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import me.trae.core.vote.events.PlayerVoteEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Arrays;

public class HandleVotingCrateReceiveOnPlayerVote extends SpigotListener<Clans, CrateManager> {

    public HandleVotingCrateReceiveOnPlayerVote(final CrateManager manager) {
        super(manager);
    }

    @EventHandler
    public void onPlayerVote(final PlayerVoteEvent event) {
        final Player player = event.getPlayer();

        final VotingCrate crate = this.getManager().getModuleByClass(VotingCrate.class);
        if (crate == null) {
            return;
        }

        crate.give(player);

        UtilMessage.simpleBroadcast("Vote", "<yellow><var></yellow> has voted and received a <var>.", Arrays.asList(player.getName(), crate.getDisplayName()));
    }
}