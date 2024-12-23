package me.trae.clans.world.modules;

import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilMath;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class SpringBlock extends SpigotListener<Clans, WorldManager> {

    @ConfigInject(type = String.class, path = "Material", defaultValue = "SPONGE")
    private String material;

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "800")
    private long recharge;

    @ConfigInject(type = Double.class, path = "Velocity", defaultValue = "1.8")
    private double velocity;

    public SpringBlock(final WorldManager manager) {
        super(manager);
    }

    private Material getMaterial() {
        return Material.valueOf(this.material);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Material material = this.getMaterial();

        final Block block = event.getClickedBlock();

        if (block.getType() != material) {
            return;
        }

        final Player player = event.getPlayer();

        if (!(UtilBlock.getBlockUnder(player.getLocation()).equals(block))) {
            return;
        }

        if (UtilMath.offset(player.getLocation().toVector(), block.getLocation().add(0.5D, 1.5D, 0.5D).toVector()) > 0.6D) {
            return;
        }

        if (!(this.getInstanceByClass(Core.class).getManagerByClass(RechargeManager.class).add(player, this.getName(), this.recharge, false))) {
            return;
        }

        event.setCancelled(true);

        block.getWorld().playEffect(block.getLocation(), Effect.BLAZE_SHOOT, 0, 15);

        for (int i = 0; i < 4; i++) {
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, material, 15);
        }

        player.setVelocity(new Vector(0.0D, this.velocity, 0.0D));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPreDamage(final CustomPreDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        if (!(event.getDamagee() instanceof Player)) {
            return;
        }

        final Player player = event.getDamageeByClass(Player.class);

        if (!(Arrays.asList(this.getMaterial(), Material.WOOL, Material.CARPET).contains(UtilBlock.getBlockUnder(player.getLocation()).getType()))) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        final ItemBuilder builder = event.getBuilder();

        if (builder.getItemStack().getType() != this.getMaterial()) {
            return;
        }

        builder.setDisplayName(this.getName());

        builder.setLore(new String[]{
                "Stand on this block and right-click",
                "it to launch yourself upwards."
        });
    }
}