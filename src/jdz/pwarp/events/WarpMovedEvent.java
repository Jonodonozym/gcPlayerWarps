
package jdz.pwarp.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import jdz.bukkitUtils.events.Cancellable;
import jdz.pwarp.data.PlayerWarp;

public class WarpMovedEvent extends WarpEvent implements Cancellable {
	private final Location newLocation;

	public WarpMovedEvent(Player player, PlayerWarp warp, Location newLocation) {
		super(player, warp);
		this.newLocation = newLocation;
	}

	public Location getNewLocation() {
		return newLocation;
	}

	public static HandlerList getHandlerList() {
		return getHandlers(WarpMovedEvent.class);
	}
}
