package br.com.zluck.commands.subcommands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

	protected CommandSender sender;
	protected String[] args;
	protected String permission;

	public SubCommand(CommandSender sender, String[] args, String permission) {
		this.sender = sender;
		this.args = args;
		this.permission = permission;
	}

	public SubCommand(CommandSender sender, String[] args) {
		this(sender, args, null);
	}

	protected abstract void execute();

	private boolean hasPermission() {
		return permission == null || sender.hasPermission(permission);
	}

	public void executeCommand() {
		if (!hasPermission()) {
			sender.sendMessage("§cVoce nao tem permissao para executar esse comando.");
			return;
		}

		try {
			execute();
		} catch (ClassCastException ex) {
			sender.sendMessage("§cComando liberado somente para players.");
		}
	}
}
