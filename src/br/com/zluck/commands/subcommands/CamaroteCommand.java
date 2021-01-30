package br.com.zluck.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zluck.Warrior;
import br.com.zluck.managers.Messages;
import br.com.zluck.managers.location.LocationManager;
import br.com.zluck.managers.location.LocationManager.LocationType;

public class CamaroteCommand extends SubCommand {

	public CamaroteCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
	protected void execute() {
		Player player = (Player) sender;

		try {
			player.teleport(LocationManager.getLocation(LocationType.CAMAROTE));
			player.sendMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.CABIN));
		} catch (NullPointerException ex) {
			player.sendMessage("§c" + ex.getMessage());
		}
	}

}
