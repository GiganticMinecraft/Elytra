package me.fromgate.elytra.tasks;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.ElytraCooldown;
import me.fromgate.elytra.util.ElytraConfig;
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

public class RunUpCheckTask implements Listener {
	
	private Map<Player, Location> oldLocale = new HashMap<>();
	private Map<String, Integer> runners = new HashMap<>();
	private ElytraConfig cfg;
	
	public RunUpCheckTask(){
		cfg = Elytra.getCfg();
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		final Player player = e.getPlayer();

		if (!player.hasPermission("elytra.runup") || !Util.isElytraWeared(player) || player.isGliding() || player.isFlying()) return;

		final Location location = player.getLocation();

		if (Util.isSameBlocks(oldLocale.get(player), location)) return;
		if (!player.isSprinting()) {
			setRunUpMode(player, false);
			return;
		}

		if(!timeToJump(player) || !Util.checkAngle(location.getPitch(), cfg.runUpMinAngle, cfg.runUpMaxAngle) || !ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.RUN_UP)) return;

		setRunUpMode(player, false);
		player.setSprinting(false);
		player.teleport(location.add(0, 1, 0));
		Vector v = player.getLocation().getDirection();
		v.multiply(cfg.runUpBoost);
		player.setVelocity(v);
		Util.playParticles(player);
		Util.playSound(player);
		Bukkit.getScheduler().runTaskLater(Elytra.getPlugin(), () -> player.setGliding(true), 5);
	}

	private boolean timeToJump(Player player) {
        if (!runners.containsKey(player.getName())) setRunUpMode(player, true);
        int count = runners.get(player.getName());
        if (count < cfg.runDistance) {
            runners.put(player.getName(), count + 1);
            return false;
        }
        return true;
    }

    private void setRunUpMode(Player player, boolean mode) {
        if (mode) runners.put(player.getName(), 0);
        else if (runners.containsKey(player.getName())) runners.remove(player.getName());
    }
}