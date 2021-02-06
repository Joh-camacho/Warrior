package com.zluck.warrior.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.zluck.warrior.Warrior;

public class DamageListener implements Listener {

	@EventHandler
	private void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();

		if (Warrior.getInstance().getWarriorEvent().isWarrior(player)) {
			if (Warrior.getInstance().getWarriorEvent().isInvincibility()
					|| Warrior.getInstance().getWarriorEvent().isFinishing()) {
				event.setDamage(0.0);
				event.setCancelled(true);
			}
		}
	}

}
