/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

/**
 * A TileEntity that implements this interface has an IO key lock. This
 * interface defines an input and output key.<br>
 * <br>
 * A Spreader can only shoot mana into a IKeyLocked interfaced block entity if the Input
 * key of the block entity is equal to the Output key of the Spreader.<br>
 * <br>
 * A Spreader can only pull mana from a IKeyLocked interfaced IManaPool block entity if the
 * Output key of the IManaPool is equal to the Input key of the Spreader.
 */
public interface IKeyLocked {

	String getInputKey();

	String getOutputKey();

}
