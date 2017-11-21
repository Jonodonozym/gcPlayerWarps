
package jdz.pwarp.eventListeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if (!nameDescisionListener.containsKey(player))
			return;
		
		String text = ChatColor.stripColor(event.getMessage()).split(" ")[0];
		
		if (text.equalsIgnoreCase("cancel") || text.equalsIgnoreCase("stop"))
			player.sendMessage(ChatColor.RED+"Warp renaming cancelled");
		else 
			new WarpRenamedEvent(player, nameDescisionListener.get(player), text).call();

		nameDescisionListener.remove(player);
		event.setCancelled(true);
	}
	
	public void addPlayer(Player player, PlayerWarp warp) {
		player.sendMessage(ChatColor.GREEN+"Enter the name of the warp, or type 'cancel' to stop warp renaming");
		nameDescisionListener.put(player, warp);
	}

}
