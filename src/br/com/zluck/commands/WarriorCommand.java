package br.com.zluck.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import br.com.zluck.commands.subcommands.*;
import br.com.zluck.managers.location.LocationManager.LocationType;

public class WarriorCommand implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			new EntrarCommand(sender, args).executeCommand();
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("entrar")) {
				new EntrarCommand(sender, args).executeCommand();
			} else if (args[0].equalsIgnoreCase("sair")) {
				new SairCommand(sender, args).executeCommand();
			} else if (args[0].equalsIgnoreCase("iniciar")) {
				new IniciarCommand(sender, args, "perm.admin").executeCommand();
			} else if (args[0].equalsIgnoreCase("parar")) {
				new PararCommand(sender, args, "perm.admin").executeCommand();
			} else if (args[0].equalsIgnoreCase("camarote")) {
				new CamaroteCommand(sender, args).executeCommand();
			} else {
				error(sender);
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				new SetCommand(sender, args, "perm.admin").executeCommand();
			} else {
				error(sender);
			}
		} else {
			error(sender);
		}

		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			List<String> commands = Arrays.asList("entrar", "sair", "camarote");
			List<String> commandsAdmin = Arrays.asList("entrar", "sair", "camarote", "iniciar", "parar", "set");

			if (sender.hasPermission("perm.admin")) {
				return commandsAdmin.stream().filter(cmd -> cmd.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
			} else {
				return commands.stream().filter(cmd -> cmd.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set") && sender.hasPermission("perm.admin")) {
				List<String> locationsType = LocationType.getLocationsType();
				locationsType.add("Inventory");

				return locationsType.stream().map(cmd -> cmd.toLowerCase()).filter(cmd -> cmd.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
			}
		}

		return null;
	}

	private String error(CommandSender sender) {
		if (sender.hasPermission("perm.admin")) {
			return "§cUse: /warrior <entrar/sair/camarote/iniciar/parar/set>";
		} else {
			return "§cUse: /warrior <entrar/sair/camarote>";
		}
	}

}
