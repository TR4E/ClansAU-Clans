package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IPlayerCaughtFishEvent;
import me.trae.core.event.CustomCancellableEvent;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMath;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerCaughtFishEvent extends CustomCancellableEvent implements IPlayerCaughtFishEvent {

    private final Player player;
    private final State state;
    private final FishHook hook;

    private Entity caught;
    private int weight;
    private String caughtName;
    private boolean broadcastInform;
    private String informPrefix = "Fishing";

    public PlayerCaughtFishEvent(final CustomFishingEvent customFishingEvent) {
        this.player = customFishingEvent.getPlayer();
        this.state = customFishingEvent.getState();
        this.hook = customFishingEvent.getHook();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public FishHook getHook() {
        return this.hook;
    }

    @Override
    public Entity getCaught() {
        return this.caught;
    }

    @Override
    public void setCaught(final Entity caught) {
        this.caught = caught;
    }

    @Override
    public void setCaughtItemStack(final ItemStack itemStack) {
        final Item item = UtilItem.createItem(this.getHook().getLocation(), itemStack, false, false);

        this.setCaught(item);
    }

    @Override
    public boolean isCaught() {
        return this.getCaught() != null;
    }

    @Override
    public boolean isCaughtFish() {
        final Entity caught = this.getCaught();
        if (caught == null) {
            return false;
        }

        if (!(caught instanceof Item)) {
            return false;
        }

        final Item item = UtilJava.cast(Item.class, caught);
        if (item == null) {
            return false;
        }

        final ItemStack itemStack = item.getItemStack();
        if (itemStack == null) {
            return false;
        }

        return itemStack.getType() == Material.RAW_FISH;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(final int weight) {
        this.weight = weight;
    }

    @Override
    public String getCaughtName() {
        return this.caughtName;
    }

    @Override
    public void setCaughtName(final String caughtName) {
        this.caughtName = caughtName;
    }

    @Override
    public void setCaughtName(final ChatColor chatColor, final String caughtName) {
        this.setCaughtName(chatColor + ChatColor.stripColor(caughtName));
    }

    @Override
    public boolean isBroadcastInform() {
        return this.broadcastInform;
    }

    @Override
    public void setBroadcastInform(final boolean broadcastInform) {
        this.broadcastInform = broadcastInform;
    }

    @Override
    public String getInformPrefix() {
        return this.informPrefix;
    }

    @Override
    public void setInformPrefix(final String informPrefix) {
        this.informPrefix = informPrefix;
    }

    @Override
    public boolean isChance(final int minChance, final int maxChance, final int baseChance) {
        return UtilMath.getRandomNumber(Integer.class, minChance, maxChance) > baseChance;
    }
}