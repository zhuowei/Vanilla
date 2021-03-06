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
package org.spout.vanilla.material.block.solid;

import java.util.ArrayList;
import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.block.MovingBlock;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.tool.Spade;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.Instrument;

public class Gravel extends Solid implements DynamicMaterial, Mineable{
	private Random rand = new Random();

	public Gravel(String name, int id) {
		super(name, id);
		this.setHardness(0.6F).setResistance(1.0F);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material.equals(VanillaMaterials.FIRE)) {
			return face == BlockFace.TOP;
		} else {
			return false;
		}
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Spade ? (short) 1 : (short) 2;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.SNAREDRUM;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (rand.nextInt(10) == 0) {
			drops.add(new ItemStack(VanillaMaterials.FLINT, 1));
		} else {
			drops.add(new ItemStack(this, 1));
		}
		return drops;
	}

	@Override
	public Vector3[] maxRange() {
		return new Vector3[0];
	}

	@Override
	public long onPlacement(Block b, Region r, long currentTime) {
		return currentTime;
	}

	@Override
	public long update(Block b, Region r, long updateTime, long lastUpdateTime, Object hint) {
		if (!b.translate(0, -1, 0).getMaterial().isSolid()) {
			b.setMaterial(VanillaMaterials.AIR);
			b.getWorld().createAndSpawnEntity(b.getPosition(), new MovingBlock(VanillaControllerTypes.MOVING_BLOCK, this));
		}
		return b.getWorld().getAge() + 1;
	}
}
