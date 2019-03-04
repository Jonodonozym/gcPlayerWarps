
package jdz.pwarp.eventListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import jdz.bukkitUtils.components.messengers.OfflineMessenger;
import jdz.bukkitUtils.pluginExtensions.vault.VaultLoader;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.config.WarpConfig;
import jdz.pwarp.data.WarpManager;
import jdz.pwarp.events.WarpCreatedEvent;
import jdz.pwarp.events.WarpDeletedEvent;
import jdz.pwarp.events.WarpMovedEvent;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

class WarpCreationDeletionListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpDelete(WarpDeletedEvent event) {
		if (event.isCancelled())
			return;

		WarpManager.getInstance().remove(event.getWarp());

		if (event.getCause() != null) {
			event.getCause().sendMessage(ChatColor.GREEN + "Warp removed successfully");
			if (!event.getCause().equals(event.getWarp().getOwner().getPlayer())
					&& event.getWarp().getOwner().isOnline())
				OfflineMessenger.getMessenger(PlayerWarpPlugin.getInstance()).message(event.getWarp().getOwner(),
						ChatColor.RED + "Your warp '" + event.getWarp().getName() + "' was removed by an admin");
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWarpCreatePreProcess(WarpCreatedEvent event) {
		if (WarpConfig.getWarpCost() > 0) {
			Player player = (Player) event.getCause();
			Economy eco = VaultLoader.getEconomy();

			double balance = eco.getBalance(player);
			if (balance < WarpConfig.getWarpCost()) {
				player.sendMessage(ChatColor.RED + "You need " + eco.format(WarpConfig.getWarpCost() - balance)
						+ " to create a warp!");
				event.setCancelled(true);
			}
		}
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpCreate(WarpCreatedEvent event) {
		if (event.isCancelled())
			return;

		if (WarpConfig.getWarpCost() > 0) {
			Player player = (Player) event.getCause();
			Economy eco = VaultLoader.getEconomy();
			eco.withdrawPlayer(player, WarpConfig.getWarpCost());
			player.sendMessage(
					ChatColor.GREEN + eco.format(WarpConfig.getWarpCost()) + " has been taken from your account");

			WarpManager.getInstance().add(event.getWarp());

			event.getCause().sendMessage(ChatColor.GREEN + "Warp set! to warp to it, use /pwarp "
					+ event.getWarp().getOwner().getName() + " " + event.getWarp().getName());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpMove(WarpMovedEvent event) {
		if (event.isCancelled())
			return;

		event.getWarp().setLocation(event.getNewLocation());

		event.getCause().sendMessage(ChatColor.GREEN + "Warp set! to warp to it, use /pwarp "
				+ event.getWarp().getOwner().getName() + " " + event.getWarp().getName());
	}
}
