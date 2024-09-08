package me.trae.clans.clan.data.enums;

import me.trae.clans.clan.data.enums.interfaces.IMemberRole;
import me.trae.core.utility.UtilString;

public enum MemberRole implements IMemberRole {

    RECRUIT, MEMBER, ADMIN, LEADER;

    private final String name;

    MemberRole() {
        this.name = UtilString.clean(this.name());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPrefix() {
        return this.name().substring(0, 1);
    }
}