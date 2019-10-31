/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 10, 2014, 7:55:12 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.base.Predicates;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileManaDetector extends TileMod implements ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.MANA_DETECTOR)
	public static TileEntityType<TileManaDetector> TYPE;

	public TileManaDetector() {
		super(TYPE);
	}

	@Override
	public void tick() {
		boolean state = world.getBlockState(getPos()).get(BotaniaStateProps.POWERED);
		boolean expectedState = world.getEntitiesWithinAABB(ThrowableEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)), Predicates.instanceOf(IManaBurst.class)).size() != 0;
		if(state != expectedState && !world.isRemote)
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(BotaniaStateProps.POWERED, expectedState));

		if(expectedState)
			for(int i = 0; i < 4; i++) {
                SparkleParticleData data = SparkleParticleData.sparkle(0.7F + 0.5F * (float) Math.random(), 1F, 0.2F, 0.2F, 5);
                world.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
            }
	}

}
