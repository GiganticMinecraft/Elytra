package me.fromgate.elytra.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.ElytraCooldown;
import me.fromgate.elytra.util.Util;

public class ShiftActivationCheckTask implements Listener {

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		final Player player = e.getPlayer();

		if (!player.hasPermission("elytra.shift-activation") || player.isSwimming() || player.hasMetadata("falling") || !player.isSneaking() || !player.isGliding() ||
				!Util.isElytraWeared(player)) return;

		Vector vector = player.getVelocity();
		if (vector.length() > Elytra.getCfg().shiftActSpeed || !ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.SHIFT)) return;

		player.setVelocity(vector.multiply(Elytra.getCfg().shiftMult));
		Util.playParticles(player);
		Util.playSound(player);
		Util.processGForce(player);
	}

}
