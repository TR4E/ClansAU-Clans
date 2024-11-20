package me.trae.clans.weapon.weapons.items;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.weapons.items.components.SupplyCrateComponent;
import me.trae.clans.weapon.weapons.items.currency.OneMillionDisc;
import me.trae.clans.weapon.weapons.items.data.SupplyCrateData;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.*;
import me.trae.core.utility.enums.ArmourMaterialType;
import me.trae.core.utility.enums.ArmourSlotType;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.registry.WeaponRegistry;
import me.trae.core.weapon.types.ActiveCustomItem;
import me.trae.core.weapon.types.CustomItem;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SupplyCrate extends CustomItem<Clans, WeaponManager, WeaponData> implements Listener, SupplyCrateComponent {

    private final List<SupplyCrateData> DATA = new ArrayList<>();

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "5_000")
    private long recharge;

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "60_000")
    private long duration;

    @ConfigInject(type = Long.class, path = "Chest-Duration", defaultValue = "60_000")
    private long chestDuration;

    public SupplyCrate(final WeaponManager manager) {
        super(manager, new ItemStack(Material.BEACON));
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public boolean isNaturallyObtained() {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Placing this block will call",
                "down a supply crate which contains",
                "a wide variety of items.",
                "",
                "This can only be placed in the Wilderness.",
                "",
                UtilString.pair("<gray>Duration", String.format("<green>%s", UtilTime.getTime(this.duration))),
                UtilString.pair("<gray>Recharge", String.format("<green>%s", UtilTime.getTime(this.recharge))),
        };
    }

    @Override
    public List<SupplyCrateData> getData() {
        return this.DATA;
    }

    @Override
    public Material getMaterial() {
        return this.getItemStack().getType();
    }

    @Override
    public Material getChestMaterial() {
        return Material.CHEST;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public long getChestDuration() {
        return this.chestDuration;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();

        if (block.getType() != this.getMaterial()) {
            return;
        }

        final Player player = event.getPlayer();

        final Client client = this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player);
        if (client == null) {
            return;
        }

        if (client.isAdministrating()) {
            return;
        }

        if (!(this.canPlace(player, block))) {
            event.setCancelled(true);
            return;
        }

        block.setType(Material.AIR);

        this.startSupplyCrate(block, Material.IRON_BLOCK);

        this.DATA.add(new SupplyCrateData(block.getLocation(), this.duration));

        for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
            final PlayerDisplayNameEvent playerDisplayNameEvent = new PlayerDisplayNameEvent(player, targetPlayer);
            UtilServer.callEvent(playerDisplayNameEvent);

            UtilMessage.simpleMessage(targetPlayer, this.getName(), "<var> has called in a <var> at <var>.", Arrays.asList(playerDisplayNameEvent.getPlayerName(), this.getDisplayName(), UtilLocation.locationToString(block.getLocation())));
        }
    }

    private boolean canPlace(final Player player, final Block block) {
        if (this.DATA.stream().anyMatch(data -> data.getLocation().getChunk() == block.getLocation().getChunk())) {
            UtilMessage.message(player, this.getName(), "There can only be 1 Supply Crate per chunk at a time.");
            return false;
        }

        final ClanManager clanManager = this.getInstance().getManagerByClass(ClanManager.class);

        if (clanManager.getClanByLocation(block.getLocation()) != null) {
            UtilMessage.message(player, this.getName(), "Supply Crates may only be called in the Wilderness.");
            return false;
        }

        for (final Clan nearbyClan : clanManager.getNearbyClans(block.getChunk(), 1)) {
            if (nearbyClan.isMemberByPlayer(player)) {
                continue;
            }

            UtilMessage.message(player, this.getName(), "You cannot place this next to another clans territory.");
            return false;
        }

        if (!(this.getInstance(Core.class).getManagerByClass(RechargeManager.class).add(player, this.getName(), this.recharge, true))) {
            return false;
        }

        return true;
    }

    @Override
    public void fillChest(final Inventory inventory) {
        final boolean eotw = this.getInstance().getManagerByClass(ClanManager.class).eotw;

        if (eotw) {
            for (final Legendary<?, ?, ?> legendary : WeaponRegistry.getWeaponsByClass(Legendary.class)) {
                if (UtilMath.getRandomNumber(Integer.class, 0, 10) < 4) {
                    continue;
                }

                inventory.addItem(legendary.getFinalBuilder().toItemStack());
            }

            for (int i = 0; i < 3; i++) {
                inventory.addItem(UtilItem.updateItemStack(new ItemStack(Material.TNT, 64)));
            }
            return;
        }

        UtilJava.call(ArmourMaterialType.getRandom(), armourMaterialType -> {
            for (final ArmourSlotType armourSlotType : ArmourSlotType.values()) {
                final Material material = Material.valueOf(String.format("%s_%s", armourMaterialType.name(), armourSlotType.name()));

                inventory.addItem(UtilItem.updateItemStack(new ItemStack(material)));
            }
        });

        for (final ActiveCustomItem<?, ?, ?> activeCustomItem : WeaponRegistry.getWeaponsByClass(ActiveCustomItem.class)) {
            if (UtilMath.getRandomNumber(Integer.class, 0, 15) < 5) {
                continue;
            }

            final ItemStack itemStack = activeCustomItem.getFinalBuilder().toItemStack();

            itemStack.setAmount(UtilMath.getRandomNumber(Integer.class, 2, 6));

            inventory.addItem(itemStack);
        }

        final int randomInteger = UtilMath.getRandomNumber(Integer.class, 0, 1000);

        if (randomInteger > 998) {
            inventory.addItem(UtilItem.updateItemStack(new ItemStack(Material.TNT, 4)));
        } else if (randomInteger < 3) {
            inventory.addItem(WeaponRegistry.getWeaponByClass(OneMillionDisc.class).getFinalBuilder().toItemStack());
        } else if (randomInteger < 15) {
            inventory.addItem(this.getFinalBuilder().toItemStack());
        }
    }
}