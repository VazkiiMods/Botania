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

public enum EnumChallengeLevel {

	EASY("botania.challengelevel.easy"),
	NORMAL("botania.challengelevel.normal"),
	HARD("botania.challengelevel.hard"),
	LUNATIC("botania.challengelevel.lunatic");

	final String name;

	private EnumChallengeLevel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
