package me.itoncek.mtyt.manager;

import express.Express;
import org.json.JSONObject;

import static me.itoncek.mtyt.manager.Manager.config;

/*
 * Copyright (c) 2022 IToncek
 *
 * All rights reserved to original developer [IToncek].
 */

public class ServerRunnable implements Runnable{
	@Override
	public void run() {
		Express server = new Express();
		
		//Ping
		server.get("/", (request, response) -> {
			response.send(String.valueOf(System.currentTimeMillis()));
		});
		
		server.get("/addpoints/:mcName/:amount", (request, response) -> {
			config.getJSONArray("participants").forEach(participant -> {
				if (((JSONObject) participant).getString("name").equals(request.getParam("mcName"))) {
					((JSONObject) participant).put("points", ((JSONObject) participant).getInt("points") + Integer.parseInt(request.getParam("amount")));
				}
			});
			response.send("ok");
		});
		
		server.listen(config.getJSONObject("manager").getInt("port"));
	}
}
