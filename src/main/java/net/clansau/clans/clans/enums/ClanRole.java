package net.clansau.clans.clans.enums;

public enum ClanRole {

    RECRUIT, MEMBER, ADMIN, LEADER;

    public static ClanRole getClanRole(final int ordinal) {
        for (final ClanRole clanRole : values()) {
            if (clanRole.ordinal() == ordinal) {
                return clanRole;
            }
        }
        return null;
    }
}