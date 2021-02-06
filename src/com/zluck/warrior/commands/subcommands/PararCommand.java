package com.zluck.warrior.commands.subcommands;

import org.bukkit.command.CommandSender;

import com.zluck.warrior.Warrior;

public class PararCommand extends SubCommand {

	public PararCommand(CommandSender sender, String[] args, String permission) {
		super(sender, args, permission);
	}

	@Override
	protected void execute() {
		Warrior.getInstance().getWarriorEvent().finalizeWarrior();
	}

}
