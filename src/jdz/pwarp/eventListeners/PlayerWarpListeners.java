
package jdz.pwarp.eventListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import jdz.pwarp.data.PlayerWarp;

public class PlayerWarpListeners {
	public static void register(JavaPlugin plugin) {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new WarpCreationDeletionListener(), plugin);
		pm.registerEvents(new WarpRequestListener(plugin), plugin);
		pm.registerEvents(new WarpRenameLoreListener(), plugin);
		pm.registerEvents(new WarpRentExpireListener(), plugin);
		pm.registerEvents(new WarpChatCreateListener(), plugin);
		pm.registerEvents(new WarpChatRenameListener(), plugin);
	}

	public static void addRenameChatPlayer(Player player, PlayerWarp warp) {
		WarpChatRenameListener.instance.addPlayer(player, warp);
	}

	public static void addCreateChatPlayer(Player player) {
		WarpChatCreateListener.instance.addPlayer(player);
	}
}
