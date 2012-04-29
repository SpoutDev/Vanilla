/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.protocol.handler;

import org.spout.api.event.EventManager;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.generic.VanillaBlockMaterial;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;

public final class PlayerDiggingMessageHandler extends MessageHandler<PlayerDiggingMessage> {
	@Override
	public void handleServer(Session session, Player player, PlayerDiggingMessage message) {
		if (player == null) {
			return;
		}

		boolean blockBroken = false;

		EventManager eventManager = player.getSession().getGame().getEventManager();
		World world = player.getEntity().getWorld();

		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();

		org.spout.api.geo.cuboid.Block block = world.getBlock(x, y, z);

		//TODO Need to have some sort of verification to deal with malicious clients.
		if (message.getState() == PlayerDiggingMessage.STATE_START_DIGGING) {
			boolean isAir = false;
			if (block == null || block.getMaterial() == VanillaMaterials.AIR) {
				isAir = true;
			}
			PlayerInteractEvent interactEvent = new PlayerInteractEvent(player, new Point(world, x, y, z), player.getEntity().getInventory().getCurrentItem(), PlayerInteractEvent.Action.LEFT_CLICK, isAir);
			eventManager.callEvent(interactEvent);
			if (interactEvent.isCancelled() || isAir) {
				return;
			}
			if (player.getEntity().getController() instanceof VanillaPlayer && !((VanillaPlayer) player.getEntity().getController()).isSurvival()) {
				blockBroken = true;
			}
		} else if (message.getState() == PlayerDiggingMessage.STATE_DONE_DIGGING) {
			//TODO: Timing checks!
			blockBroken = true;
		}

		BlockMaterial material = block.getMaterial();
		if (material == VanillaMaterials.WATER || material == VanillaMaterials.LAVA) {
			blockBroken = false; //deny digging for water or lava
		} else if (material.getHardness() == 0.0f) {
			blockBroken = true; //insta-break
		}

		if (blockBroken) {
			BlockMaterial oldMat = world.getBlockMaterial(x, y, z);
			world.setBlockMaterial(x, y, z, VanillaMaterials.AIR, (short) 0, true, player);
			oldMat.onDestroy(world, x, y, z);

			if (player.getEntity().getController() instanceof VanillaPlayer && ((VanillaPlayer) player.getEntity().getController()).isSurvival()) {
				Material dropMat = oldMat;
				int count = 1;
				if (oldMat instanceof VanillaBlockMaterial) {
					VanillaBlockMaterial blockMat = (VanillaBlockMaterial) oldMat;
					dropMat = Material.get(blockMat.getDrop().getId());
					count = blockMat.getDropCount();
				}

				if (dropMat != null) {
					for (int i = 0; i < count && dropMat.getId() != 0; ++i) {
						world.createAndSpawnEntity(block.getPosition(), new Item(new ItemStack(dropMat, 1), player.getEntity().getPosition().normalize().add(0, 5, 0)));
					}
				}
			}
		}
	}
}
