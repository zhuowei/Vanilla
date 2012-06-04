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
package org.spout.vanilla.util;

/** 
 * A class that contains Highly Informative Messages<sup>TM</sup>. 
 * The messages are originally from the Spoutcraft project, and are composed of whimsical nonsense and geeky references.
 * They are intended to be displayed while connecting to a server.
 */

public final class HighlyInformativeMessages {

	private HighlyInformativeMessages(){}

	/** the list of messages. */

	public static final String[] messages = {
			"Adding Hidden Agendas",
			"Adjusting Bell Curves",
			"Aesthesizing Industrial Areas",
			"Aligning Covariance Matrices",
			"Applying Feng Shui Shaders",
			"Applying Theatre Soda Layer",
			"Asserting Packed Exemplars",
			"Attempting to Lock Back-Buffer",
			"Binding Sapling Root System",
			"Breeding Fauna",
			"Building Data Trees",
			"Bureacritizing Bureaucracies",
			"Calculating Inverse Probability Matrices",
			"Calculating Llama Expectoration Trajectory",
			"Calibrating Blue Skies",
			"Charging Ozone Layer",
			"Coalescing Cloud Formations",
			"Cohorting Exemplars",
			"Collecting Meteor Particles",
			"Compounding Inert Tessellations",
			"Compressing Fish Files",
			"Computing Optimal Bin Packing",
			"Concatenating Sub-Contractors",
			"Containing Existential Buffer",
			"Dialing Mother",
			"Debarking Ark Ramp",
			"Debunching Unionized Commercial Services",
			"Deciding What Message to Display Next",
			"Decomposing Singular Values",
			"Decrementing Tectonic Plates",
			"Deleting Ferry Routes",
			"Depixelating Inner Mountain Surface Back Faces",
			"Depositing Slush Funds",
			"Destabilizing Economic Indicators",
			"Determining Width of Blast Fronts",
			"Deunionizing Bulldozers",
			"Dicing Models",
			"Diluting Livestock Nutrition Variables",
			"Doing A Barrel Roll",
			"Downloading Satellite Terrain Data",
			"Doubting The Spoon",
			"Exposing Flash Variables to Streak System",
			"Extracting Resources",
			"Factoring Pay Scale",
			"Fixing Election Outcome Matrix",
			"Flood-Filling Ground Water",
			"Flushing Pipe Network",
			"Gathering Particle Sources",
			"Generating Jobs",
			"Gesticulating Mimes",
			"Graphing Whale Migration",
			"Hiding Willio Webnet Mask",
			"Hiring Consultant",
			"Implementing Impeachment Routine",
			"Increasing Accuracy of Memory Leaks",
			"Increasing Magmafacation",
			"Initializing Tracking Mechanism",
			"Initializing Breeding Timetable",
			"Initializing Robotic Click-Path AI",
			"Inserting Sublimated Messages",
			"Integrating Curves",
			"Integrating Illumination Form Factors",
			"Integrating Population Graphs",
			"Iterating Cellular Automata",
			"Lecturing Errant Subsystems",
			"Losing The Game",
			"Mixing Genetic Pool",
			"Modeling Object Components",
			"Mopping Occupant Leaks",
			"Normalizing Power",
			"Obfuscating Quigley Matrix",
			"Perturbing Matrices",
			"Pixalating Nude Patch",
			"Polishing Water Highlights",
			"Populating Block Templates",
			"Preparing Sprites for Random Walks",
			"Prioritizing Slimes",
			"Projecting Law Enforcement Pastry Intake",
			"Promising Cake",
			"Realigning Alternate Time Frames",
			"Reconfiguring User Mental Processes",
			"Relaxing Splines",
			"Removing Texture Gradients",
			"Restoring World From Backups",
			"Resolving GUID Conflict",
			"Reticulating Splines",
			"Retracting Phong Shader",
			"Retrieving from Back Store",
			"Reverse Engineering Image Consultant",
			"Routing Neural Network Infanstructure",
			"Scattering Rhino Food Sources",
			"Scrubbing Terrain",
			"Searching for Llamas",
			"Seeding Architecture Simulation Parameters",
			"Sequencing Particles",
			"Setting Advisor Moods",
			"Setting Inner Deity Indicators",
			"Setting Universal Physical Constants",
			"Sonically Enhancing Occupant-Free Timber",
			"Speculating Stock Market Indices",
			"Splatting Transforms",
			"Stratifying Ground Layers",
			"Sub-Sampling Water Data",
			"Synthesizing Gravity", 
			"Synthesizing Wavelets",
			"Time-Compressing Simulator Clock",
			"Unable to Reveal Current Activity",
			"Wanting To Believe",
			"Weathering Landforms",
			"Zeroing Creeper Network",
			"Teaching Wibbly Wobbly Timey Wimey to humans"
	};

	/**
	 * returns a random Highly Informative Message<sup>TM</sup>
	 * @returns a random message from the list
	 */

	public static String getRandomMessage() {
		return messages[(new java.util.Random()).nextInt(messages.length)];
	}
}
