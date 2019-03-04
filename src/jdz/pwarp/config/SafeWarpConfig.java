
package jdz.pwarp.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import jdz.bukkitUtils.configuration.AutoConfig;
import lombok.Getter;

public class SafeWarpConfig extends AutoConfig {
	@Getter private static boolean enabled = true;
	@Getter private static List<Integer> unsafeBlocks = new ArrayList<>();

	public SafeWarpConfig(Plugin plugin) {
		super(plugin, "SafeWarp");
	}

}
