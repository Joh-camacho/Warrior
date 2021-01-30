package br.com.zluck.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.zluck.Warrior;

public class QuitListener implements Listener {

	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		Warrior.getInstance().getWarriorEvent().quitWarrior(player);
	}

}
