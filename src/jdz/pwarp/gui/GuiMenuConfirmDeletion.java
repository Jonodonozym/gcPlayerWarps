
package jdz.pwarp.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.components.guiMenu.guis.GuiMenuConfirmDialogue;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.events.WarpDeletedEvent;

class GuiMenuConfirmDeletion extends GuiMenuConfirmDialogue {
	private final GuiMenuMyWarps superMenu;
	private final PlayerWarp warp;

	public GuiMenuConfirmDeletion(Player player, PlayerWarp warp, GuiMenuMyWarps superMenu) {
		super(PlayerWarpPlugin.getInstance(), ChatColor.BLUE + "Delete Warp " + warp.getName() + "?");
		this.superMenu = superMenu;
		this.warp = warp;
	}

	@Override
	public void onCancel(Player player) {
		superMenu.open(player);
	}

	@Override
	public void onConfirm(Player player) {
		new WarpDeletedEvent(player, warp).call();
		superMenu.refresh();
		superMenu.open(player);
	}

}
