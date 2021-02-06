package com.zluck.warrior.commands.subcommands;

import org.bukkit.command.CommandSender;

import com.zluck.warrior.Warrior;

public class IniciarCommand extends SubCommand {

	public IniciarCommand(CommandSender sender, String[] args, String permission) {
		super(sender, args, permission);
	}

	@Override
	protected void execute() {
		try {
			Warrior.getInstance().getWarriorEvent().startEvent();
		} catch (NullPointerException | IllegalStateException ex) {
			sender.sendMessage("§c" + ex.getMessage());
		}
	}

}
