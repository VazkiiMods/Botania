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

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileTeruTeruBozu extends TileMod implements ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.TERU_TERU_BOZU)
	public static TileEntityType<TileTeruTeruBozu> TYPE;
	private boolean wasRaining = false;

	public TileTeruTeruBozu() {
		super(TYPE);
	}

	@Override
	public void tick() {
		if (world.isRemote)
			return;

		boolean isRaining = world.isRaining();
		if(isRaining && world.rand.nextInt(9600) == 0) {
			world.getWorldInfo().setRaining(false);
			resetRainTime(world);
		}

		if(wasRaining != isRaining)
			world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		wasRaining = isRaining;
	}

	public static void resetRainTime(World w){
	    w.getWorldInfo().setRainTime(w.rand.nextInt(w.getWorldInfo().isRaining() ? 12000 : 168000) + 12000);
	}
}
