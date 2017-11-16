
package jdz.pwarp.eventListeners;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import jdz.bukkitUtils.vault.VaultLoader;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpConfig;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.events.WarpCreatedEvent;
import jdz.pwarp.events.WarpDeletedEvent;
import jdz.pwarp.events.WarpMovedEvent;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

class WarpCreationDeletionListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpDelete(WarpDeletedEvent event) {
		if (!event.isCancelled())
			WarpDatabase.instance.delWarp(event.getWarp());
		if (event.getCause() != null) {
			event.getCause().sendMessage(ChatColor.GREEN + "Warp removed successfully");
			if (!event.getCause().equals(event.getWarp().getOwner()) && event.getWarp().getOwner().isOnline())
				PlayerWarpPlugin.sqlMessageQueue.addQueuedMessage(event.getWarp().getOwner(),
						ChatColor.RED + "Your warp '" + event.getWarp().getName() + "' was removed by an admin");
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWarpCreatePreProcess(WarpCreatedEvent event) {
		if (WarpConfig.warpCost > 0) {
			Player player = (Player)event.getCause();
			Economy eco = VaultLoader.getEconomy();
			
			double balance = eco.getBalance(player);
			if (balance < WarpConfig.warpCost) {
				player.sendMessage(ChatColor.RED+"You need "+eco.format(WarpConfig.warpCost - balance)+" to create a warp!");
				event.setCancelled(true);
			}
		}
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpCreate(WarpCreatedEvent event) {
		if (WarpConfig.warpCost > 0) {
			Player player = (Player)event.getCause();
			Economy eco = VaultLoader.getEconomy();
			eco.withdrawPlayer(player, WarpConfig.warpCost);
			player.sendMessage(ChatColor.GREEN+eco.format(WarpConfig.warpCost)+" has been taken from your account");
		}
		setWarp(event.getCause(), event.getWarp());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpMove(WarpMovedEvent event) {
		PlayerWarp warp = new PlayerWarp(event.getWarp().getOwner(), event.getNewLocation(), event.getWarp().getName());
		setWarp(event.getCause(), warp);
	}

	private void setWarp(CommandSender sender, PlayerWarp warp) {
		WarpDatabase.instance.setWarp(warp);
		sender.sendMessage(ChatColor.GREEN + "Warp set! to warp to it, use /pwarp " + warp.getOwner().getName() + " "
				+ warp.getName());
	}
}
