/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.xplat.XplatAbstractions;

public class BotaniaBlockSetTypes {

	public static final BlockSetType LIVINGWOOD_BLOCK_SET = registerWoodBlockSetType("livingwood");
	public static final BlockSetType DREAMWOOD_BLOCK_SET = registerWoodBlockSetType("dreamwood");
	public static final BlockSetType SHIMMERWOOD_BLOCK_SET = registerWoodBlockSetType("shimmerwood");

	public static final BlockSetType LIVINGROCK_BLOCK_SET = registerStoneBlockSetType("livingrock");
	public static final BlockSetType SHIMMERROCK_BLOCK_SET = registerStoneBlockSetType("shimmerrock");
	public static final BlockSetType CORPOREA_BLOCK_SET = registerStoneBlockSetType("corporea");

	public static final BlockSetType METAMORPHIC_FOREST_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE);
	public static final BlockSetType METAMORPHIC_PLAINS_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_TALC);
	public static final BlockSetType METAMORPHIC_MOUNTAIN_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_GNEISS);
	public static final BlockSetType METAMORPHIC_FUNGAL_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE);
	public static final BlockSetType METAMORPHIC_SWAMP_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE);
	public static final BlockSetType METAMORPHIC_DESERT_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_SOLITE);
	public static final BlockSetType METAMORPHIC_TAIGA_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_LUNITE);
	public static final BlockSetType METAMORPHIC_MESA_BLOCK_SET = registerStoneBlockSetType(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC);

	public static final WoodType LIVINGWOOD = registerWoodType("livingwood", LIVINGWOOD_BLOCK_SET);
	public static final WoodType DREAMWOOD = registerWoodType("dreamwood", DREAMWOOD_BLOCK_SET);
	public static final WoodType SHIMMERWOOD = registerWoodType("shimmerwood", SHIMMERWOOD_BLOCK_SET);

	private static BlockSetType registerWoodBlockSetType(String name) {
		return XplatAbstractions.instance().registerBlockSetType(name,
				true,
				true,
				true,
				BlockSetType.PressurePlateSensitivity.EVERYTHING,
				SoundType.WOOD,
				SoundEvents.WOODEN_DOOR_CLOSE,
				SoundEvents.WOODEN_DOOR_OPEN,
				SoundEvents.WOODEN_TRAPDOOR_CLOSE,
				SoundEvents.WOODEN_TRAPDOOR_OPEN,
				SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
				SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
				SoundEvents.WOODEN_BUTTON_CLICK_OFF,
				SoundEvents.WOODEN_BUTTON_CLICK_ON);
	}

	private static BlockSetType registerStoneBlockSetType(String name) {
		return XplatAbstractions.instance().registerBlockSetType(
				name,
				true,
				true,
				false,
				BlockSetType.PressurePlateSensitivity.MOBS,
				SoundType.STONE,
				SoundEvents.IRON_DOOR_CLOSE,
				SoundEvents.IRON_DOOR_OPEN,
				SoundEvents.IRON_TRAPDOOR_CLOSE,
				SoundEvents.IRON_TRAPDOOR_OPEN,
				SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
				SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
				SoundEvents.STONE_BUTTON_CLICK_OFF,
				SoundEvents.STONE_BUTTON_CLICK_ON);
	}

	private static WoodType registerWoodType(String name, BlockSetType blockSetType) {
		return XplatAbstractions.instance().registerWoodType(name, blockSetType, SoundType.WOOD, SoundType.HANGING_SIGN, SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN);
	}
}
