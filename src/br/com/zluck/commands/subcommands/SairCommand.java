package br.com.zluck.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zluck.Warrior;

public class SairCommand extends SubCommand {

	public SairCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
	protected void execute() {
		Player player = (Player) this.sender;

		Warrior.getInstance().getWarriorEvent().leftWarrior(player);
	}

}
