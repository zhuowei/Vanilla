/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.command;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.ChatColor;
import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.BlockController;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.entity.PlayerController;
import org.spout.api.entity.type.ControllerRegistry;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.Session;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.util.HighlyInformativeMessages;
import org.spout.vanilla.util.explosion.ExplosionModels;

public class ClientCommands {
	private final Set<String> invisible = new HashSet<String>();
	@SuppressWarnings("unused")
	private final VanillaPlugin plugin;

	public ClientCommands(VanillaPlugin instance) {
		plugin = instance;
	}

	@Command(aliases = {"connect"}, usage = "<ip> [port]", desc = "Connect to a server", min = 1, max = 2)
	public void connect(CommandContext args, CommandSource source) throws CommandException {
		Client client = (Client) Spout.getEngine();
		//Player player = client.getActivePlayer();
		InetSocketAddress address = new InetSocketAddress(args.getString(0), args.getInteger(1, 25565));
		/*String playerName = args.getString(2, "Player" + System.currentTimeMillis() % 1000);
		String hash = args.getString(3, "-");*/
		client.connect(address);
		source.sendMessage("Connecting to server " + address.toString());
		source.sendMessage(HighlyInformativeMessages.getRandomMessage());
	}

	@Command(aliases = {"disconect"}, usage = "<disconnect>", desc = "Quit from a server")
	public void disconnect(CommandContext args, CommandSource source) throws CommandException {
		source.sendMessage("Coming soon! In the meantime, click the Close button.");
	}

	@Command(aliases = {"say"}, usage = "[message]", desc = "Say something!", min = 1, max = -1)
	public void say(CommandContext args, CommandSource source) throws CommandException {
		String message = args.getJoinedString(0);
		if (!message.isEmpty()) {
			Session session = ((Client) Spout.getEngine()).getPlayer("Player", false).getSession();
			session.send(session.getPlayerProtocol().getChatMessage(message), true);
		}
	}

}
