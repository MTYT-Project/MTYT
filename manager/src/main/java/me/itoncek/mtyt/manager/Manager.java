package me.itoncek.mtyt.manager;

import net.ME1312.Galaxi.Library.Version.Version;
import net.ME1312.SubServers.Bungee.Host.Host;
import net.ME1312.SubServers.Bungee.Host.SubCreator;
import net.ME1312.SubServers.Bungee.Host.SubServer;
import net.ME1312.SubServers.Bungee.SubAPI;
import net.md_5.bungee.api.plugin.Plugin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/*
 * Copyright (c) 2022 IToncek
 *
 * All rights reserved to original developer [IToncek].
 */

public final class Manager extends Plugin {
	
	public static Thread server = new Thread(new ServerRunnable());
	public static JSONObject config = new JSONObject();
	public static HashMap<String, Long> points = new HashMap<>();
	public static Logger l;
	
	@Override
	public void onEnable() {
		
		l = getLogger();
		this.getLogger().info("Loading config...");
		File cfg = new File("plugins/MTYT/config.json");
		try {
			if(!cfg.exists()) {
				if(!cfg.toPath().getParent().toFile().exists()) {
					cfg.toPath().getParent().toFile().mkdirs();
				}
				getLogger().info("Config not found, creating...");
				cfg.createNewFile(); // ignoring boolean
				getLogger().info("Config created!");
				getLogger().info("Please fill out the config and restart the server.");
				getLogger().info("Setting values to default options...");
				JSONObject defaultCFG = new JSONObject();
				
				// Manager config
				JSONObject manager = new JSONObject();
				manager.put("port", 8000);
				
				defaultCFG.put("manager", manager);
				
				// Writing default config to file
				getLogger().info("Writing config to file...");
				FileOutputStream fos = new FileOutputStream(cfg);
				fos.write(defaultCFG.toString(4).getBytes());
				config = defaultCFG;
				getLogger().info("Config written to file!");
				getLogger().info("Shutting down...");
				getProxy().stop("Config was missing, please fill out all variables and start the proxy again.");
			} else {
				// Loading already existing config
				Scanner sc = new Scanner(cfg);
				StringBuilder sb = new StringBuilder();
				while (sc.hasNextLine()) {
					sb.append(sc.nextLine());
				}
				sc.close();
				// Parsing config
				config = new JSONObject(sb.toString());
			}
			
			File pdata = new File("plugins/MTYT/playerdata.json");
			if(!pdata.exists()) {
				pdata.createNewFile();
				try (FileOutputStream fos = new FileOutputStream(pdata)) {
					JSONObject playerdata = new JSONObject();
					playerdata.put("players", new JSONArray());
					fos.write(playerdata.toString(4).getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Scanner sc = new Scanner(pdata);
				StringBuilder sb = new StringBuilder();
				while (sc.hasNextLine()) {
					sb.append(sc.nextLine());
				}
				sc.close();
				JSONObject playerdata = new JSONObject(sb.toString());
				playerdata.getJSONArray("players").forEach(p -> points.put(((JSONObject) p).getString("name"), ((JSONObject) p).getLong("points")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			server.start();
		}
		createServers();
	}
	
	@Override
	public void onDisable() {
		File cfg = new File("plugins/MTYT/playerdata.json");
		JSONArray playerdata = new JSONArray();
		points.forEach((name, points) -> {
			playerdata.put(new JSONObject().put("name", name).put("points", points));
		});
		try (FileOutputStream fos = new FileOutputStream(cfg, true)) {
			fos.write(playerdata.toString(4).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.interrupt();
	}
	
	public static void createServers() {
		SubAPI sub = SubAPI.getInstance();
		AtomicBoolean lobby = new AtomicBoolean(false);
		for (Map.Entry<String, Host> e : sub.getHosts().entrySet()) {
			Host h = e.getValue();
			for (Map.Entry<String, ? extends SubServer> entry : h.getSubServers().entrySet()) {
				SubServer subServer = entry.getValue();
				System.out.println(subServer.getName());
				switch (subServer.getName()) {
					case "lobby":
						lobby.set(true);
						break;
					default:
						break;
				}
			}
		}
		if(!lobby.get()) {
			createServer("Lobby");
		}
	}
	
	public static void createServer(String template) {
		TreeMap<Integer, Host> hostFullness = new TreeMap<>();
		SubAPI.getInstance().getHosts().forEach((s, h) -> hostFullness.put(h.getSubServers().size(), h));
		SubCreator.ServerTemplate tmplate = hostFullness.firstEntry().getValue().getCreator().getTemplate(template);
		int maxport = 0;
		for (Integer port : hostFullness.firstEntry().getValue().getCreator().getReservedPorts()) {
			if(port > maxport) {
				maxport = port;
			}
		}
		maxport++;
		hostFullness.firstEntry().getValue().getCreator().create(template.toLowerCase(), tmplate, Version.fromString("1.19.1"), maxport);
	}
}
