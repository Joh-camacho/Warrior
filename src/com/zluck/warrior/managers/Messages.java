package com.zluck.warrior.managers;

import org.bukkit.plugin.*;
import java.util.*;
import org.bukkit.*;

public class Messages {

	public static final String EVENT_CLOSED = "EventClosed";
	public static final String EVENT_STARTING = "EventStarting";
	public static final String EVENT_STARTED = "EventStarted";
	public static final String EVENT_FINISHED = "EventFinished";
	public static final String INSUFFICIENT_PLAYERS = "InsufficientPlayers";
	public static final String TIME_INVICIBILITY = "TimeInvincibility";
	public static final String TIME_GAME = "TimeGame";
	public static final String ALREADY_PLAYING = "AlreadyPlaying";
	public static final String INV_NOT_EMPTY = "InvNotEmpty";
	public static final String EVENT_FULL = "EventFull";
	public static final String JOIN_EVENT = "JoinEvent";
	public static final String LEFT_EVENT = "LeftEvent";
	public static final String NOT_PLAYING = "NotPlaying";
	public static final String CABIN = "Cabin";

	private Map<String, String> messages;
	private PluginConfig messagesConfig;

	public Messages(Plugin plugin, String name) {
		messages = new HashMap<String, String>();
		messagesConfig = new PluginConfig(plugin, name);

		loadMessages();
	}

	private void loadMessages() {
		loadOrCreate(EVENT_CLOSED, "&cEvento &a&lWARRIOR &cesta fechado!");
		loadOrCreate(EVENT_STARTING, 
				" ", 
				"&e&l » EVENTO WARRIOR INICIADO", 
				"&e&l » ENTRE DIGITANDO &f&l/WARRIOR",
				"&e&l » TEMPO RESTANTE: &f{tempo}", 
				"&e&l » PARTICIPANTES: &f{participantes}", 
				" ");
		loadOrCreate(EVENT_STARTED, 
				" ", 
				"&e&l » PORTAS FECHADAS PARA O EVENTO",
				"&e&l » VEJA O EVENTO NO &a&l/WARRIOR CAMAROTE", 
				"&e&l » PARTICIPANTES: &f{participantes}", 
				" ");
		loadOrCreate(EVENT_FINISHED, 
				" ",
				"&e&l » {winner} &e&lGANHOU O EVENTO &a&lWARRIOR &e&lCOM &a&l{kills} &e&lKILL(S)", 
				" ",
				"&e&l » PREMIOS: &f&l5.000 Cash &e&l+ &f&l$100B &e&l+ &f&lTag &eWarrior", 
				" ");
		loadOrCreate(INSUFFICIENT_PLAYERS, 
				" ", 
				"&e&l » EVENTO &a&lWARRIOR &e&lFINALIZADO POR FALTA DE JOGADORES", 
				" ");
		loadOrCreate(TIME_INVICIBILITY, "&cInvencibilidade acabando em &a{tempo}");
		loadOrCreate(TIME_GAME, "&eTempo: &a{tempo}   |   &eParticipantes: &a{participantes}");
		loadOrCreate(ALREADY_PLAYING, "&cVoce ja esta no evento &a&lWARRIOR");
		loadOrCreate(INV_NOT_EMPTY, "&cSeu inventario precisa estar vazio para entrar no evento.");
		loadOrCreate(EVENT_FULL, "&cO evento &a&lWARRIOR &cesta lotado!");
		loadOrCreate(JOIN_EVENT, "&e{player} &eentrou no evento (&a{participantes}&e/&a{max_players}&e)");
		loadOrCreate(LEFT_EVENT, "&cVoce saiu do evento &a&lWARRIOR.");
		loadOrCreate(NOT_PLAYING, "&cVoce nao esta participando do evento!");
		loadOrCreate(CABIN, "&aVoce foi teleportado pro camarote!");
	}

	private void loadOrCreate(String path, String... def) {
		String message = null;

		if (messagesConfig.contains(path)) {
			if (messagesConfig.isList(path)) {
				message = String.join("\n", messagesConfig.getStringList(path));
			} else if (messagesConfig.isString(path)) {
				message = messagesConfig.getString(path);
			}
		} else {
			if (def.length > 1) {
				messagesConfig.set(path, Arrays.asList(def));
				message = String.join("\n", def);
			} else {
				messagesConfig.set(path, def[0]);
				message = def[0];
			}

			messagesConfig.save();
		}

		messages.put(path, ChatColor.translateAlternateColorCodes('&', message));
	}

	public String getMessage(String path) {
		return messages.get(path);
	}

}
