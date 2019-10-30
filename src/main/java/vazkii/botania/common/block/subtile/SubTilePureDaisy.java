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
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.Arrays;

public class SubTilePureDaisy extends TileEntitySpecialFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":pure_daisy")
	public static TileEntityType<SubTilePureDaisy> TYPE;

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
		super(TYPE);
		Arrays.fill(ticksRemaining, -1);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote) {
			for (int i = 0; i < POSITIONS.length; i++) {
				if ((activePositions >>> i & 1) > 0) {
					BlockPos coords = getPos().add(POSITIONS[i]);
                    SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), 1F, 1F, 1F, 5);
                    world.addParticle(data, coords.getX() + Math.random(), coords.getY() + Math.random(), coords.getZ() + Math.random(), 0, 0, 0);
                }
			}

			return;
		}

		positionAt++;
		if(positionAt == POSITIONS.length)
			positionAt = 0;

		BlockPos acoords = POSITIONS[positionAt];
		BlockPos coords = getPos().add(acoords);
		World world = getWorld();
		if(!world.isAirBlock(coords)) {
			world.getProfiler().startSection("findRecipe");
			RecipePureDaisy recipe = findRecipe(coords);
			world.getProfiler().endSection();

			if(recipe != null) {
				if (ticksRemaining[positionAt] == -1)
					ticksRemaining[positionAt] = recipe.getTime();
				ticksRemaining[positionAt]--;

				if(ticksRemaining[positionAt] <= 0) {
					ticksRemaining[positionAt] = -1;

					if(recipe.set(world,coords, this)) {
						world.addBlockEvent(getPos(), getBlockState().getBlock(), RECIPE_COMPLETE_EVENT, positionAt);
						if(ConfigHandler.COMMON.blockBreakParticles.get())
							getWorld().playEvent(2001, coords, Block.getStateId(recipe.getOutputState()));
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
			getWorld().addBlockEvent(getPos(), getBlockState().getBlock(), UPDATE_ACTIVE_EVENT, newActivePositions);
		}
	}

	private RecipePureDaisy findRecipe(BlockPos coords) {
		BlockState state = getWorld().getBlockState(coords);

		for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes.values()) {
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

						WispParticleData data = WispParticleData.wisp((float) Math.random() / 2F, 1, 1, 1);
						getWorld().addParticle(data, x, y, z, 0, 0, 0);
					}
				}

				return true;
			}
			default: return super.receiveClientEvent(type, param);
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(getPos(), 1);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		positionAt = cmp.getInt(TAG_POSITION);

		if(getWorld() != null && !getWorld().isRemote)
			for(int i = 0; i < ticksRemaining.length; i++)
				ticksRemaining[i] = cmp.getInt(TAG_TICKS_REMAINING + i);
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_POSITION, positionAt);
		for(int i = 0; i < ticksRemaining.length; i++)
			cmp.putInt(TAG_TICKS_REMAINING + i, ticksRemaining[i]);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.pureDaisy;
	}
}
