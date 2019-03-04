
package jdz.pwarp.eventListeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import jdz.pwarp.config.TeleportConfig;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.events.WarpGoEvent;
import jdz.pwarp.events.WarpRequestEvent;
import net.md_5.bungee.api.ChatColor;

class WarpRequestListener implements Listener {
	private final Map<Player, Integer> timers = new HashMap<>();
	private final Map<Player, Integer> cooldowns = new HashMap<>();
	private final Map<Player, Location> locations = new HashMap<>();

	private final Plugin plugin;

	public WarpRequestListener(Plugin plugin) {
		Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			onSecond();
		}, 20, 20);
		this.plugin = plugin;
	}

	private void onSecond() {
		List<Player> toRemove = new ArrayList<>();

		for (Player player : timers.keySet()) {
			Location newLocation = player.getLocation();
			if (newLocation.distance(locations.get(player)) > 1) {
				player.sendMessage(ChatColor.GRAY + "Teleportation cancelled");
				toRemove.add(player);
			}
		}

		for (Player player : toRemove) {
			timers.remove(player);
			locations.remove(player);
		}

		toRemove.clear();
		for (Player player : cooldowns.keySet()) {
			cooldowns.put(player, cooldowns.get(player) - 1);
			if (cooldowns.get(player) <= 0)
				toRemove.add(player);
		}
		for (Player player : toRemove)
			cooldowns.remove(player);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarpRequest(WarpRequestEvent event) {
		if (!event.isCancelled()) {
			Player player = (Player) event.getCause();

			if (timers.containsKey(player))
				player.sendMessage(ChatColor.RED + "You are already warping somewhere");
			else if (cooldowns.containsKey(player))
				player.sendMessage(
						ChatColor.RED + "You must wait " + cooldowns.get(player) + " seconds before warping again");
			else if (player.hasPermission("pwarp.instanttp"))
				new WarpGoEvent(player, event.getWarp()).call();
			else
				scheduleWarp(new WarpGoEvent(player, event.getWarp()));
		}
	}

	private void scheduleWarp(WarpGoEvent event) {
		Player player = (Player) event.getCause();
		PlayerWarp warp = event.getWarp();

		timers.put(player, TeleportConfig.getWarmUp());
		locations.put(player, player.getLocation());

		player.sendMessage(ChatColor.GREEN + "Teleporting you to " + warp.getOwner().getName() + "'s warp "
				+ warp.getName() + " in " + TeleportConfig.getWarmUp() + "s");

		new BukkitRunnable() {
			@Override
			public void run() {
				if (timers.containsKey(player)) {
					timers.remove(player);
					locations.remove(player);
					event.call();
				}
			}
		}.runTaskLater(plugin, TeleportConfig.getWarmUp() * 20L);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWarp(WarpGoEvent event) {
		if (event.isCancelled())
			return;

		Player player = (Player) event.getCause();
		PlayerWarp warp = event.getWarp();

		if (!event.getCause().hasPermission(TeleportConfig.getNoCooldownPermission()))
			cooldowns.put(player, TeleportConfig.getCooldown());

		player.teleport(warp.getLocation().add(0, 1, 0));
		player.sendMessage(ChatColor.GREEN + "Teleported you to "
				+ (warp.getOwner().isOnline() ? warp.getOwner().getPlayer().getDisplayName()
						: warp.getOwner().getName())
				+ ChatColor.GREEN + "'s warp " + warp.getName());

		try {
			for (Player everyone : Bukkit.getOnlinePlayers())
				everyone.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 10, 1);
		}
		catch (NoSuchFieldError e) {}

		if (warp.getOwner().isOnline())
			warp.getOwner().getPlayer().sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.GREEN
					+ " warped to your " + warp.getName() + " warp");
	}
}
