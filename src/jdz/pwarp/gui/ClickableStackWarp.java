
package jdz.pwarp.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;

import jdz.bukkitUtils.guiMenu.guis.GuiMenu;
import jdz.bukkitUtils.guiMenu.itemStacks.ClickableStack;
import jdz.pwarp.data.PlayerWarp;
import net.md_5.bungee.api.ChatColor;

class ClickableStackWarp extends ClickableStack{
	protected final PlayerWarp warp;
	
	public ClickableStackWarp(PlayerWarp warp) {
		super(Material.SKULL_ITEM, ChatColor.AQUA+warp.getName(),
				Arrays.asList(ChatColor.GREEN+"Owner: "+warp.getOwner().getName(),
						"", ChatColor.GREEN+"Click to warp"));
		
		this.warp = warp;
		
		getStack().setDurability((short)3);
		
		SkullMeta skullMeta = (SkullMeta) getStack().getItemMeta();
		skullMeta.setOwner(warp.getOwner().getName());
		getStack().setItemMeta(skullMeta);
	}	
	
	@Override
	public void onClick(GuiMenu menu, InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		player.closeInventory();
		warp.warp(player);
	}
}
