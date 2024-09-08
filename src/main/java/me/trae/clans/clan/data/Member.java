package me.trae.clans.clan.data;

import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.data.interfaces.IMember;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Member implements IMember {

    private final UUID uuid;

    private MemberRole memberRole;

    public Member(final UUID uuid, final MemberRole memberRole) {
        this.uuid = uuid;
        this.memberRole = memberRole;
    }

    public Member(final Player player, final MemberRole memberRole) {
        this(player.getUniqueId(), memberRole);
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public MemberRole getRole() {
        return this.memberRole;
    }

    @Override
    public void setRole(final MemberRole memberRole) {
        this.memberRole = memberRole;
    }

    @Override
    public boolean hasRole(final MemberRole memberRole) {
        return this.getRole().ordinal() >= memberRole.ordinal();
    }
}