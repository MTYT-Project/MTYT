package me.itoncek.mtyt.lobby;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Copyright (c) 2022 IToncek
 *
 * All rights reserved to original developer [IToncek].
 */

public class BukkitLogger implements ILogger {
	public static String prefix = "[Lobby] ";
	@Override
	public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Object... args) {
		switch (level){
			case DEBUG:
				Bukkit.getLogger().fine(prefix+message);
				break;
			case INFO:
				Bukkit.getLogger().info(prefix+message);
				break;
			case WARNING:
				Bukkit.getLogger().warning(prefix+message);
				break;
			case ERROR:
			case FATAL:
				Bukkit.getLogger().severe(prefix+message);
				break;
		}
	}
	
	@Override
	public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Throwable throwable) {
		switch (level){
			case DEBUG:
				Bukkit.getLogger().fine(prefix+message);
				break;
			case INFO:
				Bukkit.getLogger().info(prefix+message);
				break;
			case WARNING:
				Bukkit.getLogger().warning(prefix+message);
				break;
			case ERROR:
			case FATAL:
				Bukkit.getLogger().severe(prefix+message);
				break;
		}
	}
	
	@Override
	public void log(@NotNull SentryLevel level, @Nullable Throwable throwable, @NotNull String message, @Nullable Object... args) {
		switch (level){
			case DEBUG:
				Bukkit.getLogger().fine(prefix+message);
				break;
			case INFO:
				Bukkit.getLogger().info(prefix+message);
				break;
			case WARNING:
				Bukkit.getLogger().warning(prefix+message);
				break;
			case ERROR:
			case FATAL:
				Bukkit.getLogger().severe(prefix+message);
				break;
		}
		
	}
	
	@Override
	public boolean isEnabled(@Nullable SentryLevel level) {
		return true;
	}
}
