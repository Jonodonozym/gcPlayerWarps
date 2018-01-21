
package jdz.pwarp.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.guiMenu.guis.GuiMenuConfirmDialogue;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.events.WarpDeletedEvent;

class GuiMenuConfirmDeletion extends GuiMenuConfirmDialogue{
	private final GuiMenuMyWarps superMenu;
	private final Player player;
	private final PlayerWarp warp;
	
	public GuiMenuConfirmDeletion(Player player, PlayerWarp warp, GuiMenuMyWarps superMenu) {
		super(PlayerWarpPlugin.instance, ChatColor.BLUE+"Delete Warp "+warp.getName()+"?");
		this.superMenu = superMenu;
		this.player = player;
		this.warp = warp;
	}

	@Override
	public void onCancel() {
		superMenu.open(player);
	}

	@Override
	public void onConfirm() {
		new WarpDeletedEvent(player, warp).call();
		superMenu.refresh();
		superMenu.open(player);
	}

}
