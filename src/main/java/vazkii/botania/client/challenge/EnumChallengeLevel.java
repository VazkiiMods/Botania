/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 29, 2015, 4:46:16 PM (GMT)]
 */
package vazkii.botania.client.challenge;

/**
 * An enumeration representing the possible difficulty levels of a challenge.
 */
public enum EnumChallengeLevel {

	/**
	 * An "easy" challenge - something with a few moving parts which automates a mundane task.
	 */
	EASY("botania.challengelevel.easy"),

	/**
	 * A normal challenge for sane people who like to automate things.
	 */
	NORMAL("botania.challengelevel.normal"),

	/**
	 * A difficult challenge which requires complex interaction between flowers, blocks,
	 * and game mechanics.
	 */
	HARD("botania.challengelevel.hard"),

	/**
	 * A challenge for those with too much time and too little sanity.
	 */
	LUNATIC("botania.challengelevel.lunatic");

	/**
	 * The name of the challenge.
	 */
	private String name;

	/**
	 * Constructs a new challenge level with the provided, unlocalized name.
	 * @param name The unlocalized name of the challenge level.
	 */
	private EnumChallengeLevel(String name) {
		this.name = name;
	}

	/**
	 * Returns the unlocalized name of the challenge level.
	 * @return The unlocalized name of the challenge level.
	 */
	public String getName() {
		return name;
	}

}
