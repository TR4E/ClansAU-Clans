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

    private final List<String> SPAWN_CLAN_NAMES = Arrays.asList("Blue_Spawn", "Red_Spawn");

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

        final String spawnClanDisplayName = spawnClan.getDisplayName();

        final ChatColor chatColor = this.getChatColorByName(spawnClanDisplayName.split(" ")[0]);

        event.setName(chatColor + spawnClanDisplayName);
        event.setLocation(spawnClan.getHome());
    }

    private Optional<Clan> getRandomSpawnClan() {
        final String clanName = this.SPAWN_CLAN_NAMES.get(UtilMath.getRandomNumber(Integer.class, 0, SPAWN_CLAN_NAMES.size()));

        final Clan randomSpawnClan = this.getManager().getClanByName(clanName);
        if (randomSpawnClan == null) {
            return Optional.empty();
        }

        return Optional.of(randomSpawnClan);
    }

    private ChatColor getChatColorByName(final String name) {
        return ChatColor.valueOf(name.toUpperCase());
    }
}