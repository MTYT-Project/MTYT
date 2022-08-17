package me.itoncek.mtyt.lobby;

import io.sentry.Sentry;
import net.ME1312.SubServers.Client.Bukkit.SubAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2022 IToncek
 *
 * All rights reserved to original developer [IToncek].
 */

public final class Lobby extends JavaPlugin {
	public static SubAPI subAPI;
	public static Plugin plugin;
	public static List<Games> gamesList = new ArrayList<>(List.of(Games.values()));
	public static Games nextGame;
	public static BukkitRunnable runnable = new BukkitRunnable() {
		public int secs = 120;
		public boolean started = false;
		public String server = "";
		@Override
		public void run() {
			if (started) {
				if(secs == 0) {
					ByteArrayOutputStream b = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(b);
					try {
						out.writeUTF("Connect");
						out.writeUTF("");
					} catch (IOException e) {
						Sentry.captureException(e);
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
			} else {
				subAPI.getHost("~", (host -> host.start(nextGame.toString().toLowerCase())));
			}
		}
	};
	
	public static BukkitRunnable ping = new BukkitRunnable() {
		@Override
		public void run() {
		
		}
	};
	@Override
	public void onEnable() {
		Sentry.init(options -> {
			options.setDsn("https://10c3bc41f40a46dca36e796325621ad7@o1345402.ingest.sentry.io/6621893");
			// Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
			// We recommend adjusting this value in production.
			options.setTracesSampleRate(1.0);
			// When first trying Sentry it's good to see what the SDK is doing:
			options.setDebug(true);
			options.setLogger(new BukkitLogger());
		});
		// Plugin startup logic
		plugin = this;
		subAPI = SubAPI.getInstance();
	}
	
	public static Plugin getInstance() {
		return plugin;
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
