package me.itoncek.mtyt.lobby;

import net.ME1312.SubServers.Client.Bukkit.SubAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/*
 * Copyright (c) 2022 IToncek
 *
 * All rights reserved to original developer [IToncek].
 */

public final class Lobby extends JavaPlugin {
	public static SubAPI subAPI;
	public static Plugin plugin;
	public static List<Games> gamesList = List.of(Games.values());
	public static Games nextGame;
	public static FileConfiguration config;
	public static BukkitRunnable runnable = new BukkitRunnable() {
		public int secs = 120;
		
		@Override
		public void run() {
			if(secs == 0) {
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				try {
					out.writeUTF("Connect");
					out.writeUTF(nextGame.name().toLowerCase());
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
				}
				
				this.cancel();
			} else {
				if(secs < 5) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.sendTitle(" ", ChatColor.GOLD.toString() + p);
					}
				} else if(secs < 10) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.sendTitle(" ", ChatColor.GOLD.toString() + p);
					}
				} else {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.sendActionBar(Component.text(ChatColor.GOLD.toString() + p));
					}
				}
				secs--;
			}
		}
	};
	
	public static BukkitRunnable ping = new BukkitRunnable() {
		@Override
		public void run() {
			if(Bukkit.getOnlinePlayers().size() > 0) {
				try {
					URL url = new URL(config.getString("manager-address", "http://localhost:8000"));
					final long before = System.currentTimeMillis();
					Scanner sc = new Scanner(url.openStream());
					final long during = Long.parseLong(sc.nextLine());
					sc.close();
					final long after = System.currentTimeMillis();
					for (Player p : Bukkit.getOnlinePlayers()) {
						if(p.isOp()) {
							p.sendActionBar(Component.text(ChatColor.GREEN + "Outbound ping: " + ChatColor.GOLD + (during - before) + ChatColor.GREEN + "ms | Inbound ping: " + ChatColor.GOLD + (after - during) + ChatColor.GREEN + "ms"));
						}
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	};
	@Override
	public void onEnable() {
		// Plugin startup logic
		plugin = this;
		subAPI = SubAPI.getInstance();
		saveDefaultConfig();
		config = getConfig();
		getCommand("start").setExecutor(new StartCommand());
		ping.runTaskTimer(this, 0, 20);
	}
	
	public static Plugin getInstance() {
		return plugin;
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
