/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 29, 2015, 4:44:49 PM (GMT)]
 */
package vazkii.botania.client.challenge;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import vazkii.botania.common.item.ModItems;
import net.minecraft.item.ItemStack;

public final class ModChallenges {

	public static final EnumMap<EnumChallengeLevel, List<Challenge>> challenges = new EnumMap(EnumChallengeLevel.class);
	
	public static void init() {
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants())
			challenges.put(level, new ArrayList());
		
		ItemStack icon = new ItemStack(ModItems.lexicon); // TODO
		addChallenge(EnumChallengeLevel.EASY, "flowerFarm", icon);
		addChallenge(EnumChallengeLevel.EASY, "recordFarm", icon);
		addChallenge(EnumChallengeLevel.EASY, "reedFarm", icon);
		addChallenge(EnumChallengeLevel.EASY, "cobbleGen", icon);
		addChallenge(EnumChallengeLevel.EASY, "pureDaisy", icon);
		addChallenge(EnumChallengeLevel.EASY, "orechid", icon);
		addChallenge(EnumChallengeLevel.EASY, "battery", icon);
		
		addChallenge(EnumChallengeLevel.NORMAL, "apothecaryRefill", icon);
		addChallenge(EnumChallengeLevel.NORMAL, "treeFarm", icon);
		addChallenge(EnumChallengeLevel.NORMAL, "fullCropFarm", icon);
		addChallenge(EnumChallengeLevel.NORMAL, "animalFarm", icon);
		addChallenge(EnumChallengeLevel.NORMAL, "boneMealFarm", icon);
		
		addChallenge(EnumChallengeLevel.HARD, "mobTower", icon);
		addChallenge(EnumChallengeLevel.HARD, "entropynniumSetup", icon);
		addChallenge(EnumChallengeLevel.HARD, "spectrolusSetup", icon);
		addChallenge(EnumChallengeLevel.HARD, "potionBrewer", icon);

		addChallenge(EnumChallengeLevel.LUNATIC, "autoQuarry", icon);
		addChallenge(EnumChallengeLevel.LUNATIC, "runeCrafter", icon);
		addChallenge(EnumChallengeLevel.LUNATIC, "kekimurusSetup", icon);
	}
	
	public static void addChallenge(EnumChallengeLevel level, String name, ItemStack icon) {
		Challenge c = new Challenge("botania.challenge." + name, icon, level);
		challenges.get(level).add(c);
	}
	
}
