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
package org.spout.vanilla.material.item.misc;

import org.spout.vanilla.material.item.VanillaItemMaterial;

public class Potion extends VanillaItemMaterial {
	
	/*
	 *  Potion effects
	 */
	private static final short EFFECT_MASK = 15;
	private static final short EFFECT_NONE = 0;
	private static final short EFFECT_REGENERATION = 1;
	private static final short EFFECT_SWIFTNESS = 2;
	private static final short EFFECT_FIRE_RESISTANCE = 3;
	private static final short EFFECT_POISON = 4;
	private static final short EFFECT_HEALING = 5;
	private static final short EFFECT_WEAKNESS = 8;
	private static final short EFFECT_STRENGTH = 9;
	private static final short EFFECT_SLOWNESS = 10;
	private static final short EFFECT_HARMING = 12;

	/*
	 * Potion tiers
	 */
	private static final short TIER_MASK = 48;
	private static final short TIER0 = 0;
	private static final short TIER1 = 16;
	private static final short TIER2 = 32;
	private static final short TIER3 = 48;
	
	/*
	 * Duration
	 */
	private static final short DURATION_MASK = 64;
	private static final short DURATION_NORMAL = 0;
	private static final short DURATION_EXTENDED = 64;
	
	/*
	 * Spash
	 */
	private static final short SPLASH_MASK = 16384;
	private static final short SPLASH_NONE = 0;
	private static final short SPLASH_SPLASH = 16384;
	
	public static final Potion EMPTY = new Potion("Empty Potion");
	public static final Potion AWKWARD = new Potion("Awkward Potion", EFFECT_NONE, TIER1, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	public static final Potion THICK = new Potion("Thick Potion", EFFECT_NONE, TIER2, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	public static final Potion POTENT = new Potion("Potent Potion", EFFECT_NONE, TIER3, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	
	public static final Potion MUNDANE_EXTENDED = new Potion("Mundane Potion (Extended)", EFFECT_NONE, TIER0, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	public static final Potion SPLASH_AWKWARD = new Potion("Splash Awkward Potion", 16384, EMPTY);
	public static final Potion MUNDANE = new Potion("Mundane Potion", 8192, EMPTY);
	
	public static final Potion REGENERATION = new Potion("Potion of Regeneration", EFFECT_REGENERATION, TIER0, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	public static final Potion REGENERATION_II = new Potion("Potion of Regeneration II", EFFECT_REGENERATION, TIER2, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	public static final Potion REGENERATION_EXTENDED = new Potion("Potion of Regeneration (Extended)", EFFECT_REGENERATION, TIER0, DURATION_EXTENDED, SPLASH_NONE, EMPTY);
	public static final Potion SPLASH_REGENERATION_EXTENDED = new Potion("Splash Potion of Regeneration (Extended)", EFFECT_REGENERATION, TIER0, DURATION_EXTENDED, SPLASH_SPLASH, EMPTY);
	public static final Potion SPLASH_REGENERATION = new Potion("Splash Potion of Regeneration", EFFECT_REGENERATION, TIER0, DURATION_NORMAL, SPLASH_SPLASH, EMPTY);
	public static final Potion SPLASH_REGENERATION_II = new Potion("Splash Potion of Regeneration II", EFFECT_REGENERATION, TIER2, DURATION_NORMAL, SPLASH_SPLASH, EMPTY);

	public static final Potion SWIFTNESS = new Potion("Potion of Swiftness", EFFECT_SWIFTNESS, TIER0, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	public static final Potion SWIFTNESS_EXTENDED = new Potion("Potion of Swiftness (Extended)", EFFECT_SWIFTNESS, TIER0, DURATION_EXTENDED, SPLASH_NONE, EMPTY);
	public static final Potion SPLASH_SWIFTNESS_EXTENDED = new Potion("Splash Potion of Swiftness (Extended)", EFFECT_SWIFTNESS, TIER0, DURATION_EXTENDED, SPLASH_SPLASH, EMPTY);
	public static final Potion SPLASH_SWIFTNESS = new Potion("Splash Potion of Swiftness", EFFECT_SWIFTNESS, TIER0, DURATION_NORMAL, SPLASH_SPLASH, EMPTY);
	public static final Potion SWIFTNESS_II = new Potion("Potion of Swiftness II", EFFECT_SWIFTNESS, TIER2, DURATION_EXTENDED, SPLASH_NONE, EMPTY);
	public static final Potion SPLASH_SWIFTNESS_II = new Potion("Splash Potion of Swiftness II", EFFECT_SWIFTNESS, TIER2, DURATION_EXTENDED, SPLASH_SPLASH, EMPTY);
	
	public static final Potion FIRE_EXTENDED = new Potion("Potion of Fire Resistance (Extended)", 8194, EMPTY);
	public static final Potion SPLASH_FIRE_EXTENDED = new Potion("Splash Potion of Fire Resistance (Extended)", 16415, EMPTY);
	public static final Potion FIRE = new Potion("Potion of Fire Resistance", 8258, EMPTY);
	public static final Potion SPLASH_FIRE = new Potion("Splash Potion of Fire Resistance", 16387, EMPTY);
	public static final Potion FIRE_II = new Potion("Potion of Fire Resistance II", EFFECT_FIRE_RESISTANCE, TIER2, DURATION_NORMAL, SPLASH_NONE, EMPTY);
	public static final Potion FIRE_REVERTED = new Potion("Potion of Fire Resistance", 8227, EMPTY);
	public static final Potion SPLASH_FIRE_REVERTED = new Potion("Splash Potion of Fire Resistance", 16419, EMPTY);
	public static final Potion HEALING = new Potion("Potion of Healing", 8197, EMPTY);
	public static final Potion SPLASH_HEALING = new Potion("Splash Potion of Healing", 16389, EMPTY);
	public static final Potion HEALING_REVERTED = new Potion("Potion of Healing", 8261, EMPTY);
	public static final Potion SPLASH_HEALING_REVERTED = new Potion("Splash Potion of Healing", 16453, EMPTY);
	public static final Potion HEALING_II = new Potion("Potion of Healing II", 8229, EMPTY);
	public static final Potion SPLASH_HEALING_II = new Potion("Splash Potion of Healing II", 16421, EMPTY);
	public static final Potion STRENGTH = new Potion("Potion of Strength", 8201, EMPTY);
	public static final Potion SPLASH_STRENGTH = new Potion("Splash Potion of Strength", 16393, EMPTY);
	public static final Potion STRENGTH_EXTENDED = new Potion("Potion of Strength (Extended)", 8265, EMPTY);
	public static final Potion SPLASH_STRENGTH_EXTENDED = new Potion("Spash Potion of Strength (Extended)", 16457, EMPTY);
	public static final Potion SLOWNESS_EXTENDED = new Potion("Potion of Slowness (Extended)", 8226, EMPTY);
	public static final Potion SPLASH_SLOWNESS_EXTENDED = new Potion("Splash Potion of Slowness (Extended)", 16458, EMPTY);
	public static final Potion SLOWNESS_REVERTED = new Potion("Potion of Slowness", 8234, EMPTY);
	public static final Potion SPLASH_SLOWNESS_REVERTED = new Potion("Splash Potion of Slowness", 16426, EMPTY);
	public static final Potion HARMING = new Potion("Potion of Harming", 8202, EMPTY);
	public static final Potion SPLASH_HARMING = new Potion("Splash Potion of Harming", 16396, EMPTY);
	public static final Potion HARMING_REVERTED = new Potion("Potion of Harming", 8264, EMPTY);
	public static final Potion SPLASH_HARMING_REVERTED = new Potion("Splash Potion of Harming", 16460, EMPTY);
	public static final Potion HARMING_II = new Potion("Potion of Harming II", 8236, EMPTY);
	public static final Potion SPLASH_HARMING_II = new Potion("Splash Potion of Harming", 16428, EMPTY);

	private Potion(String name) {
		super(name, 373);
	}

	private Potion(String name, int data, Potion parent) {
		super(name, 373, data, parent);
	}
	

	private Potion(String name, short effect, short tier, short duration, short splash, Potion parent) {
		super(name, 373, (effect & EFFECT_MASK) | (tier & TIER_MASK) | (duration & DURATION_MASK) | (splash & SPLASH_MASK), parent);
	}

	@Override
	public Potion getParentMaterial() {
		return (Potion) super.getParentMaterial();
	}
}
