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
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.state.enums.QuartzVariant;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public final class BotaniaStateProps {

	/** Unlisted properties **/

	// The property for specialFlower subtile id
	public static final PropertyObject<ResourceLocation> SUBTILE_ID = new PropertyObject<>("subtile_id", ResourceLocation.class);

	// The property for floating flower island type
	public static final PropertyObject<IFloatingFlower.IslandType> ISLAND_TYPE = new PropertyObject<>("islandtype", IFloatingFlower.IslandType.class);

	// The property for platform held blockstate id
	public static final PropertyObject<IBlockState> HELD_STATE = new PropertyObject<>("held_state", IBlockState.class);

	// The property for platform world object
	public static final PropertyObject<IBlockReader> HELD_WORLD = new PropertyObject<>("held_world", IBlockReader.class);

	// The proeprty for platform world pos
	public static final PropertyObject<BlockPos> HELD_POS = new PropertyObject<>("held_pos", BlockPos.class);

	/** Common properties to all blocks to use **/

	// The 16 Minecraft colors
	public static final EnumProperty<EnumDyeColor> COLOR = EnumProperty.create("color", EnumDyeColor.class);

	// The four cardinal directions
	public static final EnumProperty<EnumFacing> CARDINALS = EnumProperty.create("facing", EnumFacing.class, EnumFacing.Plane.HORIZONTAL);

	// All 6 directions
	public static final EnumProperty<EnumFacing> FACING = EnumProperty.create("facing", EnumFacing.class);

	// Redstone power - boolean
	// Also for any other simple boolean "on/off" state
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	/** Properties for certain kinds of blocks **/

	// The axis directions (think quartz pillars and wood logs)
	public static final EnumProperty<EnumFacing.Axis> AXIS_FACING = BlockRotatedPillar.AXIS;

	// Extra Quartz Blocks
	public static final EnumProperty<QuartzVariant> QUARTZ_VARIANT = EnumProperty.create("variant", QuartzVariant.class);

	/** Properties for individual blocks **/

	// BlockAlfPortal
	public static final EnumProperty<AlfPortalState> ALFPORTAL_STATE = EnumProperty.create("state", AlfPortalState.class);

	// BlockBiomeStoneA
	public static final EnumProperty<BiomeStoneVariant> BIOMESTONE_VARIANT = EnumProperty.create("variant", BiomeStoneVariant.class);

	// BlockBiomeStoneB
	public static final EnumProperty<BiomeBrickVariant> BIOMEBRICK_VARIANT = EnumProperty.create("variant", BiomeBrickVariant.class);

	// BlockBiomeStoneWall
	public static final EnumProperty<BiomeStoneVariant> BIOMESTONEWALL_VARIANT = EnumProperty.create("bswall_variant", BiomeStoneVariant.class, new Predicate<BiomeStoneVariant>() {
		@Override
		public boolean apply(BiomeStoneVariant input) {
			return input.getName().contains("cobble");
		}
	});

	// BlockDreamWood, BlockLivingWood
	public static final EnumProperty<LivingWoodVariant> LIVINGWOOD_VARIANT = EnumProperty.create("variant", LivingWoodVariant.class);

	// BlockEnchanter
	public static final EnumProperty<EnumFacing.Axis> ENCHANTER_DIRECTION = EnumProperty.create("facing", EnumFacing.Axis.class, Predicates.not(Predicates.equalTo(EnumFacing.Axis.Y)));

	// BlockGhostRail
	public static final EnumProperty<RailShape> RAIL_DIRECTION = BlockRailPowered.SHAPE;

	// BlockLightRelay
	public static final EnumProperty<LuminizerVariant> LUMINIZER_VARIANT = EnumProperty.create("variant", LuminizerVariant.class);

	// BlockModDoubleFlower (white to gray)
	public static final EnumProperty<EnumDyeColor> DOUBLEFLOWER_VARIANT_1 = EnumProperty.create("df_variant", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
		@Override
		public boolean apply(EnumDyeColor input) {
			return input.ordinal() >= 0 && input.ordinal() <= 7;
		}
	});

	// (silver to black)
	public static final EnumProperty<EnumDyeColor> DOUBLEFLOWER_VARIANT_2 = EnumProperty.create("df_variant", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
		@Override
		public boolean apply(EnumDyeColor input) {
			return input.ordinal() >= 8 && input.ordinal() <= 15;
		}
	});

	// BlockCraftyCrate
	public static final EnumProperty<CratePattern> CRATE_PATTERN = EnumProperty.create("pattern", CratePattern.class);

	// BlockPavement
	public static final EnumProperty<EnumDyeColor> PAVEMENT_COLOR = EnumProperty.create("color", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
		@Override
		public boolean apply(EnumDyeColor color) {
			return color == EnumDyeColor.WHITE || color == EnumDyeColor.BLACK
					|| color == EnumDyeColor.BLUE || color == EnumDyeColor.RED
					|| color == EnumDyeColor.YELLOW || color == EnumDyeColor.GREEN;
		}
	});

	// BlockPrism
	public static final BooleanProperty HAS_LENS = BooleanProperty.create("has_lens");

	private BotaniaStateProps() {
	}

}
