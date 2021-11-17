/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;

import java.util.Set;

public class SubTileAgricarnation extends TileEntityFunctionalFlower {
	private static final Set<Material> MATERIALS = ImmutableSet.of(Material.PLANT, Material.CACTUS, Material.GRASS,
			Material.LEAVES, Material.VEGETABLE, Material.WATER_PLANT, Material.BAMBOO, Material.BAMBOO_SAPLING);
	private static final int RANGE = 5;
	private static final int RANGE_MINI = 2;

	protected SubTileAgricarnation(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileAgricarnation(BlockPos pos, BlockState state) {
		this(ModSubtiles.AGRICARNATION, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		if (ticksExisted % 200 == 0) {
			sync();
		}

		if (ticksExisted % 6 == 0 && redstoneSignal == 0) {
			int range = getRange();
			int x = getEffectivePos().getX() + getLevel().random.nextInt(range * 2 + 1) - range;
			int z = getEffectivePos().getZ() + getLevel().random.nextInt(range * 2 + 1) - range;

			for (int i = 4; i > -2; i--) {
				int y = getEffectivePos().getY() + i;
				BlockPos pos = new BlockPos(x, y, z);
				if (getLevel().isEmptyBlock(pos)) {
					continue;
				}

				if (isPlant(pos) && getMana() > 5) {
					BlockState state = getLevel().getBlockState(pos);
					addMana(-5);
					state.randomTick((ServerLevel) level, pos, level.random);
					if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
						getLevel().levelEvent(2005, pos, 6 + getLevel().random.nextInt(4));
					}
					getLevel().playSound(null, x, y, z, ModSounds.agricarnation, SoundSource.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);

					break;
				}
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	/**
	 * @return Whether the block at {@code pos} grows "naturally". That is, whether its IGrowable action is simply
	 *         growing itself, instead of something like spreading around or creating flowers around, etc, and whether
	 *         this
	 *         action would have happened normally over time without bonemeal.
	 */
	private boolean isPlant(BlockPos pos) {
		BlockState state = getLevel().getBlockState(pos);
		Block block = state.getBlock();

		// Spreads when ticked
		if (block instanceof SpreadingSnowyDirtBlock) {
			return false;
		}

		// Exclude all BushBlock except known vanilla subclasses
		if (block instanceof BushBlock && !(block instanceof CropBlock) && !(block instanceof StemBlock)
				&& !(block instanceof SaplingBlock) && !(block instanceof SweetBerryBushBlock)) {
			return false;
		}

		return MATERIALS.contains(state.getMaterial())
				&& block instanceof BonemealableBlock
				&& ((BonemealableBlock) block).isValidBonemealTarget(getLevel(), pos, state, getLevel().isClientSide);
	}

	@Override
	public int getColor() {
		return 0x8EF828;
	}

	@Override
	public int getMaxMana() {
		return 200;
	}

	public int getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public static class Mini extends SubTileAgricarnation {
		public Mini(BlockPos pos, BlockState state) {
			super(ModSubtiles.AGRICARNATION_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
