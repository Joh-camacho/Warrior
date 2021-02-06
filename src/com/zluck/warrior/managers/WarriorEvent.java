package com.zluck.warrior.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.zluck.warrior.Warrior;
import com.zluck.warrior.managers.location.LocationManager;
import com.zluck.warrior.managers.location.LocationManager.LocationType;
import com.zluck.warrior.utils.Utils;

public class WarriorEvent {

	public enum StateType {
		LOBBY, GAME, END;
	}

	private List<Player> warriors;
	private Map<Player, Integer> kills;
	private StateType stateType;
	private int timeGame;
	private BukkitTask taskLobby, taskGame, taskFinalize;

	public void startEvent() {
		boolean eventAlreadyInitialized = taskLobby != null || taskGame != null || taskFinalize != null;
		if (eventAlreadyInitialized)
			throw new IllegalStateException("O evento WARRIOR ja esta em andamento.");

		// Checking if all locations are defined in the config.
		Arrays.stream(LocationType.values()).forEach(LocationManager::getLocation);

		warriors = new ArrayList<>();
		kills = new HashMap<>();
		stateType = StateType.LOBBY;
		timeGame = 0;

		int delay = Warrior.getInstance().getConfig().getInt("Timers.Starting.Delay");
		taskLobby = new BukkitRunnable() {
			int announcements = Warrior.getInstance().getConfig().getInt("Timers.Starting.Announcements");

			public void run() {
				if (announcements == 0) {
					startWarrior();
				} else {
					Bukkit.broadcastMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.EVENT_STARTING)
							.replace("{tempo}", Utils.getTime(announcements * delay))
							.replace("{participantes}", String.valueOf(warriors.size())));

					announcements--;
				}
			}
		}.runTaskTimer(Warrior.getInstance(), 0, delay * 20);
	}

	private void startWarrior() {
		if (warriors.size() < Warrior.getInstance().getConfig().getInt("MinPlayers")) {
			Bukkit.broadcastMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.INSUFFICIENT_PLAYERS));

			finalizeWarrior();
			return;
		}

		cancelTaskLobby();
		stateType = StateType.GAME;

		ItemStack[] contents = Utils.contentsFromBase64(Warrior.getInstance().getConfig().getString("Inventory.Contents"));
		ItemStack[] armor = Utils.contentsFromBase64(Warrior.getInstance().getConfig().getString("Inventory.Armor"));
		Location location = LocationManager.getLocation(LocationType.SPAWN);

		warriors.forEach(player -> {
			Utils.clearPlayer(player);

			if (contents != null)
				player.getInventory().setContents(contents);
			if (armor != null)
				player.getInventory().setArmorContents(armor);

			player.teleport(location);
		});

		Bukkit.broadcastMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.EVENT_STARTED)
				.replace("{participantes}", Integer.toString(warriors.size())));

		int timeInvincibility = Warrior.getInstance().getConfig().getInt("Timers.Invincibility");
		taskGame = new BukkitRunnable() {
			public void run() {
				if (isInvincibility()) {
					warriors.forEach(player -> Utils.sendActionBar(player,
							Warrior.getInstance().getMessagesService().getMessage(Messages.TIME_INVICIBILITY)
									.replace("{tempo}", Utils.getTime(timeInvincibility - timeGame))));
				} else {
					warriors.forEach(player -> Utils.sendActionBar(player,
							Warrior.getInstance().getMessagesService().getMessage(Messages.TIME_GAME)
									.replace("{tempo}", Utils.getTime(timeGame))
									.replace("{participantes}", String.valueOf(warriors.size()))));
				}

				timeGame++;
			}
		}.runTaskTimer(Warrior.getInstance(), 0, 20);
	}

	public void finalizeWarrior() {
		if (warriors != null && !warriors.isEmpty()) {
			Location location = LocationManager.getLocation(LocationType.SAIDA);

			warriors.forEach(player -> {
				Utils.clearPlayer(player);

				player.teleport(location);
			});
		}

		cancelTaskLobby();
		cancelTaskGame();
		cancelTaskFinalize();

		warriors = null;
		kills = null;
		stateType = StateType.END;
	}

	public void joinWarrior(Player player) {
		if (stateType != StateType.LOBBY) {
			player.sendMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.EVENT_CLOSED));
			return;
		}
		if (warriors.contains(player)) {
			player.sendMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.ALREADY_PLAYING));
			return;
		}
		if (Utils.invNotEmpty(player)) {
			player.sendMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.INV_NOT_EMPTY));
			return;
		}

		int maxPlayers = Warrior.getInstance().getConfig().getInt("MaxPlayers");
		if (warriors.size() == maxPlayers) {
			player.sendMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.EVENT_FULL));
			return;
		}

		Utils.clearPlayer(player);

		player.teleport(LocationManager.getLocation(LocationType.LOBBY));

		warriors.add(player);
		warriors.forEach(players -> Utils.sendActionBar(players,
				Warrior.getInstance().getMessagesService().getMessage("JoinEvent")
						.replace("{player}", player.getDisplayName())
						.replace("{max_players}", String.valueOf(maxPlayers))
						.replace("{participantes}", String.valueOf(warriors.size()))));
	}

	public void leftWarrior(Player player) {
		if (!isWarrior(player)) {
			player.sendMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.NOT_PLAYING));
			return;
		}
		
		quitWarrior(player);
		
		player.teleport(LocationManager.getLocation(LocationType.SAIDA));
		player.sendMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.LEFT_EVENT));
	}

	public void deathWarrior(Player player) {
		if (player.getKiller() != null && isWarrior(player.getKiller())) 
			addKill(player.getKiller());

		quitWarrior(player);
	}

	public void quitWarrior(Player player) {
		Utils.clearPlayer(player);
		removeWarrior(player);
		checkWinner();
	}

	public void removeWarrior(Player player) {
		warriors.remove(player);
	}

	public boolean isWarrior(Player player) {
		return warriors != null && warriors.contains(player);
	}

	public boolean isInvincibility() {
		return stateType == StateType.GAME
				&& timeGame <= Warrior.getInstance().getConfig().getInt("Timers.Invincibility");
	}

	public boolean isFinishing() {
		return taskFinalize != null;
	}

	public StateType getStateType() {
		return stateType;
	}

	public void addKill(Player player) {
		int kill = 1;
		
		if (kills.containsKey(player)) 
			kill += kills.get(player);

		kills.put(player, kill);
	}

	private boolean checkWinner() {
		if (stateType == StateType.LOBBY)
			return false;

		if (warriors.isEmpty()) {
			finalizeWarrior();
			return false;
		}

		if (warriors.size() == 1 && stateType == StateType.GAME) {
			Player winner = warriors.stream().findFirst().get();

			List<String> commandsReward = Warrior.getInstance().getConfig().getStringList("RewardWinner");
			commandsReward.forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{winner}", winner.getName())));

			Bukkit.broadcastMessage(Warrior.getInstance().getMessagesService().getMessage(Messages.EVENT_FINISHED)
					.replace("{winner}", winner.getDisplayName())
					.replace("{kills}", kills.containsKey(winner) ? String.valueOf(kills.get(winner)) : "0"));

			Warrior.getInstance().getConfig().set("Winner", winner.getName());
			Warrior.getInstance().saveConfig();

			cancelTaskGame();

			stateType = StateType.END;
			taskFinalize = new BukkitRunnable() {
				int timeFinalize = Warrior.getInstance().getConfig().getInt("Timers.Finishing");

				public void run() {
					boolean winnerOffline = winner == null || !winner.isOnline();

					if (timeFinalize == 0 || winnerOffline) {
						finalizeWarrior();
					} else {
						Utils.spawnFirework(winner.getLocation());
						timeFinalize--;
					}
				}
			}.runTaskTimer(Warrior.getInstance(), 0L, 20L);
			return true;
		}

		return false;
	}

	private void cancelTaskLobby() {
		if (taskLobby != null) {
			taskLobby.cancel();
			taskLobby = null;
		}
	}

	private void cancelTaskGame() {
		if (taskGame != null) {
			taskGame.cancel();
			taskGame = null;
		}
	}

	private void cancelTaskFinalize() {
		if (taskFinalize != null) {
			taskFinalize.cancel();
			taskFinalize = null;
		}
	}

}
