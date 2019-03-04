
package jdz.pwarp.events;

import org.bukkit.command.CommandSender;

import jdz.bukkitUtils.components.events.Event;
import jdz.pwarp.data.PlayerWarp;

public abstract class WarpEvent extends Event {
	private final CommandSender cause;
	private final PlayerWarp warp;

	public WarpEvent(CommandSender player, PlayerWarp warp) {
		cause = player;
		this.warp = warp;
	}

	public CommandSender getCause() {
		return cause;
	}

	public PlayerWarp getWarp() {
		return warp;
	}
}
