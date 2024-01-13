package me.fromgate.elytra.tasks;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.ElytraCooldown;
import me.fromgate.elytra.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BoostCheckTask implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		if (!player.hasPermission("elytra.speedup") || !player.isGliding() || !Util.isElytraWeared(player) || player.isSwimming() || player.hasMetadata("falling")) return;

		final Location location = player.getLocation();
		final Location from = e.getFrom();
		final Vector vector = player.getVelocity();

		if (Util.isNotBoostAngle(from.getPitch()) || Util.isNotBoostAngle(location.getPitch()) || vector.length() < Elytra.getCfg().activationSpeedMin ||
				vector.length() > Elytra.getCfg().activationSpeedMax || !ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.SPEED_UP)) return;

		player.setVelocity(vector.multiply(Elytra.getCfg().speedUpMult));
		Util.playParticles(player);
		Util.playSound(player);
		Util.processGForce(player);
	}

}
