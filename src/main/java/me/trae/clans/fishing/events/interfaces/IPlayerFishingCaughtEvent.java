package me.trae.clans.fishing.events.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface IPlayerFishingCaughtEvent extends IFishingEvent {

    Entity getCaught();

    void setCaught(final Entity caught);

    void setCaughtItemStack(final ItemStack itemStack);

    boolean isCaught();

    boolean isCaughtFish();

    int getWeight();

    void setWeight(final int weight);

    String getCaughtType();

    void setCaughtType(final String caughtType);

    String getCaughtName();

    void setCaughtName(final String caughtName);

    void setCaughtName(final ChatColor chatColor, final String caughtName);

    boolean isBroadcastInform();

    void setBroadcastInform(final boolean broadcastInform);

    String getInformPrefix();

    void setInformPrefix(final String broadcastInformPrefix);

    boolean isChance(final int minChance, final int maxChance, final int baseChance);
}