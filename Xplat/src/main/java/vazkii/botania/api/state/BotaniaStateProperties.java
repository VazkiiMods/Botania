/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.api.state.enums.CraftyCratePattern;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public final class BotaniaStateProperties {

	// AlfheimPortalBlock
	public static final EnumProperty<AlfheimPortalState> ALFPORTAL_STATE = EnumProperty.create("state", AlfheimPortalState.class);

	// ManaEnchanterBlock
	public static final EnumProperty<Direction.Axis> ENCHANTER_DIRECTION = EnumProperty.create("facing", Direction.Axis.class, a -> a != Direction.Axis.Y);

	// CraftyCrateBlock
	public static final EnumProperty<CraftyCratePattern> CRATE_PATTERN = EnumProperty.create("pattern", CraftyCratePattern.class);

	// ManaPrismBlock
	public static final BooleanProperty HAS_LENS = BooleanProperty.create("has_lens");

	// ManaSpreaderBlock
	public static final BooleanProperty HAS_SCAFFOLDING = BooleanProperty.create("has_scaffolding");

	private BotaniaStateProperties() {}

}
