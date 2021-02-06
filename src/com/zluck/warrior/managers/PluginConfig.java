package com.zluck.warrior.managers;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;
import java.io.*;
import org.bukkit.configuration.*;

public class PluginConfig extends YamlConfiguration {

	private File file;
	private Plugin plugin;

	public PluginConfig(Plugin plugin, File file) {
		this.file = file;
		this.plugin = plugin;

		load();
	}

	public PluginConfig(Plugin plugin, String name) {
		this(plugin, new File(plugin.getDataFolder(), name));
	}

	public void load() {
		if (!file.isFile()) {
			if (plugin.getResource(file.getName()) != null) {
				plugin.saveResource(file.getName(), false);
			} else {
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}

				try {
					file.createNewFile();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		try {
			load(file);
		} catch (IOException | InvalidConfigurationException ex) {
			ex.printStackTrace();
		}
	}

	public void save() {
		try {
			save(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void reload() {
		save();
		load();
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public String getFileName() {
		return file.getName();
	}

}
