package me.itoncek.mtyt.lobby;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

import static me.itoncek.mtyt.lobby.Lobby.gamesList;
import static me.itoncek.mtyt.lobby.Lobby.nextGame;

/*
 * Copyright (c) 2022 IToncek
 *
 * All rights reserved to original developer [IToncek].
 */

/**
 * Class for /start command.
 * @author IToncek
 */
public class StartCommand implements CommandExecutor {
	/**
	 * Executes the command.
	 * @param sender The sender of the command.
	 * @param command The command.
	 * @param label The label of the command.
	 * @param args The arguments of the command.
	 * @return True if the command was executed successfully, false otherwise.
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(sender.isOp()) {
			SecureRandom rnd = new SecureRandom();
			nextGame = gamesList.get(rnd.nextInt(gamesList.size()));
			Lobby.runnable.runTaskTimer(Lobby.getInstance(), 0, 20);
		}
		return true;
	}
}
