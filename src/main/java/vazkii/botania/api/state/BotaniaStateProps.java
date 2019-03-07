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
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.CratePattern;

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

	/** Properties for individual blocks **/

	// BlockAlfPortal
	public static final EnumProperty<AlfPortalState> ALFPORTAL_STATE = EnumProperty.create("state", AlfPortalState.class);

	// BlockEnchanter
	public static final EnumProperty<EnumFacing.Axis> ENCHANTER_DIRECTION = EnumProperty.create("facing", EnumFacing.Axis.class, Predicates.not(Predicates.equalTo(EnumFacing.Axis.Y)));

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

	// BlockPrism
	public static final BooleanProperty HAS_LENS = BooleanProperty.create("has_lens");

	private BotaniaStateProps() {
	}

}
