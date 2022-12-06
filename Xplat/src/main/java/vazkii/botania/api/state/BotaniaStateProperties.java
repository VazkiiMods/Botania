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
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.api.state.enums.CraftyCratePattern;

import java.util.Optional;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public final class BotaniaStateProperties {

	public enum OptionalDyeColor implements StringRepresentable {
		WHITE,
		ORANGE,
		MAGENTA,
		LIGHT_BLUE,
		YELLOW,
		LIME,
		PINK,
		GRAY,
		LIGHT_GRAY,
		CYAN,
		PURPLE,
		BLUE,
		BROWN,
		GREEN,
		RED,
		BLACK,
		NONE;

		public Optional<DyeColor> toDyeColor() {
			return switch (this) {
				case WHITE -> Optional.of(DyeColor.WHITE);
				case ORANGE -> Optional.of(DyeColor.ORANGE);
				case MAGENTA -> Optional.of(DyeColor.MAGENTA);
				case LIGHT_BLUE -> Optional.of(DyeColor.LIGHT_BLUE);
				case YELLOW -> Optional.of(DyeColor.YELLOW);
				case LIME -> Optional.of(DyeColor.LIME);
				case PINK -> Optional.of(DyeColor.PINK);
				case GRAY -> Optional.of(DyeColor.GRAY);
				case LIGHT_GRAY -> Optional.of(DyeColor.LIGHT_GRAY);
				case CYAN -> Optional.of(DyeColor.CYAN);
				case PURPLE -> Optional.of(DyeColor.PURPLE);
				case BLUE -> Optional.of(DyeColor.BLUE);
				case BROWN -> Optional.of(DyeColor.BROWN);
				case GREEN -> Optional.of(DyeColor.GREEN);
				case RED -> Optional.of(DyeColor.RED);
				case BLACK -> Optional.of(DyeColor.BLACK);
				case NONE -> Optional.empty();
			};
		}

		public static OptionalDyeColor fromDyeColor(DyeColor color) {
			return switch (color) {
				case WHITE -> WHITE;
				case ORANGE -> ORANGE;
				case MAGENTA -> MAGENTA;
				case LIGHT_BLUE -> LIGHT_BLUE;
				case YELLOW -> YELLOW;
				case LIME -> LIME;
				case PINK -> PINK;
				case GRAY -> GRAY;
				case LIGHT_GRAY -> LIGHT_GRAY;
				case CYAN -> CYAN;
				case PURPLE -> PURPLE;
				case BLUE -> BLUE;
				case BROWN -> BROWN;
				case GREEN -> GREEN;
				case RED -> RED;
				case BLACK -> BLACK;
			};
		}

		public static OptionalDyeColor fromOptionalDyeColor(Optional<DyeColor> color) {
			return color.map(OptionalDyeColor::fromDyeColor).orElse(NONE);
		}

		@Override
		public @NotNull String getSerializedName() {
			return this.toDyeColor().map(DyeColor::getSerializedName).orElse("none");
		}
	}

	// Currently only ManaPoolBlock
	public static final EnumProperty<OptionalDyeColor> OPTIONAL_DYE_COLOR = EnumProperty.create("color", OptionalDyeColor.class);

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
