/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state;

import ModelProperty;
import com.google.common.base.Predicates;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.Direction;

import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.CratePattern;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public final class BotaniaStateProps {

	// The property for floating flower island type
	public static final ModelProperty<IFloatingFlower> FLOATING_DATA = new ModelProperty<>();

	// BlockAlfPortal
	public static final EnumProperty<AlfPortalState> ALFPORTAL_STATE = EnumProperty.of("state", AlfPortalState.class);

	// BlockEnchanter
	public static final EnumProperty<Direction.Axis> ENCHANTER_DIRECTION = EnumProperty.of("facing", Direction.Axis.class, Predicates.not(Predicates.equalTo(Direction.Axis.Y)));

	// BlockCraftyCrate
	public static final EnumProperty<CratePattern> CRATE_PATTERN = EnumProperty.of("pattern", CratePattern.class);

	// BlockPrism
	public static final BooleanProperty HAS_LENS = BooleanProperty.of("has_lens");

	private BotaniaStateProps() {}

}
