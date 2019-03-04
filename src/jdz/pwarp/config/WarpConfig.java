
package jdz.pwarp.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import jdz.bukkitUtils.components.events.custom.ConfigReloadEvent;
import jdz.bukkitUtils.configuration.AutoConfig;
import lombok.Getter;

public class WarpConfig extends AutoConfig {
	@Getter private static int warpCost = 100000;
	@Getter private static List<String> disabledWorlds = new ArrayList<>();

	@Getter private static int maxLoreSize = 90;
	@Getter private static int loreLineWidth = 30;

	@Getter private static boolean log = true;

	@Getter private static String menuReturnCommand = "";

	@Getter private static boolean GriefPreventionEnabled = false;
	@Getter private static boolean PlotSquaredEnabled = false;
	@Getter private static boolean ASkyblockEnabled = false;

	public WarpConfig(Plugin plugin) {
		super(plugin, "Settings");
	}

	@Override
	public void onConfigReload(ConfigReloadEvent event) {
		super.onConfigReload(event);
		loreLineWidth = maxLoreSize / 3;
	}
}
