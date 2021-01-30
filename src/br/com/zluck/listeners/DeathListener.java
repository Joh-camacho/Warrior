package br.com.zluck.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zluck.Warrior;
import br.com.zluck.managers.location.LocationManager;
import br.com.zluck.managers.location.LocationManager.LocationType;

public class DeathListener implements Listener {

	@EventHandler
	private void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();

		if (Warrior.getInstance().getWarriorEvent().isWarrior(player)) {
			event.getDrops().clear();

			new BukkitRunnable() {
				public void run() {
					player.spigot().respawn();
				}
			}.runTask(Warrior.getInstance());
		}
	}

	@EventHandler
	private void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		if (Warrior.getInstance().getWarriorEvent().isWarrior(player)) {
			Warrior.getInstance().getWarriorEvent().deathWarrior(player);

			event.setRespawnLocation(LocationManager.getLocation(LocationType.SAIDA));
		}
	}

}
