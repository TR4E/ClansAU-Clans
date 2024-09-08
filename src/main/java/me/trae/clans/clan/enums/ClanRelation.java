package me.trae.clans.clan.enums;

import me.trae.clans.clan.enums.interfaces.IClanRelation;
import org.bukkit.ChatColor;

public enum ClanRelation implements IClanRelation {

    NEUTRAL(ChatColor.GOLD, ChatColor.YELLOW),
    SELF(ChatColor.DARK_AQUA, ChatColor.AQUA),
    ALLIANCE(ChatColor.DARK_GREEN, ChatColor.GREEN),
    TRUSTED_ALLIANCE(ChatColor.GREEN, ChatColor.DARK_GREEN),
    ENEMY(ChatColor.DARK_RED, ChatColor.RED),
    PILLAGE(ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE);

    private final ChatColor prefix, suffix;

    ClanRelation(final ChatColor prefix, final ChatColor suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public ChatColor getPrefix() {
        return this.prefix;
    }

    @Override
    public ChatColor getSuffix() {
        return this.suffix;
    }
}