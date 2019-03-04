
package jdz.pwarp.eventListeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.events.WarpCreatedEvent;

class WarpChatCreateListener implements Listener {
	public static WarpChatCreateListener instance;

	private final Set<Player> nameDescisionListener = new HashSet<>();

	public WarpChatCreateListener() {
		instance = this;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		if (!nameDescisionListener.contains(player))
			return;

		String text = ChatColor.stripColor(event.getMessage()).split(" ")[0];

		if (text.equalsIgnoreCase("cancel") || text.equalsIgnoreCase("stop"))
			player.sendMessage(ChatColor.RED + "Warp creation cancelled");
		else
			new WarpCreatedEvent(player, new PlayerWarp(player, player.getLocation(), text)).call();

		nameDescisionListener.remove(player);
		event.setCancelled(true);
	}

	public void addPlayer(Player player) {
		player.sendMessage(ChatColor.GREEN + "Enter the name of the warp, or type 'cancel' to stop warp creation");
		nameDescisionListener.add(player);
	}
}
