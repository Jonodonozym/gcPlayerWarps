
package jdz.pwarp.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import jdz.bukkitUtils.guiMenu.guis.GuiMenu;
import jdz.bukkitUtils.guiMenu.itemStacks.ClickableStack;
import jdz.pwarp.data.WarpManager;
import jdz.pwarp.eventListeners.PlayerWarpListeners;
import net.md_5.bungee.api.ChatColor;

class ClickableStackCreateWarp extends ClickableStack {
	private final boolean canCreate;

	public ClickableStackCreateWarp(Player player, boolean canCreate) {
		super(Material.NETHER_STAR, ChatColor.GREEN + "Click to create a warp",
				Arrays.asList(ChatColor.AQUA + "You can create " + ChatColor.BOLD + ChatColor.GOLD
						+ (WarpManager.getInstance().getNumWarpsAllowed(player)
								- WarpManager.getInstance().getAll(player).size())
						+ ChatColor.RESET + ChatColor.AQUA + " more warps"));

		this.canCreate = canCreate;

		if (!canCreate) {
			setMaterial(Material.BARRIER);
			setName(ChatColor.RED + "Can't create any more warps");
			setLore(Arrays.asList(ChatColor.YELLOW + "Delete another warp to create more,",
					ChatColor.YELLOW + "or move existing ones"));
		}
	}

	@Override
	public void onClick(Player player, GuiMenu menu, InventoryClickEvent event) {
		if (canCreate) {
			player.closeInventory();
			PlayerWarpListeners.addCreateChatPlayer(player);
		}
	}

}
