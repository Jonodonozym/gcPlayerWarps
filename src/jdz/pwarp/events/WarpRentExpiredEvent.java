
package jdz.pwarp.events;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import jdz.pwarp.data.PlayerWarp;

public class WarpRentExpiredEvent extends WarpEvent {

	public WarpRentExpiredEvent(PlayerWarp warp) {
		super(Bukkit.getConsoleSender(), warp);
	}

	public static HandlerList getHandlerList() {
		return getHandlers(WarpRentExpiredEvent.class);
	}
}
