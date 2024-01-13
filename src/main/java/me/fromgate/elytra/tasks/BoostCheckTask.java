package me.fromgate.elytra.tasks;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.ElytraCooldown;
import me.fromgate.elytra.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BoostCheckTask extends BukkitRunnable {
	
	private Map<Player, Location> oldLocale = new HashMap<>();
	
	@Override
	public void run() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("elytra.speedup")) return;
			if (!player.isGliding()) return;
			if (!Util.isElytraWeared(player)) return;
			if(player.isSwimming() || player.hasMetadata("falling")) return;
			Location l = player.getLocation();
			if(oldLocale.containsKey(player) && !Util.isSameBlocks(oldLocale.get(player), l)){
				Vector vector = player.getVelocity();
				if (Util.isNotBoostAngle(oldLocale.get(player).getPitch())){
					oldLocale.remove(player);
					oldLocale.put(player, l);
					return;
				}
				if (Util.isNotBoostAngle(l.getPitch())){
					oldLocale.remove(player);
					oldLocale.put(player, l);
					return;
				}
				if (vector.length() < Elytra.getCfg().activationSpeedMin){
					oldLocale.remove(player);
					oldLocale.put(player, l);
					return;
				}
				if (vector.length() > Elytra.getCfg().activationSpeedMax){
					oldLocale.remove(player);
					oldLocale.put(player, l);
					return;
				}
				if (!ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.SPEED_UP)){
					oldLocale.remove(player);
					oldLocale.put(player, l);
					return;
				}
				player.setVelocity(vector.multiply(Elytra.getCfg().speedUpMult));
				Util.playParticles(player);
				Util.playSound(player);
				Util.processGForce(player);
				oldLocale.remove(player);
				oldLocale.put(player, l);
			} else {
				oldLocale.put(player, l);
			}
		}
	}
}
