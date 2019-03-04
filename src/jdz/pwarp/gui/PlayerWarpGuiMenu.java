
package jdz.pwarp.gui;

import java.util.ArrayList;
import java.util.Arrays;
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

import jdz.bukkitUtils.components.guiMenu.guis.GuiMenu;
import jdz.bukkitUtils.components.guiMenu.guis.GuiMenuList;
import jdz.bukkitUtils.components.guiMenu.itemStacks.ClickableStack;
import jdz.bukkitUtils.components.guiMenu.itemStacks.ClickableStackCommands;
import jdz.bukkitUtils.components.guiMenu.itemStacks.ClickableStackLinkedMenu;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.config.WarpConfig;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpManager;
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
			listMenu = new GuiMenuList(PlayerWarpPlugin.getInstance(), ChatColor.GREEN + "All Warps",
					new ArrayList<ClickableStack>(), this);
			refreshList();

			inventory = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Player Warps");

			ClickableStack toListMenu = new ClickableStackLinkedMenu(Material.NETHER_STAR, ChatColor.AQUA + "All Warps",
					listMenu);
			ClickableStack toMyWarpsMenu = new ClickableStackLinkedMenu(Material.ENDER_CHEST,
					ChatColor.GREEN + "My Warps", listMenu) {
				@Override
				public void onClick(Player player, GuiMenu menu, InventoryClickEvent event) {
					new GuiMenuMyWarps(player, instance).open(player);
				}
			};

			setItem(toListMenu, 11, inventory);
			setItem(toMyWarpsMenu, 15, inventory);

			ClickableStackCommands returnArrow = new ClickableStackCommands(Material.ARROW,
					ChatColor.AQUA + (WarpConfig.getMenuReturnCommand().equals("") ? "Exit" : "Return"), false,
					Arrays.asList(WarpConfig.getMenuReturnCommand()));

			if (WarpConfig.getMenuReturnCommand().equals(""))
				returnArrow.closeOnClick();

			setItem(returnArrow, 22, inventory);
		}
	}

	public void refreshList() {
		new BukkitRunnable() {
			@Override
			public void run() {
				List<ClickableStack> items = new ArrayList<>();

				List<PlayerWarp> warps = WarpManager.getInstance().getAllWarps();

				for (PlayerWarp warp : warps)
					items.add(new ClickableStackWarp(warp));

				listMenu.setItems(items);
			}
		}.runTaskAsynchronously(PlayerWarpPlugin.getInstance());
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
