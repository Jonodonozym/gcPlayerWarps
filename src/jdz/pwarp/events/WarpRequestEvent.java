
package jdz.pwarp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import jdz.bukkitUtils.events.Cancellable;
import jdz.pwarp.data.PlayerWarp;

public class WarpRequestEvent extends WarpEvent implements Cancellable{
	public WarpRequestEvent(Player player, PlayerWarp warp) {
		super(player, warp);
	}
	
	public static HandlerList getHandlerList() {
		return getHandlers(WarpRequestEvent.class);
	}
}
