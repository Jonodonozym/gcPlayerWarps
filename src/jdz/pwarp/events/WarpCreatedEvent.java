
package jdz.pwarp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import jdz.bukkitUtils.components.events.Cancellable;
import jdz.pwarp.data.PlayerWarp;

public class WarpCreatedEvent extends WarpEvent implements Cancellable {

	public WarpCreatedEvent(Player player, PlayerWarp warp) {
		super(player, warp);
	}

	public static HandlerList getHandlerList() {
		return getHandlers(WarpCreatedEvent.class);
	}
}
