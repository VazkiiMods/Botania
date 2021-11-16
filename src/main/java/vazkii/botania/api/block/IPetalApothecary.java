/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;

import java.util.Locale;

/**
 * Base Interface for the Petal Apothecary block entity
 */
public interface IPetalApothecary {
	enum State implements StringRepresentable {
		EMPTY,
		WATER,
		LAVA;

		@Nonnull
		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public Fluid asVanilla() {
			return switch (this) {
			case EMPTY -> Fluids.EMPTY;
			case WATER -> Fluids.WATER;
			case LAVA -> Fluids.LAVA;
			};
		}
	}

	/**
	 * Set the contained fluid.
	 */
	void setFluid(State fluid);

	/**
	 * Get the contained fluid.
	 */
	State getFluid();

	default BlockEntity blockEntity() {
		return (BlockEntity) this;
	}
}
