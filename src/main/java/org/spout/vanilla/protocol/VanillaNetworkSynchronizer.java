/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.protocol;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ChunkSnapshot;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.protocol.Session.State;
import org.spout.api.protocol.event.ProtocolEventListener;
import org.spout.api.util.map.TIntPairObjectHashMap;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.LoadChunkMessage;
import org.spout.vanilla.protocol.msg.LoginRequestMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotsMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.world.generator.VanillaBiome;

import static org.spout.vanilla.material.VanillaMaterials.getMinecraftId;

public class VanillaNetworkSynchronizer extends NetworkSynchronizer implements ProtocolEventListener {
	@SuppressWarnings("unused")
	private final static int POSITION_UPDATE_TICKS = 20;
	private final static double STANCE = 1.6D;
	private final static int TIMEOUT = 15000;
	private TIntPairObjectHashMap<TIntHashSet> activeChunks = new TIntPairObjectHashMap<TIntHashSet>();
	private final TIntObjectHashMap<Message> queuedInventoryUpdates = new TIntObjectHashMap<Message>();
	private static final byte[] SOLID_CHUNK_DATA = new byte[Chunk.CHUNK_VOLUME * 5 /2];
	private static final byte[] AIR_CHUNK_DATA = new byte[Chunk.CHUNK_VOLUME * 5 /2];
	private boolean first = true;
	private long lastKeepAlive = System.currentTimeMillis();

	static {
		int i = 0;
		for (int c = 0; c < Chunk.CHUNK_VOLUME; c++) { // blocks
			SOLID_CHUNK_DATA[i] = 1;
			AIR_CHUNK_DATA[i++] = 0;
		}

		for (int c = 0; c < Chunk.CHUNK_VOLUME / 2; c++) { // block data
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = 0x00;
		}

		for (int c = 0; c < Chunk.CHUNK_VOLUME / 2; c++) { // block light
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = 0x00;
		}

		for (int c = 0; c < Chunk.CHUNK_VOLUME / 2; c++) { // sky light
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = (byte) 0xFF;
		}
	}

	public VanillaNetworkSynchronizer(Player player, Entity entity) {
		super(player, entity);
		registerProtocolEvents(this);
		initChunk(player.getEntity().getPosition());
	}

	@Override
	protected void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.CHUNK_SIZE_BITS;
		int y = (int) p.getY() >> Chunk.CHUNK_SIZE_BITS; // + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.CHUNK_SIZE_BITS;

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.CHUNK_SIZE_BITS) {
			return;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
				activeChunks.remove(x, z);
				LoadChunkMessage unLoadChunk = new LoadChunkMessage(x, z, false);
				owner.getSession().send(unLoadChunk);
			} else {
				byte[][] data = new byte[16][];
				data[y] = AIR_CHUNK_DATA;
				CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, false, new boolean[16], 0, data, null);
				session.send(CCMsg);
			}
		}
	}

	@Override
	protected void initChunk(Point p) {
		int x = (int) p.getX() >> Chunk.CHUNK_SIZE_BITS;
		int y = (int) p.getY() >> Chunk.CHUNK_SIZE_BITS;// + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.CHUNK_SIZE_BITS;

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.CHUNK_SIZE_BITS) {
			return;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column == null) {
			int[][] heights = new int[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];

			World w = p.getWorld();

			for (int xx = 0; xx < Chunk.CHUNK_SIZE; xx++) {
				for (int zz = 0; zz < Chunk.CHUNK_SIZE; zz++) {
					heights[xx][zz] = w.getSurfaceHeight(p.getBlockX() + xx, p.getBlockZ() + zz, true);
				}
			}

			byte[][] packetChunkData = new byte[16][Chunk.CHUNK_VOLUME * 5 / 2];

			for (int xx = 0; xx < Chunk.CHUNK_SIZE; xx++) {
				for (int zz = 0; zz < Chunk.CHUNK_SIZE; zz++) {
					for (int yy = 0; yy < Chunk.CHUNK_SIZE * 16; yy++) {
						int cube = yy >> Chunk.CHUNK_SIZE_BITS;
						int yOffset = yy & 0xF;
						int dataOffset = xx | (yOffset << 8) | (zz << 4);
						if (heights[xx][zz] < yy && yy > 0) {
							byte mask = (dataOffset & 0x1) == 0 ? (byte) 0x0F : (byte) 0xF0;
							packetChunkData[cube][(Chunk.CHUNK_VOLUME << 1) + (dataOffset >> 1)] |= mask;
						} else {
							packetChunkData[cube][dataOffset] = 1;
						}
					}
				}
			}

			column = new TIntHashSet();
			activeChunks.put(x, z, column);
			LoadChunkMessage loadChunk = new LoadChunkMessage(x, z, true);
			owner.getSession().send(loadChunk);

			Chunk chunk = p.getWorld().getChunkFromBlock(p);
			byte[] biomeData = new byte[Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE];
			final int chunkMask = (Chunk.CHUNK_SIZE - 1);
			for (int dx = x; dx < x + Chunk.CHUNK_SIZE; ++dx) {
				for (int dz = z; dz < z + Chunk.CHUNK_SIZE; ++dz) {
					Biome biome = chunk.getBiomeType(dx & chunkMask, 0, dz & chunkMask);
					if (biome instanceof VanillaBiome) {
						biomeData[(dz & chunkMask) << 4 | (dx & chunkMask)] = (byte) ((VanillaBiome) biome).getBiomeId();
					}
				}
			}

			CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, true, new boolean[16], 0, packetChunkData, biomeData);
			owner.getSession().send(CCMsg);
		}
		column.add(y);
	}

	@Override
	public void sendChunk(Chunk c) {
		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();

		if (y < 0 || y >= c.getWorld().getHeight() >> Chunk.CHUNK_SIZE_BITS) {
			return;
		}

		ChunkSnapshot snapshot = c.getSnapshot(false);
		short[] rawBlockIdArray = snapshot.getBlockIds();
		short[] rawBlockData = snapshot.getBlockData();
		byte[] rawBlockLight = snapshot.getBlockLight();
		byte[] rawSkyLight = snapshot.getSkyLight();
		byte[] fullChunkData = new byte[Chunk.CHUNK_VOLUME * 5 / 2];

		boolean hasData = false;
		int arrIndex = 0;
		for (int i = 0; i < rawBlockIdArray.length; i++) {
			short convert = getMinecraftId(rawBlockIdArray[i]);
			fullChunkData[arrIndex++] = (byte) (convert & 0xFF);
			if ((rawBlockIdArray[i] & 0xFF) != 0) {
				hasData = true;
			}
		}
		if (!hasData) {
			return;
		}

		for (int i = 0; i < rawBlockData.length; i += 2) {
			fullChunkData[arrIndex++] = (byte) ((rawBlockData[i + 1] << 4) | (rawBlockData[i] & 0xF));
		}

		System.arraycopy(rawBlockLight, 0, fullChunkData, arrIndex, rawBlockLight.length);
		arrIndex += rawBlockLight.length;

		System.arraycopy(rawSkyLight, 0, fullChunkData, arrIndex, rawSkyLight.length);

		arrIndex += rawSkyLight.length;

		byte[][] packetChunkData = new byte[16][];
		packetChunkData[y] = fullChunkData;
		CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, false, new boolean[16], 0, packetChunkData, null);
		owner.getSession().send(CCMsg);
	}

	@Override
	protected void sendPosition(Point p, Quaternion rot) {
		//TODO: Implement Spout Protocol
		PlayerPositionLookMessage PRMsg = new PlayerPositionLookMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), rot.getYaw(), rot.getPitch(), true);
		owner.getSession().send(PRMsg);
	}

	@Override
	protected void worldChanged(World world) {
		//Grab world characteristics.
		Byte gamemode = (Byte) world.getDataMap().get("gamemode", 2);
		Byte worldDifficulty = (Byte) world.getDataMap().get("difficulty", 1); //Normal difficulty
		Integer dimensionBit = (Integer) world.getDataMap().get("dimensionbit", 1); //Normal sky
		String worldType = (String) world.getDataMap().get("type", "DEFAULT");
		Transform spawn = (Transform) world.getDataMap().get("spawnpoint", new Transform(new Point(world, 1, 80, 1), Quaternion.IDENTITY, Vector3.ONE));

		//TODO Handle infinite height
		if (first) {
			first = false;
			int entityId = owner.getEntity().getId();
			VanillaPlayer vc = (VanillaPlayer) owner.getEntity().getController();
			LoginRequestMessage idMsg = new LoginRequestMessage(entityId, owner.getName(), gamemode, dimensionBit, worldDifficulty, 256, session.getGame().getMaxPlayers(), worldType);
			owner.getSession().send(idMsg, true);
			owner.getSession().setState(State.GAME);
			for (int slot = 0; slot < 4; slot++) {
				ItemStack slotItem = vc.getInventory().getArmor().getItem(slot);
				EntityEquipmentMessage EEMsg;
				if (slotItem == null) {
					EEMsg = new EntityEquipmentMessage(entityId, slot, -1, 0);
				} else {
					EEMsg = new EntityEquipmentMessage(entityId, slot, getMinecraftId(slotItem.getMaterial().getId()), slotItem.getData());
				}
				owner.getSession().send(EEMsg);
			}
		} else {
			owner.getSession().send(new RespawnMessage(dimensionBit,  worldDifficulty, gamemode, 256, worldType));
		}

		if (world != null) {
			Point pos = spawn.getPosition();
			SpawnPositionMessage SPMsg = new SpawnPositionMessage((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
			owner.getSession().send(SPMsg);
		}
	}

	@Override
	public void preSnapshot() {
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastKeepAlive + TIMEOUT) {
			KeepAliveMessage PingMsg = new KeepAliveMessage((int) currentTime);
			lastKeepAlive = currentTime;
			owner.getSession().send(PingMsg, true);
		}

		for (TIntObjectIterator<Message> i = queuedInventoryUpdates.iterator(); i.hasNext(); ) {
			i.advance();
			session.send(i.value());
		}
		super.preSnapshot();
	}

	@Override
	public void updateBlock(Chunk chunk, int x, int y, int z, BlockMaterial material, short data) {
		int id = getMinecraftId(material);
		if ((data & 0xF) > 15) {
			data = 0;
		}
		x += chunk.getBlockX();
		y += chunk.getBlockY();
		z += chunk.getBlockZ();
		if (y >= 0 && y < chunk.getWorld().getHeight()) {
			BlockChangeMessage BCM = new BlockChangeMessage(x, y, z, id & 0xFF, data & 0xF);
			session.send(BCM);
		}
	}

	@Override
	public void spawnEntity(Entity e) {
		if (e == null) {
			return;
		}

		Controller c = e.getController();
		if (c != null) {
			EntityProtocol ep = c.getType().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
			if (ep != null) {
				Message[] spawn = ep.getSpawnMessage(e);
				if (spawn != null) {
					session.sendAll(spawn);
				}
			}
		}
		super.spawnEntity(e);
	}

	@Override
	public void destroyEntity(Entity e) {
		if (e == null) {
			return;
		}

		Controller c = e.getController();
		if (c != null) {
			EntityProtocol ep = c.getType().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
			if (ep != null) {
				Message[] death = ep.getDestroyMessage(e);
				if (death != null) {
					session.sendAll(death);
				}
			}
		}
		super.destroyEntity(e);
	}

	@Override
	public void syncEntity(Entity e) {
		if (e == null) {
			return;
		}

		Controller c = e.getController();
		if (c != null) {
			EntityProtocol ep = c.getType().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
			if (ep != null) {
				Message[] sync = ep.getUpdateMessage(e);
				if (sync != null) {
					session.sendAll(sync);
				}
			}
		}
		super.syncEntity(e);
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		Controller c = owner.getEntity().getController();
		if (!(c instanceof VanillaPlayer)) {
			return;
		}
		VanillaPlayer controller = (VanillaPlayer) c;
		slot = controller.getActiveWindow().getNativeSlotIndex(slot);
		if (slot == -1) {
			return;
		}

		Message message;
		int id = controller.getActiveWindow().getInstanceId();
		if (item == null) {
			message = new SetWindowSlotMessage(id, slot);
		} else {
			message = new SetWindowSlotMessage(id, slot, getMinecraftId(item.getMaterial().getId()), item.getAmount(), item.getData(), item.getNBTData());
		}
		queuedInventoryUpdates.put(slot, message);
	}

	@Override
	public void updateAll(InventoryBase inventory, ItemStack[] slots) {
		Controller c = owner.getEntity().getController();
		if (!(c instanceof VanillaPlayer)) {
			return;
		}

		VanillaPlayer controller = (VanillaPlayer) c;
		ItemStack[] newSlots = slots.clone();
		for (int i = 0; i < slots.length; i++) {
			newSlots[controller.getActiveWindow().getNativeSlotIndex(i)] = slots[i];
		}

		int id = controller.getActiveWindow().getInstanceId();
		session.send(new SetWindowSlotsMessage((byte) id, newSlots));
		queuedInventoryUpdates.clear();
	}
}
