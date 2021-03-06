
package jdz.pwarp.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;

import jdz.bukkitUtils.components.guiMenu.guis.GuiMenu;
import jdz.bukkitUtils.components.guiMenu.itemStacks.ClickableStack;
import jdz.pwarp.data.PlayerWarp;
import net.md_5.bungee.api.ChatColor;

class ClickableStackWarp extends ClickableStack {
	protected final PlayerWarp warp;

	public ClickableStackWarp(PlayerWarp warp) {
		super(Material.PLAYER_HEAD, ChatColor.AQUA + warp.getName(), Arrays.asList(
				ChatColor.GREEN + "Owner: " + warp.getOwner().getName(), "", ChatColor.GREEN + "Click to warp"));

		this.warp = warp;

		SkullMeta skullMeta = (SkullMeta) getStack().getItemMeta();
		skullMeta.setOwningPlayer(warp.getOwner());

		List<String> newLore = skullMeta.getLore();
		for (String s : warp.getLore())
			if (!s.equals(""))
				newLore.add(s);
		skullMeta.setLore(newLore);

		getStack().setItemMeta(skullMeta);
	}

	@Override
	public void onClick(Player player, GuiMenu menu, InventoryClickEvent event) {
		player.closeInventory();
		warp.warp(player);
	}
}
