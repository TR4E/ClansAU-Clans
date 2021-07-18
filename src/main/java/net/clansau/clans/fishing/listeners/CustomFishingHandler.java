package net.clansau.clans.fishing.listeners;

import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.fishing.Fish;
import net.clansau.clans.fishing.FishingManager;
import net.clansau.clans.fishing.enums.CatchType;
import net.clansau.clans.fishing.events.CustomFishingEvent;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.*;
import net.clansau.core.weapon.Weapon;
import net.clansau.core.weapon.WeaponManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class CustomFishingHandler extends CoreListener<FishingManager> {

    public CustomFishingHandler(final FishingManager manager) {
        super(manager, "Custom Fishing Handler");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomFishing(final CustomFishingEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final int randomValue = UtilMath.randomInt(0, 500);
        if (randomValue < 75) {
            this.handleEntityTyeCatch(player, e);
            return;
        }
        this.handleFishCatch(player, randomValue, e);
    }

    private void handleEntityTyeCatch(final Player player, final CustomFishingEvent e) {
        final LivingEntity entity = (LivingEntity) e.getCaught().getWorld().spawnEntity(e.getCaught().getLocation(), getManager().getRandomEntityType());
        entity.setVelocity(entity.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(-1.0D).normalize());
        UtilMessage.messageCMD(player, "Fishing", "You caught a " + ChatColor.GREEN + UtilFormat.cleanString(entity.getType().name()) + ChatColor.GRAY + ".");
    }

    private void handleFishCatch(final Player player, final int randomValue, final CustomFishingEvent e) {
        double mean = 550.0D;
        double variance = 170.0D;
        CatchType catchType = CatchType.STANDARD;
        if (randomValue > 450) {
            mean = 1800.0D;
            variance = 300.0D;
            catchType = CatchType.HUGE_CATCH;
        } else if (randomValue > 375) {
            mean = 1000.0D;
            variance = 150.0D;
            catchType = CatchType.BIG_CATCH;
        }
        final boolean frenzy = player.getGameMode().equals(GameMode.CREATIVE);
        String caught = "";
        final int random = (frenzy ? UtilMath.randomInt(1000, 10050) : UtilMath.randomInt(0, 10000));
        ItemStack item = null;
        if (random > 9980) {
            if (random < 9996) {
                item = new ItemStack(Material.TNT, 1);
                caught = "a " + ChatColor.GREEN + "TNT";
            } else {
                final Weapon weapon = getInstance().getManager(WeaponManager.class).getRandomWeapon();
                item = weapon.toItemStack(true);
                caught = "a " + weapon.getDisplayName();
            }
        }
        if (item == null) {
            if (random > 9500) {
                final ItemStack[] randItems = new ItemStack[]{new ItemStack(Material.GOLD_SWORD, 1), new ItemStack(Material.IRON_PICKAXE, 1), new ItemStack(Material.DIAMOND_SPADE, 1), new ItemStack(Material.EMERALD, UtilMath.randomInt(12)), new ItemStack(Material.LEATHER, UtilMath.randomInt(18))};
                item = randItems[UtilMath.randomInt(randItems.length - 1)];
                caught = ChatColor.GREEN.toString() + item.getAmount() + "x " + ChatColor.stripColor(UtilItem.updateNames(item).getItemMeta().getDisplayName());
                catchType = CatchType.BIG_CATCH;
            }
        }
        if (item == null) {
            final int i = (int) RandomGaussian.getGaussian(mean, variance);
            final String randomFish = getManager().getRandomFish();
            item = UtilItem.createItem(new ItemStack(Material.RAW_FISH, i / 3), ChatColor.YELLOW + randomFish, null);
            caught = "a " + ChatColor.GREEN + i + " Pound " + randomFish;
            getManager().addFish(new Fish(player.getUniqueId(), randomFish, i, System.currentTimeMillis()));
        }
        UtilItem.insert(player, item);
        catchType.announce(getInstance().getManager(ClanManager.class), player, caught);
    }
}