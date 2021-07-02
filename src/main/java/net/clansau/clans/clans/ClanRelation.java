package net.clansau.clans.clans;

import org.bukkit.ChatColor;

public enum ClanRelation {

    NEUTRAL(ChatColor.GOLD, ChatColor.YELLOW),
    SELF(ChatColor.DARK_AQUA, ChatColor.AQUA),
    ALLY(ChatColor.DARK_GREEN, ChatColor.GREEN),
    TRUSTED_ALLY(ChatColor.GREEN, ChatColor.DARK_GREEN),
    ENEMY(ChatColor.DARK_RED, ChatColor.RED),
    PILLAGE(ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE),
    ADMIN(ChatColor.WHITE, ChatColor.WHITE);

    private final ChatColor prefix, suffix;

    ClanRelation(final ChatColor prefix, final ChatColor suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public final ChatColor getPrefix() {
        return this.prefix;
    }

    public final ChatColor getSuffix() {
        return this.suffix;
    }
}