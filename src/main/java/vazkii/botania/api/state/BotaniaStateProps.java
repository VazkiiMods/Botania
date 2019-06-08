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
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.client.model.data.ModelProperty;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.CratePattern;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public final class BotaniaStateProps {

	/** Unlisted properties **/

	// The property for floating flower island type
	public static final ModelProperty<IFloatingFlower> FLOATING_DATA = new ModelProperty<>();

	// The property for platform held blockstate
	public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

	// The property for platform world pos
	public static final ModelProperty<BlockPos> HELD_POS = new ModelProperty<>();

	/** Common properties to all blocks to use **/

	// The 16 Minecraft colors
	public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

	// The four cardinal directions
	public static final EnumProperty<Direction> CARDINALS = EnumProperty.create("facing", Direction.class, Direction.Plane.HORIZONTAL);

	// All 6 directions
	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);

	// Redstone power - boolean
	// Also for any other simple boolean "on/off" state
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	/** Properties for individual blocks **/

	// BlockAlfPortal
	public static final EnumProperty<AlfPortalState> ALFPORTAL_STATE = EnumProperty.create("state", AlfPortalState.class);

	// BlockEnchanter
	public static final EnumProperty<Direction.Axis> ENCHANTER_DIRECTION = EnumProperty.create("facing", Direction.Axis.class, Predicates.not(Predicates.equalTo(Direction.Axis.Y)));

	// BlockCraftyCrate
	public static final EnumProperty<CratePattern> CRATE_PATTERN = EnumProperty.create("pattern", CratePattern.class);

	// BlockPrism
	public static final BooleanProperty HAS_LENS = BooleanProperty.create("has_lens");

	private BotaniaStateProps() {
	}

}
