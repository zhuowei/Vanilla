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
package org.spout.vanilla.world.generator.normal.biome;

import net.royawesome.jlibnoise.module.modifier.ScalePoint;

import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;

public class TundraBiome extends VanillaNormalBiome {

	private final static ScalePoint NOISE = new ScalePoint();

	static {
		NOISE.SetSourceModule(0, VanillaNormalBiome.MASTER);
		NOISE.setxScale(0.09D);
		NOISE.setyScale(0.08D);
		NOISE.setzScale(0.09D);
	}

	public TundraBiome(int id) {
		super(id, NOISE/*, new PondDecorator()*/);
		this.minDensityTerrainHeight = 67;
		this.maxDensityTerrainHeight = 69;
		this.upperHeightMapScale = 3.3f;
		this.bottomHeightMapScale = 3.7f;
	}

	@Override
	public String getName() {
		return "Tundra";
	}

	@Override
	protected void replaceBlocks(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		super.replaceBlocks(blockData, x, chunkY, z);
		final byte size = (byte) blockData.getSize().getY();
		final int startY = chunkY * 16;
		final int endY = startY + size;
		if (blockData.get(x, startY, z) == VanillaMaterials.AIR.getId()) {
			if (getSample(blockData.getWorld(), x, startY - 1, startY, z).get(x, startY - 1, z)
					!= VanillaMaterials.AIR.getId()) {
				blockData.set(x, startY, z, VanillaMaterials.SNOW.getId());
			}
		}
		boolean hasSurface = false;
		for (int y = startY; y < endY; y++) {
			short id = blockData.get(x, y, z);
			if (id == VanillaMaterials.GRASS.getId()) {
				hasSurface = true;
			} else if (id == VanillaMaterials.AIR.getId() && hasSurface) {
				blockData.set(x, y, z, VanillaMaterials.SNOW.getId());
				hasSurface = false;
			}
		}
		if (chunkY * 16 <= SEA_LEVEL && (chunkY + 1) * 16 > SEA_LEVEL
				&& blockData.get(x, SEA_LEVEL, z) == VanillaMaterials.STATIONARY_WATER.getId()) {
			blockData.set(x, SEA_LEVEL, z, VanillaMaterials.ICE.getId());
		}
	}
}
