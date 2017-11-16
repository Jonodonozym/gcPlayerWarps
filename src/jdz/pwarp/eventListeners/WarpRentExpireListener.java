
package jdz.pwarp.eventListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.events.WarpDeletedEvent;
import jdz.pwarp.events.WarpRentExpiredEvent;
import net.md_5.bungee.api.ChatColor;

class WarpRentExpireListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpRentExpire(WarpRentExpiredEvent event) {
		PlayerWarpPlugin.sqlMessageQueue.addQueuedMessage(event.getWarp().getOwner(),
				ChatColor.RED+"You miseed your rent payments on the warp '"+event.getWarp().getName()+"' and it has been removed!");
		new WarpDeletedEvent(null, event.getWarp()).call();
	}
}
