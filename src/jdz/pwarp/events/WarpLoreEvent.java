
package jdz.pwarp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import jdz.pwarp.data.PlayerWarp;

public class WarpLoreEvent extends WarpEvent {
	private final String newLore;
	private final int line;

	public WarpLoreEvent(Player player, PlayerWarp warp, String newLore, int line) {
		super(player, warp);
		this.newLore = newLore;
		this.line = line;
	}

	public String getNewLore() {
		return newLore;
	}

	public int getLine() {
		return line;
	}

	public static HandlerList getHandlerList() {
		return getHandlers(WarpLoreEvent.class);
	}

}
