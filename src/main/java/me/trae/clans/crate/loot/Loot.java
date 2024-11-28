package me.trae.clans.crate.loot;

import me.trae.clans.Clans;
import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.interfaces.ILoot;
import me.trae.clans.crate.loot.models.ItemLoot;
import me.trae.core.framework.SpigotSubModule;
import me.trae.core.item.ItemBuilder;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public abstract class Loot extends SpigotSubModule<Clans, Crate> implements ILoot {

    private final ItemStack itemStack;
    private final double chance;

    public Loot(final Crate module, final double chance, final ItemStack itemStack) {
        super(module);

        this.itemStack = UtilItem.updateItemStack(itemStack);
        this.chance = chance;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public double getChance() {
        return this.chance;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return new ItemBuilder(this.getItemStack(), this.getDisplayName());
    }

    @Override
    public void reward(final Player player) {
        this.getConsumer().accept(player);

        String displayName = this.getDisplayName();

        if (this instanceof ItemLoot) {
            final ItemStack itemStack = this.getItemStack();

            if (itemStack.getAmount() > 1) {
                displayName = String.format("<yellow>%sx <gray>of %s", itemStack.getAmount(), displayName);
            } else {
                displayName = String.format("a %s", displayName);
            }
        }

        if (this.isBroadcast()) {
            for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
                final PlayerDisplayNameEvent playerDisplayNameEvent = new PlayerDisplayNameEvent(player, targetPlayer);
                UtilServer.callEvent(playerDisplayNameEvent);

                UtilMessage.simpleMessage(targetPlayer, this.getModule().getName(), "<var> has received <var>.", Arrays.asList(playerDisplayNameEvent.getPlayerName(), displayName));
            }
        } else {
            UtilMessage.simpleMessage(player, this.getModule().getName(), "You received <var>.", Collections.singletonList(displayName));
        }
    }
}