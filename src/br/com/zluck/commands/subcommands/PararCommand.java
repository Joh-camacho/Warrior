package br.com.zluck.commands.subcommands;

import org.bukkit.command.CommandSender;

import br.com.zluck.Warrior;

public class PararCommand extends SubCommand {

	public PararCommand(CommandSender sender, String[] args, String permission) {
		super(sender, args, permission);
	}

	@Override
	protected void execute() {
		Warrior.getInstance().getWarriorEvent().finalizeWarrior();
	}

}
