
package jdz.pwarp.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import jdz.bukkitUtils.components.guiMenu.guis.GuiMenu;
import jdz.bukkitUtils.components.guiMenu.itemStacks.ClickableStack;
import jdz.bukkitUtils.components.guiMenu.itemStacks.ClickableStackLinkedMenu;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpManager;

class GuiMenuMyWarps extends GuiMenu {
	private Inventory inventory;
	private final Player player;
	private final GuiMenu superMenu;

	public GuiMenuMyWarps(Player player, GuiMenu superMenu) {
		super(PlayerWarpPlugin.getInstance());
		this.player = player;
		this.superMenu = superMenu;
		refresh();
	}

	public void refresh() {
		if (inventory != null)
			clear(inventory);

		List<PlayerWarp> myWarps = WarpManager.getInstance().getAll(player);

		inventory = Bukkit.createInventory(player, Math.max(myWarps.size() / 9 * 9, 36), ChatColor.GREEN + "My Warps");

		for (int i = 0; i < myWarps.size(); i++)
			setItem(new ClickableStackWarpEditable(myWarps.get(i)), i, inventory);

		ClickableStack backButton = new ClickableStackLinkedMenu(Material.ENDER_PEARL, ChatColor.GREEN + "Back",
				superMenu);

		setItem(backButton, 33, inventory);

		int currentWarps = myWarps.size();
		int allowedWarps = WarpManager.getInstance().getNumWarpsAllowed(player);

		setItem(new ClickableStackCreateWarp(player, allowedWarps > currentWarps), 35, inventory);
	}

	@Override
	public void open(Player player) {
		player.openInventory(inventory);
	}
}
