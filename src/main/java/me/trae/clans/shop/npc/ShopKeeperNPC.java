package me.trae.clans.shop.npc;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.npc.interfaces.IShopKeeperNPC;
import me.trae.core.npc.CustomNPC;
import me.trae.core.npc.models.ClickableNPC;
import me.trae.core.npc.models.LookCloseNPC;
import me.trae.core.npc.models.SilentSoundNPC;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.enums.ClickType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ShopKeeperNPC extends CustomNPC implements ClickableNPC, LookCloseNPC, SilentSoundNPC, IShopKeeperNPC {

    private final ShopKeeper shopKeeper;

    public ShopKeeperNPC(final EntityType entityType, final Location location, final ShopKeeper shopKeeper) {
        super(entityType, location);

        this.shopKeeper = shopKeeper;
    }

    @Override
    public String getDisplayName() {
        return this.getShopKeeper().getDisplayName();
    }

    @Override
    public void onSpawn() {
        this.getShopKeeper().updateNPC(this, UtilJava.cast(LivingEntity.class, this.getNPC().getEntity()));
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        if (!(this.getShopKeeper().isEnabled())) {
            return;
        }

        if (clickType != ClickType.RIGHT) {
            return;
        }

        this.getShopKeeper().onClick(player, clickType);
    }

    @Override
    public ShopKeeper getShopKeeper() {
        return this.shopKeeper;
    }
}