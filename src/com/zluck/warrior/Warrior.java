package com.zluck.warrior;

import org.bukkit.plugin.java.JavaPlugin;

import com.zluck.warrior.commands.WarriorCommand;
import com.zluck.warrior.listeners.ChatListener;
import com.zluck.warrior.listeners.DamageListener;
import com.zluck.warrior.listeners.DeathListener;
import com.zluck.warrior.listeners.QuitListener;
import com.zluck.warrior.managers.Messages;
import com.zluck.warrior.managers.WarriorEvent;

/**
 * 
 * @author Johnny Camacho
 *
 */
public class Warrior extends JavaPlugin {

	private static Warrior instance;
	private WarriorEvent warriorEvent;
	private Messages messages;

	public void onEnable() {
		instance = this;
		warriorEvent = new WarriorEvent();
		messages = new Messages(instance, "messages.yml");

		saveDefaultConfig();

		registerListeners();
		registerCommands();
	}

	public void onDisable() {
		warriorEvent.finalizeWarrior();

		instance = null;
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new DamageListener(), instance);
		getServer().getPluginManager().registerEvents(new DeathListener(), instance);
		getServer().getPluginManager().registerEvents(new QuitListener(), instance);

		if (getServer().getPluginManager().isPluginEnabled("Legendchat")) {
			getServer().getPluginManager().registerEvents(new ChatListener(), instance);
		}
	}

	private void registerCommands() {
		getCommand("warrior").setExecutor(new WarriorCommand());
	}

	public static Warrior getInstance() {
		return instance;
	}

	public WarriorEvent getWarriorEvent() {
		return warriorEvent;
	}

	public Messages getMessagesService() {
		return messages;
	}

}
