package me.trae.clans.clan.data.interfaces;

import me.trae.clans.clan.data.enums.MemberRole;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IMember {

    default Player getOnlinePlayer() {
        return Bukkit.getPlayer(this.getUUID());
    }

    default OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.getUUID());
    }

    default boolean isOnline() {
        return this.getOnlinePlayer() != null;
    }

    UUID getUUID();

    MemberRole getRole();

    void setRole(final MemberRole memberRole);

    boolean hasRole(final MemberRole memberRole);
}