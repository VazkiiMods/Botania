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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ModChallenges {

	public static final Map<EnumChallengeLevel, List<Challenge>> challenges = new EnumMap<>(EnumChallengeLevel.class);
	public static final Map<String, Challenge> challengeLookup = new HashMap<>();

	public static void init() {
		for(EnumChallengeLevel level : EnumChallengeLevel.class.getEnumConstants())
			challenges.put(level, new ArrayList<>());

		addChallenge(EnumChallengeLevel.EASY, "flowerFarm", new ItemStack(ModBlocks.flower, 1, 6));
		addChallenge(EnumChallengeLevel.EASY, "recordFarm", new ItemStack(Items.RECORD_13));
		addChallenge(EnumChallengeLevel.EASY, "reedFarm", new ItemStack(Items.REEDS));
		addChallenge(EnumChallengeLevel.EASY, "cobbleGen", new ItemStack(Blocks.COBBLESTONE));
		addChallenge(EnumChallengeLevel.EASY, "pureDaisy", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY));
		addChallenge(EnumChallengeLevel.EASY, "battery", new ItemStack(ModBlocks.pool));

		addChallenge(EnumChallengeLevel.NORMAL, "apothecaryRefill", new ItemStack(ModBlocks.altar));
		addChallenge(EnumChallengeLevel.NORMAL, "treeFarm", new ItemStack(Blocks.SAPLING));
		addChallenge(EnumChallengeLevel.NORMAL, "fullCropFarm", new ItemStack(Items.WHEAT_SEEDS));
		addChallenge(EnumChallengeLevel.NORMAL, "animalFarm", new ItemStack(Items.LEATHER));
		addChallenge(EnumChallengeLevel.NORMAL, "boneMealFarm", new ItemStack(Items.DYE, 1, 15));
		addChallenge(EnumChallengeLevel.NORMAL, "orechid", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID));

		addChallenge(EnumChallengeLevel.HARD, "mobTower", new ItemStack(Items.BONE));
		addChallenge(EnumChallengeLevel.HARD, "entropinnyumSetup", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENTROPINNYUM));
		addChallenge(EnumChallengeLevel.HARD, "spectrolusSetup", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SPECTROLUS));
		addChallenge(EnumChallengeLevel.HARD, "shulkMeNotSetup", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SHULK_ME_NOT));
		addChallenge(EnumChallengeLevel.HARD, "potionBrewer", new ItemStack(ModBlocks.brewery));

		addChallenge(EnumChallengeLevel.LUNATIC, "kekimurusSetup", ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_KEKIMURUS));
		addChallenge(EnumChallengeLevel.LUNATIC, "autoQuarry", new ItemStack(Items.DIAMOND_PICKAXE));
		addChallenge(EnumChallengeLevel.LUNATIC, "runeCrafter", new ItemStack(ModItems.rune));
	}

	private static void addChallenge(EnumChallengeLevel level, String name, ItemStack icon) {
		Challenge c = new Challenge("botania.challenge." + name, icon, level);
		challenges.get(level).add(c);
		challengeLookup.put(c.unlocalizedName, c);
	}

}
