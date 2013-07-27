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
package org.spout.vanilla.component.entity.misc;

import java.util.HashMap;

import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.data.Difficulty;

/**
 * Component that contains the amount of damage this entity does.
 */
public class Damage extends VanillaEntityComponent {
	private final HashMap<Difficulty, org.spout.vanilla.data.Damage> damageList = new HashMap<Difficulty, org.spout.vanilla.data.Damage>();

	public Damage() {
		for (Difficulty difficulty : Difficulty.values()) {
			damageList.put(difficulty, new org.spout.vanilla.data.Damage());
		}
	}

	/**
	 * Get the damage level depending of the difficulty level.
	 * @param difficulty The difficulty level
	 * @return The {@link org.spout.vanilla.data.Damage} associated with the difficulty.
	 */
	public org.spout.vanilla.data.Damage getDamageLevel(Difficulty difficulty) {
		return damageList.get(difficulty);
	}
}
