package me.fromgate.elytra.tasks;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class AutoGlideCheckTask implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		if (!player.hasPermission("elytra.auto") || player.isSwimming() || player.hasMetadata("falling")) return;
		final Location location = player.getLocation();
		if (!Util.checkEmptyBlocks(event.getFrom(), location)) return;

		if (!Util.isElytraWeared(player) && Elytra.getCfg().autoElytraEquip && player.hasPermission("elytra.auto-equip") && Util.hasElytraStorage(player)) {
			autoEquip(player);
		}

		if (!player.isGliding() && !player.isFlying()) {
			player.setGliding(true);
		}

	}
	
	private void autoEquip(Player player){
		PlayerInventory inv = player.getInventory();
		ItemStack chestplate = new ItemStack(Material.AIR);
		ItemStack elytra = new ItemStack(Material.AIR);       	
		if(inv.getChestplate()!=null && inv.getChestplate().getType()!=Material.AIR){
			chestplate = inv.getChestplate();
			inv.setChestplate(new ItemStack(Material.AIR));
		}
		List<ItemStack> storage = new ArrayList<ItemStack>(Arrays.asList(inv.getStorageContents()));
		for(ItemStack item : storage){
			if (item.getType().equals(Material.ELYTRA)) {
				elytra = item;
				break;
			}
		}
		storage.remove(elytra);
		if(chestplate.getType()!=Material.AIR){
			storage.add(chestplate);
		}
		player.getInventory().setStorageContents(Util.listToArray(storage));
		inv.setChestplate(elytra);
		player.sendMessage(ChatColor.GREEN + "Elytra Auto-Equipped");
	}
}
