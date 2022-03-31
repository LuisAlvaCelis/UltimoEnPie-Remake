package net.ultimoenpie.apis.anvilgui;

import org.bukkit.Bukkit;


import java.util.Arrays;
import java.util.List;

public class VersionMatcher {

	private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
	private final List<Class<? extends VersionWrapper>> versions = Arrays.asList(Wrapper1_16_R1.class);

	public VersionWrapper match() {
		try {
			return versions.stream().filter(version -> version.getSimpleName().substring(7).equals(serverVersion)).findFirst().orElseThrow(() -> new RuntimeException("Your server version isn't supported in AnvilGUI!")).newInstance();
		} catch (IllegalAccessException | InstantiationException ex) {
			throw new RuntimeException(ex);
		}
	}

}
