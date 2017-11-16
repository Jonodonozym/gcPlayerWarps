
package jdz.pwarp.eventListeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.events.WarpRenamedEvent;

@SuppressWarnings("deprecation")
class WarpChatRenameListener implements Listener{
	public static WarpChatRenameListener instance;
	
	private final Map<Player, PlayerWarp> nameDescisionListener = new HashMap<Player, PlayerWarp>();
	
	public WarpChatRenameListener() {
		instance = this;
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if (!nameDescisionListener.containsKey(player))
			return;

		String name = event.getMessage().split(" ")[0];
		
		if (name.equalsIgnoreCase("cancel") || name.equalsIgnoreCase("stop"))
			player.sendMessage(ChatColor.RED+"Warp renaming cancelled");
		else 
			new WarpRenamedEvent(player, nameDescisionListener.get(player), name).call();

		nameDescisionListener.remove(player);
		event.setCancelled(true);
	}
	
	public void addPlayer(Player player, PlayerWarp warp) {
		player.sendMessage(ChatColor.GREEN+"Enter the name of the warp, or type 'cancel' to stop warp renaming");
		nameDescisionListener.put(player, warp);
	}

}
