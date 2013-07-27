/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.material.block.solid;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.data.drops.flag.ToolTypeFlags;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.component.FurnaceBlock;

public class NetherRack extends Solid implements TimedCraftable {
	public NetherRack(String name, int id) {
		super(name, id, VanillaMaterialModels.NETHER_RACK);
		this.setHardness(0.6F).setResistance(0.6F);
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.PICKAXE);
	}

	@Override
	public ItemStack getResult() {
		return new ItemStack(VanillaMaterials.NETHER_BRICK, 1);
	}

	@Override
	public float getCraftTime() {
		return FurnaceBlock.SMELT_TIME;
	}
}
