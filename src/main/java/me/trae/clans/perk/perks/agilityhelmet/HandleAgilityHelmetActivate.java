package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.api.champions.skill.events.SkillPreActivateEvent;
import me.trae.champions.Champions;
import me.trae.champions.effect.EffectManager;
import me.trae.champions.effect.types.Silenced;
import me.trae.clans.Clans;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotSubListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilLeap;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.world.events.PlayerItemInteractEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class HandleAgilityHelmetActivate extends SpigotSubListener<Clans, AgilityHelmet> {

    final String LEAP_ABILITY_NAME = "Agility Leap";

    @ConfigInject(type = Float.class, path = "Energy", defaultValue = "0.0")
    private float energy;

    @ConfigInject(type = Long.class, path = "Leap-Recharge", defaultValue = "3_000")
    private long leapRecharge;

    @ConfigInject(type = Long.class, path = "WallKick-Recharge", defaultValue = "150")
    private long wallKickRecharge;

    public HandleAgilityHelmetActivate(final AgilityHelmet module) {
        super(module);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerItemInteract(final PlayerItemInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        final ItemStack itemStack = event.getItemStack();
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            return;
        }

        final Player player = event.getPlayer();

        if (!(this.getModule().isUsing(player))) {
            return;
        }

        if (!(this.canActivate(player))) {
            return;
        }


        final String prefix = this.getModule().getName();

        final String leapAbilityName = this.LEAP_ABILITY_NAME;
        final String wallKickAbilityName = "Agility Wall Kick";

        final String leapCooldownName = leapAbilityName;
        final String wallKickCooldownName = wallKickAbilityName;

        final long leapRechargeDuration = this.leapRecharge;
        final long wallKickRechargeDuration = this.wallKickRecharge;

        final float energy = this.energy;

        UtilLeap.activate(player, prefix, leapAbilityName, wallKickAbilityName, leapCooldownName, wallKickCooldownName, leapRechargeDuration, wallKickRechargeDuration, energy);
    }

    private boolean canActivate(final Player player) {
        final SkillPreActivateEvent skillPreActivateEvent = new SkillPreActivateEvent(null, player);
        UtilServer.callEvent(skillPreActivateEvent);
        if (skillPreActivateEvent.isCancelled()) {
            return false;
        }

        if (UtilBlock.isInLiquid(player.getLocation())) {
            UtilMessage.simpleMessage(player, this.getModule().getName(), "You cannot use <green><var></green> while in liquid.", Collections.singletonList(this.LEAP_ABILITY_NAME));
            return false;
        }

        if (this.getInstance(Champions.class).getManagerByClass(EffectManager.class).getModuleByClass(Silenced.class).isUserByEntity(player)) {
            UtilMessage.simpleMessage(player, this.getModule().getName(), "You cannot use <green><var></green> while silenced.", Collections.singletonList(this.LEAP_ABILITY_NAME));
            return false;
        }

        return true;
    }
}