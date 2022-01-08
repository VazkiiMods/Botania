/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.xplat.BotaniaConfig;

import javax.annotation.Nullable;

import java.util.Arrays;

public class SubTilePureDaisy extends TileEntitySpecialFlower {
	private static final String TAG_POSITION = "position";
	private static final String TAG_TICKS_REMAINING = "ticksRemaining";
	private static final int RECIPE_COMPLETE_EVENT = 0;

	private static final BlockPos[] POSITIONS = {
			new BlockPos(-1, 0, -1),
			new BlockPos(-1, 0, 0),
			new BlockPos(-1, 0, 1),
			new BlockPos(0, 0, 1),
			new BlockPos(1, 0, 1),
			new BlockPos(1, 0, 0),
			new BlockPos(1, 0, -1),
			new BlockPos(0, 0, -1),
	};

	private int positionAt = 0;
	private final int[] prevTicksRemaining = new int[POSITIONS.length];
	private final int[] ticksRemaining = new int[POSITIONS.length];

	public SubTilePureDaisy(BlockPos pos, BlockState state) {
		super(ModSubtiles.PURE_DAISY, pos, state);
		Arrays.fill(prevTicksRemaining, -1);
		Arrays.fill(ticksRemaining, -1);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			for (int i = 0; i < POSITIONS.length; i++) {
				if (ticksRemaining[i] > 0) {
					BlockPos coords = getEffectivePos().offset(POSITIONS[i]);
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), 1F, 1F, 1F, 5);
					level.addParticle(data, coords.getX() + Math.random(), coords.getY() + Math.random(), coords.getZ() + Math.random(), 0, 0, 0);
				}
			}

			return;
		}

		positionAt++;
		if (positionAt == POSITIONS.length) {
			positionAt = 0;
		}

		BlockPos acoords = POSITIONS[positionAt];
		BlockPos coords = getEffectivePos().offset(acoords);
		Level world = getLevel();
		if (!world.isEmptyBlock(coords)) {
			world.getProfiler().push("findRecipe");
			IPureDaisyRecipe recipe = findRecipe(coords);
			world.getProfiler().pop();

			if (recipe != null) {
				if (ticksRemaining[positionAt] == -1) {
					ticksRemaining[positionAt] = recipe.getTime();
				}

				ticksRemaining[positionAt]--;

				if (ticksRemaining[positionAt] <= 0) {
					ticksRemaining[positionAt] = -1;

					if (recipe.set(world, coords, this)) {
						if (BotaniaConfig.common().blockBreakParticles()) {
							getLevel().levelEvent(2001, coords, Block.getId(recipe.getOutputState()));
						}
						getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), RECIPE_COMPLETE_EVENT, positionAt);
					}
				}

			} else {
				ticksRemaining[positionAt] = -1;
			}
		} else {
			ticksRemaining[positionAt] = -1;
		}

		if (!Arrays.equals(ticksRemaining, prevTicksRemaining)) {
			setChanged();
			sync();
			System.arraycopy(ticksRemaining, 0, prevTicksRemaining, 0, POSITIONS.length);
		}
	}

	@Nullable
	private IPureDaisyRecipe findRecipe(BlockPos coords) {
		BlockState state = getLevel().getBlockState(coords);

		for (Recipe<?> recipe : ModRecipeTypes.getRecipes(level, ModRecipeTypes.PURE_DAISY_TYPE).values()) {
			if (recipe instanceof IPureDaisyRecipe && ((IPureDaisyRecipe) recipe).matches(getLevel(), coords, this, state)) {
				return ((IPureDaisyRecipe) recipe);
			}
		}

		return null;
	}

	@Override
	public boolean triggerEvent(int type, int param) {
		switch (type) {
			case RECIPE_COMPLETE_EVENT: {
				if (getLevel().isClientSide) {
					BlockPos coords = getEffectivePos().offset(POSITIONS[param]);
					for (int i = 0; i < 25; i++) {
						double x = coords.getX() + Math.random();
						double y = coords.getY() + Math.random() + 0.5;
						double z = coords.getZ() + Math.random();

						WispParticleData data = WispParticleData.wisp((float) Math.random() / 2F, 1, 1, 1);
						getLevel().addParticle(data, x, y, z, 0, 0, 0);
					}
				}

				return true;
			}
			default:
				return super.triggerEvent(type, param);
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), 1);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		positionAt = cmp.getInt(TAG_POSITION);

		for (int i = 0; i < ticksRemaining.length; i++) {
			ticksRemaining[i] = cmp.getInt(TAG_TICKS_REMAINING + i);
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_POSITION, positionAt);
		for (int i = 0; i < ticksRemaining.length; i++) {
			cmp.putInt(TAG_TICKS_REMAINING + i, ticksRemaining[i]);
		}
	}

}
