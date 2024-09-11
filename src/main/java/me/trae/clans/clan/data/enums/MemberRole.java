package me.trae.clans.clan.data.enums;

import me.trae.clans.clan.data.enums.interfaces.IMemberRole;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;

public enum MemberRole implements IMemberRole {

    RECRUIT(ChatColor.AQUA),
    MEMBER(ChatColor.AQUA),
    ADMIN(ChatColor.RED),
    LEADER(ChatColor.DARK_RED);

    private final String name;
    private final ChatColor chatColor;

    MemberRole(final ChatColor chatColor) {
        this.name = UtilString.clean(this.name());
        this.chatColor = chatColor;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ChatColor getChatColor() {
        return this.chatColor;
    }

    @Override
    public String getPrefix() {
        return this.name().substring(0, 1);
    }
}