package me.fromgate.elytra.tasks;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AutoGlideCheckTask extends BukkitRunnable {	
	private Map<Player, Location> oldLocale = new HashMap<>();
	@Override
	public void run() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.hasPermission("elytra.auto") && !player.isSwimming() && !player.hasMetadata("falling")){
				Location l = player.getLocation();
				if (oldLocale.containsKey(player) && !Util.isSameBlocks(l, oldLocale.get(player))){
					if(Util.checkEmptyBlocks(oldLocale.get(player), l)){
						if(!Util.isElytraWeared(player) && Elytra.getCfg().autoElytraEquip && player.hasPermission("elytra.auto-equip") && Util.hasElytraStorage(player)){
							autoEquip(player);
						}
						if (!player.isGliding() && !player.isFlying()){
							player.setGliding(true);
						}
					}
					oldLocale.remove(player);
					oldLocale.put(player, l);
				} else {
					oldLocale.put(player, l);
				}
			}
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
