package com.zluck.warrior.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zluck.warrior.Warrior;

public class EntrarCommand extends SubCommand {

	public EntrarCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
	protected void execute() {
		Player player = (Player) sender;

		Warrior.getInstance().getWarriorEvent().joinWarrior(player);
	}

}
