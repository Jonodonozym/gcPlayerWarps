
package jdz.pwarp.config;

import org.bukkit.plugin.Plugin;

import jdz.bukkitUtils.configuration.AutoConfig;
import lombok.Getter;

public class TeleportConfig extends AutoConfig {
	@Getter private static int cooldown = 10;
	@Getter private static int warmUp = 5;
	@Getter private static boolean cancelOnMovement = true;
	@Getter private static String noCooldownPermission = "pwarp.nocooldown";

	public TeleportConfig(Plugin plugin) {
		super(plugin, "Teleport");
	}

}
