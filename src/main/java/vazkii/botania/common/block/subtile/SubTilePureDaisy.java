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

import java.util.Arrays;

public class SubTilePureDaisy extends SubTileEntity {

	private static final String TAG_POSITION = "position";
	private static final String TAG_TICKS_REMAINING = "ticksRemaining";
	private static final int UPDATE_ACTIVE_EVENT = 0;
	private static final int RECIPE_COMPLETE_EVENT = 1;

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

	private int positionAt = 0;
	private final int[] ticksRemaining = new int[POSITIONS.length];

	// Bitfield of active positions, used clientside for particles
	private int activePositions = 0;

	public SubTilePureDaisy() {
		Arrays.fill(ticksRemaining, -1);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(getWorld().isRemote) {
			for (int i = 0; i < POSITIONS.length; i++) {
				if ((activePositions >>> i & 1) > 0) {
					BlockPos coords = getPos().add(POSITIONS[i]);
					Botania.proxy.sparkleFX(coords.getX() + Math.random(), coords.getY() + Math.random(), coords.getZ() + Math.random(), 1F, 1F, 1F, (float) Math.random(), 5);
				}
			}

			return;
		}

		positionAt++;
		if(positionAt == POSITIONS.length)
			positionAt = 0;

		BlockPos acoords = POSITIONS[positionAt];
		BlockPos coords = supertile.getPos().add(acoords);
		World world = supertile.getWorld();
		if(!world.isAirBlock(coords)) {
			world.profiler.startSection("findRecipe");
			RecipePureDaisy recipe = findRecipe(coords);
			world.profiler.endSection();

			if(recipe != null) {
				if (ticksRemaining[positionAt] == -1)
					ticksRemaining[positionAt] = recipe.getTime();
				ticksRemaining[positionAt]--;

				if(ticksRemaining[positionAt] <= 0) {
					ticksRemaining[positionAt] = -1;

					if(recipe.set(world,coords, this)) {
						world.addBlockEvent(getPos(), supertile.getBlockType(), RECIPE_COMPLETE_EVENT, positionAt);
						if(ConfigHandler.blockBreakParticles)
							supertile.getWorld().playEvent(2001, coords, Block.getStateId(recipe.getOutputState()));
					}
				}
			} else ticksRemaining[positionAt] = -1;
		} else ticksRemaining[positionAt] = -1;

		updateActivePositions();
	}

	private void updateActivePositions() {
		int newActivePositions = 0;
		for (int i = 0; i < ticksRemaining.length; i++) {
			if (ticksRemaining[i] > -1) {
				newActivePositions |= 1 << i;
			}
		}

		if (newActivePositions != activePositions) {
			getWorld().addBlockEvent(getPos(), supertile.getBlockType(), UPDATE_ACTIVE_EVENT, newActivePositions);
		}
	}

	private RecipePureDaisy findRecipe(BlockPos coords) {
		IBlockState state = getWorld().getBlockState(coords);

		for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
			if(recipe.matches(getWorld(), coords, this, state)) {
				return recipe;
			}
		}

		return null;
	}

	@Override
	public boolean receiveClientEvent(int type, int param) {
		switch (type) {
			case UPDATE_ACTIVE_EVENT: activePositions = param; return true;
			case RECIPE_COMPLETE_EVENT: {
				if (getWorld().isRemote) {
					BlockPos coords = getPos().add(POSITIONS[param]);
					for(int i = 0; i < 25; i++) {
						double x = coords.getX() + Math.random();
						double y = coords.getY() + Math.random() + 0.5;
						double z = coords.getZ() + Math.random();

						Botania.proxy.wispFX(x, y, z, 1F, 1F, 1F, (float) Math.random() / 2F);
					}
				}

				return true;
			}
			default: return super.receiveClientEvent(type, param);
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
