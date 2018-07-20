
package jdz.pwarp.gui;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import jdz.bukkitUtils.guiMenu.guis.GuiMenu;
import jdz.bukkitUtils.misc.Config;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.eventListeners.PlayerWarpListeners;

public class ClickableStackWarpEditable extends ClickableStackWarp {
	private final PlayerWarp warp;

	public ClickableStackWarpEditable(PlayerWarp warp) {
		super(warp);
		this.warp = warp;
		getStack().setType(Material.PAPER);
		getStack().setDurability((short) 0);

		ItemMeta itemMeta = getStack().getItemMeta();

		List<String> lore = itemMeta.getLore();
		lore.remove(0);

		if (Config.getConfig(PlayerWarpPlugin.getInstance()).getBoolean("Rent.enabled"))
			lore.add(0, ChatColor.BLUE + "Rent days paid: " + warp.getRentDaysPaid());

		lore.add(ChatColor.GREEN + "Middle click to rename");
		lore.add(ChatColor.RED + "Right click to delete this warp");

		itemMeta.setLore(lore);
		getStack().setItemMeta(itemMeta);
	}

	@Override
	public void onClick(GuiMenu menu, InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (event.getClick() == ClickType.LEFT)
			super.onClick(menu, event);
		if (event.getClick() == ClickType.MIDDLE) {
			player.closeInventory();
			PlayerWarpListeners.addRenameChatPlayer(player, warp);
		}
		if (event.getClick() == ClickType.RIGHT)
			new GuiMenuConfirmDeletion(player, warp, (GuiMenuMyWarps) menu).open(player);
	}

}
