
package jdz.pwarp.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import jdz.bukkitUtils.guiMenu.guis.GuiMenu;
import jdz.bukkitUtils.guiMenu.guis.GuiMenuList;
import jdz.bukkitUtils.guiMenu.itemStacks.ClickableStack;
import jdz.bukkitUtils.guiMenu.itemStacks.ClickableStackLinkedMenu;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.events.WarpCreatedEvent;
import jdz.pwarp.events.WarpDeletedEvent;
import jdz.pwarp.events.WarpLoreEvent;
import jdz.pwarp.events.WarpRenamedEvent;

public class PlayerWarpGuiMenu extends GuiMenu {
	public static PlayerWarpGuiMenu instance;

	private Inventory inventory;

	private GuiMenuList listMenu;

	public PlayerWarpGuiMenu(JavaPlugin plugin) {
		super(plugin);

		if (instance == null) {
			instance = this;

			Bukkit.getPluginManager().registerEvents(new WarpChangeListener(this), plugin);
			listMenu = new GuiMenuList(PlayerWarpPlugin.instance, ChatColor.GREEN+"All Warps", new ArrayList<ClickableStack>(), this);
			refreshList();

			inventory = Bukkit.createInventory(null, 27, ChatColor.GREEN+"Player Warps");

			ClickableStack toListMenu = new ClickableStackLinkedMenu(Material.NETHER_STAR, ChatColor.AQUA+"All Warps", listMenu);
			ClickableStack toMyWarpsMenu = new ClickableStackLinkedMenu(Material.ENDER_CHEST, ChatColor.GREEN+"My Warps", listMenu) {
				@Override
				public void onClick(GuiMenu menu, InventoryClickEvent event) {
					new GuiMenuMyWarps((Player)event.getWhoClicked(), instance).open((Player)event.getWhoClicked());
				}
			};
			
			setItem(toListMenu, 11, inventory);
			setItem(toMyWarpsMenu, 15, inventory);
		}
	}

	public void refreshList() {
		new BukkitRunnable() {
			@Override
			public void run() {
				List<ClickableStack> items = new ArrayList<ClickableStack>();

				List<PlayerWarp> warps = WarpDatabase.instance.getAllWarps();
				
				for (PlayerWarp warp : warps)
					items.add(new ClickableStackWarp(warp));

				listMenu.setItems(items);
			}
		}.runTaskAsynchronously(PlayerWarpPlugin.instance);
	}

	@Override
	public void open(Player player) {
		player.openInventory(inventory);
	}

	private static class WarpChangeListener implements Listener {
		private final PlayerWarpGuiMenu superMenu;

		private WarpChangeListener(PlayerWarpGuiMenu superMenu) {
			this.superMenu = superMenu;
		}

		@EventHandler()
		public void onWarpCreate(WarpCreatedEvent event) {
			superMenu.refreshList();
		}

		@EventHandler()
		public void onWarpDeleted(WarpDeletedEvent event) {
			superMenu.refreshList();
		}

		@EventHandler()
		public void onWarpRename(WarpRenamedEvent event) {
			superMenu.refreshList();
		}

		@EventHandler()
		public void onWarpLoreChange(WarpLoreEvent event) {
			superMenu.refreshList();
		}
	}
}
