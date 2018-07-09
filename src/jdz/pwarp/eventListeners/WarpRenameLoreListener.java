
package jdz.pwarp.eventListeners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import jdz.bukkitUtils.messengers.OfflineMessenger;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.events.WarpLoreEvent;
import jdz.pwarp.events.WarpRenamedEvent;

class WarpRenameLoreListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRename(WarpRenamedEvent event) {
		CommandSender sender = event.getCause();
		PlayerWarp warp = event.getWarp();
		String newName = event.getNewName();

		warp.setName(newName);
		sender.sendMessage(ChatColor.GREEN + "'" + warp.getName() + "' successfully renamed to '" + newName + "'");

		if (!sender.equals(warp.getOwner().getPlayer()))
			OfflineMessenger.getMessenger(PlayerWarpPlugin.getInstance()).message(warp.getOwner(),
					ChatColor.RED + "Your warp '" + warp.getName() + "' was renamed to '" + newName + "' by an admin.");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLoreChange(WarpLoreEvent event) {
		String lore = ChatColor.translateAlternateColorCodes('&', event.getNewLore());

		event.getWarp().setLoreLine(lore, event.getLine());

		event.getCause().sendMessage(
				ChatColor.GREEN + "Warp lore line " + event.getLine() + " set to '" + event.getNewLore() + "'");
	}

}
