package me.trae.clans.world.enums;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.world.enums.interfaces.IWarpType;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum WarpType implements IWarpType {

    BLUE_SPAWN(7, ChatColor.AQUA, new ItemStack(Material.STAINED_CLAY, 1, (short) 11)),
    RED_SPAWN(1, ChatColor.RED, new ItemStack(Material.STAINED_CLAY, 1, (short) 14)),
    BLUE_SHOPS(5, ChatColor.AQUA, new ItemStack(Material.WOOL, 1, (short) 11)),
    RED_SHOPS(3, ChatColor.RED, new ItemStack(Material.WOOL, 1, (short) 14)),
    FIELDS(8, ChatColor.WHITE, new ItemStack(Material.IRON_PICKAXE)),
    LAKE(6, ChatColor.WHITE, new ItemStack(Material.FISHING_ROD));

    private final String name;
    private final int slot;
    private final ChatColor chatColor;
    private final ItemStack itemStack;

    WarpType(final int slot, final ChatColor chatColor, final ItemStack itemStack) {
        this.name = UtilString.clean(this.name());
        this.slot = slot;
        this.chatColor = chatColor;
        this.itemStack = itemStack;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public ChatColor getChatColor() {
        return this.chatColor;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public String getDisplayName() {
        return this.getChatColor() + this.getName();
    }

    @Override
    public Location getLocation() {
        final ClanManager clanManager = UtilPlugin.getInstanceByClass(Clans.class).getManagerByClass(ClanManager.class);

        if (clanManager.isClanByName(this.name())) {
            final Clan clan = clanManager.getClanByName(this.name());
            if (clan != null && clan.hasHome()) {
                return clan.getHome();
            }
        }

        if (this == LAKE) {
            return new Location(UtilWorld.getDefaultWorld(), -68.5D, 68.0D, 29.5D, UtilLocation.getYawByDirectionType(DirectionType.NORTH), 0.0F);
        }

        return null;
    }

    @Override
    public boolean showForAdministrating() {
        return this == FIELDS || this == LAKE;
    }
}