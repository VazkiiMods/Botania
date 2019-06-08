/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 10, 2014, 5:50:01 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileForestEye extends TileMod implements ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.FOREST_EYE)
	public static TileEntityType<TileForestEye> TYPE;

	public int entities = 0;

	public TileForestEye() {
		super(TYPE);
	}

	@Override
	public void tick() {
		if (world.isRemote)
			return;
		int range = 6;
		int entityCount = world.getEntitiesWithinAABB(AnimalEntity.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1))).size();
		if(entityCount != entities) {
			entities = entityCount;
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		}
	}

}
