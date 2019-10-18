/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 30, 2014, 4:22:15 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;

/**
 * Base Interface for the Petal Apothecary block. Can
 * be safely casted to TileEntity.
 */
public interface IPetalApothecary {

	/**
	 * Set the contained fluid. Must be {@link Fluids#WATER}, {@link Fluids#LAVA}, or {@link Fluids#EMPTY}.
	 */
	public void setFluid(Fluid fluid);

	/**
	 * Get the contained fluid. Will be {@link Fluids#WATER}, {@link Fluids#LAVA}, or {@link Fluids#EMPTY}.
	 */
	public Fluid getFluid();
}
