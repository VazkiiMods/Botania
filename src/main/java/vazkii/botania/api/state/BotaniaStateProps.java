/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.AltGrassVariant;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.api.state.enums.CrateVariant;
import vazkii.botania.api.state.enums.CustomBrickVariant;
import vazkii.botania.api.state.enums.DrumVariant;
import vazkii.botania.api.state.enums.LivingRockVariant;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.state.enums.PlatformVariant;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.api.state.enums.QuartzVariant;
import vazkii.botania.api.state.enums.SpreaderVariant;
import vazkii.botania.api.state.enums.StorageVariant;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public final class BotaniaStateProps {

	/** Unlisted properties **/

	// The property for specialFlower subtile id
	public static final PropertyObject<String> SUBTILE_ID = new PropertyObject<>("subtile_id", String.class);

	// The property for floating flower island type
	public static final PropertyObject<IFloatingFlower.IslandType> ISLAND_TYPE = new PropertyObject<>("islandtype", IFloatingFlower.IslandType.class);

	// The property for platform held blockstate id
	public static final PropertyObject<IBlockState> HELD_STATE = new PropertyObject<>("held_state", IBlockState.class);

	// The property for platform world object
	public static final PropertyObject<IBlockAccess> HELD_WORLD = new PropertyObject<>("held_world", IBlockAccess.class);

	// The proeprty for platform world pos
	public static final PropertyObject<BlockPos> HELD_POS = new PropertyObject<>("held_pos", BlockPos.class);

	/** Common properties to all blocks to use **/

	// The 16 Minecraft colors
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	// The four cardinal directions
	public static final PropertyEnum<EnumFacing> CARDINALS = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.Plane.HORIZONTAL);

	// All 6 directions
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

	// Redstone power - boolean
	// Also for any other simple boolean "on/off" state
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	/** Properties for certain kinds of blocks **/

	// The axis directions (think quartz pillars and wood logs)
	public static final PropertyEnum<EnumFacing.Axis> AXIS_FACING = BlockRotatedPillar.AXIS;

	// Extra Quartz Blocks
	public static final PropertyEnum<QuartzVariant> QUARTZ_VARIANT = PropertyEnum.create("variant", QuartzVariant.class);

	/** Properties for individual blocks **/

	// BlockAlfPortal
	public static final PropertyEnum<AlfPortalState> ALFPORTAL_STATE = PropertyEnum.create("state", AlfPortalState.class);

	// BlockAltar
	public static final PropertyEnum<AltarVariant> ALTAR_VARIANT = PropertyEnum.create("variant", AltarVariant.class);

	// BlockAltGrass
	public static final PropertyEnum<AltGrassVariant> ALTGRASS_VARIANT = PropertyEnum.create("variant", AltGrassVariant.class);

	// BlockBiomeStoneA
	public static final PropertyEnum<BiomeStoneVariant> BIOMESTONE_VARIANT = PropertyEnum.create("variant", BiomeStoneVariant.class);

	// BlockBiomeStoneB
	public static final PropertyEnum<BiomeBrickVariant> BIOMEBRICK_VARIANT = PropertyEnum.create("variant", BiomeBrickVariant.class);

	// BlockBiomeStoneWall
	public static final PropertyEnum<BiomeStoneVariant> BIOMESTONEWALL_VARIANT = PropertyEnum.create("bswall_variant", BiomeStoneVariant.class, new Predicate<BiomeStoneVariant>() {
		@Override
		public boolean apply(BiomeStoneVariant input) {
			return input.getName().contains("cobble");
		}
	});

	// BlockCustomBrick
	public static final PropertyEnum<CustomBrickVariant> CUSTOMBRICK_VARIANT = PropertyEnum.create("variant", CustomBrickVariant.class);

	// BlockDreamWood, BlockLivingWood
	public static final PropertyEnum<LivingWoodVariant> LIVINGWOOD_VARIANT = PropertyEnum.create("variant", LivingWoodVariant.class);

	// BlockEnchanter
	public static final PropertyEnum<EnumFacing.Axis> ENCHANTER_DIRECTION = PropertyEnum.create("facing", EnumFacing.Axis.class, Predicates.not(Predicates.equalTo(EnumFacing.Axis.Y)));

	// BlockForestDrum
	public static final PropertyEnum<DrumVariant> DRUM_VARIANT = PropertyEnum.create("variant", DrumVariant.class);

	// BlockGhostRail
	public static final PropertyEnum<BlockRailBase.EnumRailDirection> RAIL_DIRECTION = BlockRailPowered.SHAPE;

	// BlockLightRelay
	public static final PropertyEnum<LuminizerVariant> LUMINIZER_VARIANT = PropertyEnum.create("variant", LuminizerVariant.class);

	// BlockLivingRock
	public static final PropertyEnum<LivingRockVariant> LIVINGROCK_VARIANT = PropertyEnum.create("variant", LivingRockVariant.class);

	// BlockModDoubleFlower (white to gray)
	public static final PropertyEnum<EnumDyeColor> DOUBLEFLOWER_VARIANT_1 = PropertyEnum.create("df_variant", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
		@Override
		public boolean apply(EnumDyeColor input) {
			return input.ordinal() >= 0 && input.ordinal() <= 7;
		}
	});

	// (silver to black)
	public static final PropertyEnum<EnumDyeColor> DOUBLEFLOWER_VARIANT_2 = PropertyEnum.create("df_variant", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
		@Override
		public boolean apply(EnumDyeColor input) {
			return input.ordinal() >= 8 && input.ordinal() <= 15;
		}
	});

	// BlockOpenCrate
	public static final PropertyEnum<CrateVariant> CRATE_VARIANT = PropertyEnum.create("variant", CrateVariant.class);
	public static final PropertyEnum<CratePattern> CRATE_PATTERN = PropertyEnum.create("pattern", CratePattern.class);

	// BlockPavement
	public static final PropertyEnum<EnumDyeColor> PAVEMENT_COLOR = PropertyEnum.create("color", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
		@Override
		public boolean apply(EnumDyeColor color) {
			return color == EnumDyeColor.WHITE || color == EnumDyeColor.BLACK
					|| color == EnumDyeColor.BLUE || color == EnumDyeColor.RED
					|| color == EnumDyeColor.YELLOW || color == EnumDyeColor.GREEN;
		}
	});

	// BlockPlatform
	public static final PropertyEnum<PlatformVariant> PLATFORM_VARIANT = PropertyEnum.create("variant", PlatformVariant.class);

	// BlockPool
	public static final PropertyEnum<PoolVariant> POOL_VARIANT = PropertyEnum.create("variant", PoolVariant.class);

	// BlockPrism
	public static final PropertyBool HAS_LENS = PropertyBool.create("has_lens");

	// BlockPylon
	public static final PropertyEnum<PylonVariant> PYLON_VARIANT = PropertyEnum.create("variant", PylonVariant.class);

	// BlockSpreader
	public static final PropertyEnum<SpreaderVariant> SPREADER_VARIANT = PropertyEnum.create("variant", SpreaderVariant.class);

	// BlockStorage
	public static final PropertyEnum<StorageVariant> STORAGE_VARIANT = PropertyEnum.create("variant", StorageVariant.class);

	private BotaniaStateProps() {
	}

}
