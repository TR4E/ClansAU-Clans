package me.trae.clans.clan.modules.spawn;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.spawn.events.SpawnLocationEvent;
import me.trae.core.utility.UtilMath;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HandleClansSpawnLocation extends SpigotListener<Clans, ClanManager> {

    private final List<String> SPAWN_CLAN_NAMES = Arrays.asList("Blue Spawn", "Red Spawn");

    public HandleClansSpawnLocation(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onSpawnLocation(final SpawnLocationEvent event) {
        final Optional<Clan> randomSpawnClanOptional = this.getRandomSpawnClan();
        if (!(randomSpawnClanOptional.isPresent())) {
            return;
        }

        final Clan spawnClan = randomSpawnClanOptional.get();
        if (!(spawnClan.hasHome())) {
            return;
        }

        final ChatColor chatColor = this.getChatColorByName(spawnClan.getDisplayName().split(" ")[0]);

        event.setName(chatColor + spawnClan.getDisplayName());
        event.setLocation(spawnClan.getHome());
    }

    private Optional<Clan> getRandomSpawnClan() {
        final String clanName = this.SPAWN_CLAN_NAMES.get(UtilMath.getRandomNumber(Integer.class, 0, SPAWN_CLAN_NAMES.size() - 1));

        final Clan randomSpawnClan = this.getManager().getClanByName(clanName);
        if (randomSpawnClan == null) {
            return Optional.empty();
        }

        return Optional.of(randomSpawnClan);
    }

    private ChatColor getChatColorByName(final String name) {
        return ChatColor.valueOf(name);
    }
}