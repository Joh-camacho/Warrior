package com.zluck.warrior.commands.subcommands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zluck.warrior.Warrior;
import com.zluck.warrior.managers.location.LocationConstructor;
import com.zluck.warrior.managers.location.LocationManager;
import com.zluck.warrior.managers.location.LocationManager.LocationType;
import com.zluck.warrior.utils.Utils;

public class SetCommand extends SubCommand {

	public SetCommand(CommandSender sender, String[] args, String permission) {
		super(sender, args, permission);
	}

	@Override
	protected void execute() {
		Player player = (Player) sender;
		LocationConstructor location = new LocationConstructor(player.getLocation());

		try {
			LocationType locationType = LocationType.valueOf(args[1].toUpperCase());
			LocationManager.setLocation(locationType, location);

			player.sendMessage("§aLocal §e" + locationType.toString().toLowerCase() + " §asetado com sucesso.");
		} catch (IllegalArgumentException ex) {
			if (args[1].equalsIgnoreCase("inventory")) {
				Warrior.getInstance().getConfig().set("Inventory.Contents", Utils.contentsToBase64(player.getInventory().getContents()));
				Warrior.getInstance().getConfig().set("Inventory.Armor", Utils.contentsToBase64(player.getInventory().getArmorContents()));
				Warrior.getInstance().saveConfig();

				player.sendMessage("§aInventario setado com sucesso.");
			} else {
				List<String> locationsType = LocationType.getLocationsType();
				locationsType.add("inventory");

				sender.sendMessage("§cUse: /warrior set <" + String.join("/", locationsType).toLowerCase() + ">");
			}
		}
	}

}
