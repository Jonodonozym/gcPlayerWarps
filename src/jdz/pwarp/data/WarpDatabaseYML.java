
package jdz.pwarp.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.bukkitUtils.misc.Config;
import jdz.bukkitUtils.misc.WorldUtils;
import jdz.pwarp.PlayerWarpPlugin;
import lombok.Getter;

public class WarpDatabaseYML implements WarpDatabase {
	@Getter private static final WarpDatabaseYML instance = new WarpDatabaseYML();
	private final FileConfiguration config = Config.getConfig(PlayerWarpPlugin.getInstance(), "playerWarps.yml");
	private final File configFile = Config.getConfigFile(PlayerWarpPlugin.getInstance(), "playerWarps.yml");
	private final FileLogger logger = new FileLogger(PlayerWarpPlugin.getInstance());

	private WarpDatabaseYML() {}

	private void save() {
		try {
			config.save(configFile);
		}
		catch (IOException e) {
			logger.createErrorLog(e);
		}
	}

	@Override
	public void addWarp(PlayerWarp warp) {
		String key = warp.getOwner().getUniqueId() + "." + warp.getName();
		if (!config.contains(key)) {
			config.set(key + ".location", WorldUtils.locationToString(warp.getLocation()));
			config.set(key + ".RentDaysPaid", warp.getRentDaysPaid());
			for (int i = 0; i < warp.getLore().size(); i++)
				config.set(key + ".lore" + i, warp.getLore().get(i));
			save();
		}
	}

	@Override
	public void move(PlayerWarp warp, Location l) {
		String key = warp.getOwner().getUniqueId() + "." + warp.getName();
		if (config.contains(key)) {
			config.set(key + ".location", WorldUtils.locationToString(warp.getLocation()));
			save();
		}
	}

	@Override
	public void delWarp(PlayerWarp warp) {
		String key = warp.getOwner().getUniqueId() + "." + warp.getName();
		if (config.contains(key)) {
			config.set(key, null);
			save();
		}
	}

	@Override
	public void rename(PlayerWarp warp, String newName) {
		String key = warp.getOwner().getUniqueId() + "." + warp.getName();
		if (config.contains(key)) {
			config.set(key, null);
			PlayerWarp newWarp = warp.clone();
			newWarp.setName(newName);
			addWarp(newWarp);
			save();
		}
	}

	@Override
	public void setLore(PlayerWarp warp, String lore, int index) {
		String key = warp.getOwner().getUniqueId() + "." + warp.getName();
		if (config.contains(key)) {
			config.set(key + ".lore" + index, lore);
			save();
		}
	}

	@Override
	public void setRentDays(PlayerWarp warp, int daysPaid) {
		String key = warp.getOwner().getUniqueId() + "." + warp.getName();
		if (config.contains(key)) {
			config.set(key + ".RentDaysPaid", daysPaid);
			save();
		}
	}

	@Override
	public void decreaseRentDays() {
		decreaseRentDays(1);
	}

	@Override
	public void decreaseRentDays(int daysPaid) {
		for (String uuid : config.getKeys(false))
			for (String warp : config.getConfigurationSection(uuid).getKeys(false)) {
				int newDaysPaid = config.getInt(warp + ".RentDaysPaid") - 1;
				config.set(warp + ".RentDaysPaid", newDaysPaid);
			}
	}

	@Override
	public List<PlayerWarp> getAllWarps() {
		List<PlayerWarp> warps = new ArrayList<PlayerWarp>();
		for (String uuid : config.getKeys(false))
			for (String warpName : config.getConfigurationSection(uuid).getKeys(false)) {
				String key = uuid + "." + warpName;
				OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
				Location l = WorldUtils.locationFromString(config.getString(key + ".location"));

				List<String> lore = new ArrayList<String>();
				for (int i = 0; i < 3; i++)
					lore.add(config.getString(key + ".lore" + i));

				int rentDaysPaid = config.getInt(key + ".RentDaysPaid");

				PlayerWarp warp = new PlayerWarp(owner, l, warpName, lore, rentDaysPaid);
				System.out.println(warp);

				warps.add(warp);
			}
		return warps;
	}

	@Override
	public void runOnConnect(Runnable r) {
		r.run();
	}

}
