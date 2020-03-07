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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.LibMisc;

import java.util.Set;

public class SubTileAgricarnation extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":agricarnation") public static TileEntityType<SubTileAgricarnation> TYPE;

	private static final Set<Material> MATERIALS = ImmutableSet.of(Material.PLANTS, Material.CACTUS, Material.ORGANIC,
			Material.LEAVES, Material.GOURD, Material.OCEAN_PLANT, Material.BAMBOO);
	private static final int RANGE = 5;
	private static final int RANGE_MINI = 2;

	protected SubTileAgricarnation(TileEntityType<?> type) {
		super(type);
	}

	public SubTileAgricarnation() {
		this(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			return;
		}

		if (ticksExisted % 200 == 0) {
			sync();
		}

		if (ticksExisted % 6 == 0 && redstoneSignal == 0) {
			int range = getRange();
			int x = getEffectivePos().getX() + getWorld().rand.nextInt(range * 2 + 1) - range;
			int z = getEffectivePos().getZ() + getWorld().rand.nextInt(range * 2 + 1) - range;

			for (int i = 4; i > -2; i--) {
				int y = getEffectivePos().getY() + i;
				BlockPos pos = new BlockPos(x, y, z);
				if (getWorld().isAirBlock(pos)) {
					continue;
				}

				if (isPlant(pos) && getMana() > 5) {
					Block block = getWorld().getBlockState(pos).getBlock();
					addMana(-5);
					getWorld().getPendingBlockTicks().scheduleTick(pos, block, 1);
					if (ConfigHandler.COMMON.blockBreakParticles.get()) {
						getWorld().playEvent(2005, pos, 6 + getWorld().rand.nextInt(4));
					}
					getWorld().playSound(null, x, y, z, ModSounds.agricarnation, SoundCategory.BLOCKS, 0.01F, 0.5F + (float) Math.random() * 0.5F);

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
		BlockState state = getWorld().getBlockState(pos);
		Block block = state.getBlock();

		// Grass spreads when ticked
		if (block == Blocks.GRASS_BLOCK) {
			return false;
		}

		// Exclude all BushBlock except known vanilla subclasses
		if (block instanceof BushBlock && !(block instanceof CropsBlock)
				&& !(block instanceof SaplingBlock) && !(block instanceof SweetBerryBushBlock)) {
			return false;
		}

		return MATERIALS.contains(state.getMaterial())
				&& block instanceof IGrowable
				&& ((IGrowable) block).canGrow(getWorld(), pos, state, getWorld().isRemote);
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
		@ObjectHolder(LibMisc.MOD_ID + ":agricarnation_chibi") public static TileEntityType<SubTileAgricarnation> TYPE;

		public Mini() {
			super(TYPE);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
