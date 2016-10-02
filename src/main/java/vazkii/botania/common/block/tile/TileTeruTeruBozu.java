/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 1, 2015, 1:11:44 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

public class TileTeruTeruBozu extends TileMod {

	public boolean wasRaining = false;

	@Override
	public void update() {
		if (worldObj.isRemote)
			return;

		boolean isRaining = worldObj.isRaining();
		if(isRaining && worldObj.rand.nextInt(9600) == 0)
			worldObj.getWorldInfo().setRaining(false);

		if(wasRaining != isRaining)
			worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
		wasRaining = isRaining;
	}

}
