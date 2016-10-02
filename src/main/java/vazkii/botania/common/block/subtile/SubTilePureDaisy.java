/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 28, 2014, 9:09:39 PM (GMT)]
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTilePureDaisy extends SubTileEntity {

	private static final String TAG_POSITION = "position";
	private static final String TAG_TICKS_REMAINING = "ticksRemaining";

	private static final BlockPos[] POSITIONS = {
			new BlockPos(-1, 0, -1 ),
			new BlockPos(-1, 0, 0 ),
			new BlockPos(-1, 0, 1 ),
			new BlockPos(0, 0, 1 ),
			new BlockPos(1, 0, 1 ),
			new BlockPos(1, 0, 0 ),
			new BlockPos(1, 0, -1 ),
			new BlockPos(0, 0, -1 ),
	};

	int positionAt = 0;
	final int[] ticksRemaining = { -1, -1, -1, -1, -1, -1, -1, -1};

	@Override
	public void onUpdate() {
		super.onUpdate();

		positionAt++;
		if(positionAt == POSITIONS.length)
			positionAt = 0;

		BlockPos acoords = POSITIONS[positionAt];
		BlockPos coords = supertile.getPos().add(acoords);
		World world = supertile.getWorld();
		if(!world.isAirBlock(coords)) {
			IBlockState state = world.getBlockState(coords);
			RecipePureDaisy recipe = null;
			for(RecipePureDaisy recipe_ : BotaniaAPI.pureDaisyRecipes)
				if(recipe_.matches(world, coords, this, state)) {
					recipe = recipe_;
					break;
				}


			if(recipe != null) {
				if (ticksRemaining[positionAt] == -1)
					ticksRemaining[positionAt] = recipe.getTime();
				ticksRemaining[positionAt]--;

				Botania.proxy.sparkleFX(coords.getX() + Math.random(), coords.getY() + Math.random(), coords.getZ() + Math.random(), 1F, 1F, 1F, (float) Math.random(), 5);

				if(ticksRemaining[positionAt] <= 0) {
					ticksRemaining[positionAt] = -1;

					if(recipe.set(world,coords, this)) {
						for(int i = 0; i < 25; i++) {
							double x = coords.getX() + Math.random();
							double y = coords.getY() + Math.random() + 0.5;
							double z = coords.getZ() + Math.random();

							Botania.proxy.wispFX(x, y, z, 1F, 1F, 1F, (float) Math.random() / 2F);
						}
						if(ConfigHandler.blockBreakParticles)
							supertile.getWorld().playEvent(2001, coords, Block.getStateId(recipe.getOutputState()));
					}
				}
			} else ticksRemaining[positionAt] = -1;
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), 1);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		positionAt = cmp.getInteger(TAG_POSITION);

		if(supertile.getWorld() != null && !supertile.getWorld().isRemote)
			for(int i = 0; i < ticksRemaining.length; i++)
				ticksRemaining[i] = cmp.getInteger(TAG_TICKS_REMAINING + i);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_POSITION, positionAt);
		for(int i = 0; i < ticksRemaining.length; i++)
			cmp.setInteger(TAG_TICKS_REMAINING + i, ticksRemaining[i]);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.pureDaisy;
	}
}
