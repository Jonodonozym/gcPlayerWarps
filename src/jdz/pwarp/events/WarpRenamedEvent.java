
package jdz.pwarp.events;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import jdz.pwarp.data.PlayerWarp;

public class WarpRenamedEvent extends WarpEvent {
	private final String newName;

	public WarpRenamedEvent(CommandSender cause, PlayerWarp warp, String newName) {
		super(cause, warp);
		this.newName = newName;
	}

	public String getNewName() {
		return newName;
	}

	public static HandlerList getHandlerList() {
		return getHandlers(WarpRenamedEvent.class);
	}

}
