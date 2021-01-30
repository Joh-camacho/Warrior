package br.com.zluck.commands.subcommands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zluck.Warrior;
import br.com.zluck.managers.location.Location;
import br.com.zluck.managers.location.LocationManager;
import br.com.zluck.managers.location.LocationManager.LocationType;
import br.com.zluck.utils.Utils;

public class SetCommand extends SubCommand {

	public SetCommand(CommandSender sender, String[] args, String permission) {
		super(sender, args, permission);
	}

	@Override
	protected void execute() {
		Player player = (Player) sender;
		Location location = new Location(player.getLocation());

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
