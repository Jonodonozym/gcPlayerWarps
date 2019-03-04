
package jdz.pwarp.config;

import org.bukkit.plugin.Plugin;

import jdz.bukkitUtils.configuration.AutoConfig;
import lombok.Getter;

public class RentConfig extends AutoConfig {
	@Getter private static boolean enabled = true;
	@Getter private static double cost = 10000;
	@Getter private static int freeDays = 5;
	@Getter private static int maxDays = 50;

	public RentConfig(Plugin plugin) {
		super(plugin, "Rent");
	}

}
