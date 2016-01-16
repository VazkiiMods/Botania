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
import java.util.HashMap;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

public final class ModChallenges {

	/**
	 * A map of challenge levels to a list of the challenges of that level.
	 */
	public static final EnumMap<EnumChallengeLevel, List<Challenge>> challenges =
			new EnumMap<EnumChallengeLevel, List<Challenge>>(EnumChallengeLevel.class);

	/**
	 * A map of challenge names to their corresponding challenge objects.
	 */
	public static final HashMap<String, Challenge> challengeLookup = new HashMap<String, Challenge>();

	/**
	 * Initializes the default challenges in Botania, adding them to the lookup tables.
	 */
	public static void init() {
		// Fill the challenge map with empty lists
		for(EnumChallengeLevel level : EnumChallengeLevel.values())
			challenges.put(level, new ArrayList<Challenge>());

		// Easy Challenges
		addChallenge(EnumChallengeLevel.EASY, "flowerFarm", new ItemStack(ModBlocks.flower, 1, 6));
		addChallenge(EnumChallengeLevel.EASY, "recordFarm", new ItemStack(Items.record_13));
		addChallenge(EnumChallengeLevel.EASY, "reedFarm", new ItemStack(Items.reeds));
		addChallenge(EnumChallengeLevel.EASY, "cobbleGen", new ItemStack(Blocks.cobblestone));
		addChallenge(EnumChallengeLevel.EASY, "pureDaisy", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY));
		addChallenge(EnumChallengeLevel.EASY, "battery", new ItemStack(ModBlocks.pool));

		// Normal Challenges
		addChallenge(EnumChallengeLevel.NORMAL, "apothecaryRefill", new ItemStack(ModBlocks.altar));
		addChallenge(EnumChallengeLevel.NORMAL, "treeFarm", new ItemStack(Blocks.sapling));
		addChallenge(EnumChallengeLevel.NORMAL, "fullCropFarm", new ItemStack(Items.wheat_seeds));
		addChallenge(EnumChallengeLevel.NORMAL, "animalFarm", new ItemStack(Items.leather));
		addChallenge(EnumChallengeLevel.NORMAL, "boneMealFarm", new ItemStack(Items.dye, 1, 15));
		addChallenge(EnumChallengeLevel.NORMAL, "orechid", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID));

		// Hard Challenges
		addChallenge(EnumChallengeLevel.HARD, "mobTower", new ItemStack(Items.bone));
		addChallenge(EnumChallengeLevel.HARD, "entropinnyumSetup", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENTROPINNYUM));
		addChallenge(EnumChallengeLevel.HARD, "spectrolusSetup", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SPECTROLUS));
		addChallenge(EnumChallengeLevel.HARD, "potionBrewer", new ItemStack(ModBlocks.brewery));

		// You have a problem
		addChallenge(EnumChallengeLevel.LUNATIC, "kekimurusSetup", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_KEKIMURUS));
		addChallenge(EnumChallengeLevel.LUNATIC, "autoQuarry", new ItemStack(Items.diamond_pickaxe));
		addChallenge(EnumChallengeLevel.LUNATIC, "runeCrafter", new ItemStack(ModItems.rune));
	}

	/**
	 * Adds a challenge to the Botania challenge registry, so that it will show up in the
	 * Lexica Botania as a challenge.
	 * @param level The difficulty level of this challenge.
	 * @param name The name of this challenge.
	 * @param icon The icon of this challenge, represented as the icon of the provided item stack.
	 */
	public static void addChallenge(EnumChallengeLevel level, String name, ItemStack icon) {
		Challenge c = new Challenge("botania.challenge." + name, icon, level);

		// Insert the challenge
		challenges.get(level).add(c);
		challengeLookup.put(c.unlocalizedName, c);
	}

}
