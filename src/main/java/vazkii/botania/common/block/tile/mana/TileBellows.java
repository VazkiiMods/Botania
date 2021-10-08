/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ExoflameFurnaceHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.mixin.AccessorAbstractFurnaceBlockEntity;

public class TileBellows extends TileMod {
	private static final String TAG_ACTIVE = "active";

	public float movePos;
	public boolean active = false;
	public float moving = 0F;

	public TileBellows(BlockPos pos, BlockState state) {
		super(ModTiles.BELLOWS, pos, state);
	}

	public void interact() {
		if (moving == 0F) {
			setActive(true);
		}
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileBellows self) {
		boolean disable = true;
		BlockEntity tile = self.getLinkedTile();
		if (!self.active && tile instanceof TilePool) {
			TilePool pool = (TilePool) tile;
			boolean transfer = pool.isDoingTransfer;
			if (transfer) {
				if (pool.ticksDoingTransfer > 0) {
					self.setActive(true);
				}
				disable = false;
			}
		}

		float max = 0.9F;
		float min = 0F;

		float incr = max / 20F;

		if (self.movePos < max && self.active && self.moving >= 0F) {
			if (self.moving == 0F) {
				level.playSound(null, worldPosition, ModSounds.bellows, SoundSource.BLOCKS, 1F, 1F);
			}

			if (tile instanceof AbstractFurnaceBlockEntity) {
				AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity) tile;
				Pair<AbstractCookingRecipe, Boolean> p = canSmelt(furnace);
				if (p != null) {
					AbstractCookingRecipe recipe = p.getFirst();
					boolean canSmelt = p.getSecond();
					if (canSmelt) {
						AccessorAbstractFurnaceBlockEntity mFurnace = (AccessorAbstractFurnaceBlockEntity) furnace;
						mFurnace.setCookingProgress(Math.min(mFurnace.getCookingTotalTime(), mFurnace.getCookingProgress() + 20));
						mFurnace.setLitTime(Math.max(0, mFurnace.getLitTime() - 10));
					}

					if (furnace instanceof FurnaceBlockEntity
							&& furnace.hasLevel() && furnace.getBlockState().getValue(FurnaceBlock.LIT)) {
						// [VanillaCopy] FurnaceBlock
						double d0 = (double) worldPosition.getX() + 0.5D;
						double d1 = (double) worldPosition.getY();
						double d2 = (double) worldPosition.getZ() + 0.5D;
						// Botania: no playSound

						Direction enumfacing = furnace.getBlockState().getValue(FurnaceBlock.FACING);
						Direction.Axis enumfacing$axis = enumfacing.getAxis();
						double d3 = 0.52D;
						double d4 = level.random.nextDouble() * 0.6D - 0.3D;
						double d5 = enumfacing$axis == Direction.Axis.X ? (double) enumfacing.getStepX() * 0.52D : d4;
						double d6 = level.random.nextDouble() * 6.0D / 16.0D;
						double d7 = enumfacing$axis == Direction.Axis.Z ? (double) enumfacing.getStepZ() * 0.52D : d4;
						level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
						level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
					}
				}
			}

			self.movePos += incr * 3;
			self.moving = incr * 3;
			if (self.movePos >= max) {
				self.movePos = Math.min(max, self.movePos);
				self.moving = 0F;
				if (disable) {
					self.setActive(false);
				}
			}
		} else if (self.movePos > min) {
			self.movePos -= incr;
			self.moving = -incr;
			if (self.movePos <= min) {
				self.movePos = Math.max(min, self.movePos);
				self.moving = 0F;
			}
		}

	}

	public BlockEntity getLinkedTile() {
		Direction side = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		return level.getBlockEntity(getBlockPos().relative(side));
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putBoolean(TAG_ACTIVE, active);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		active = cmp.getBoolean(TAG_ACTIVE);
	}

	public void setActive(boolean active) {
		if (!level.isClientSide) {
			boolean diff = this.active != active;
			this.active = active;
			if (diff) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}
	}

	public static Pair<AbstractCookingRecipe, Boolean> canSmelt(AbstractFurnaceBlockEntity furnace) {
		RecipeType<? extends AbstractCookingRecipe> rt = ExoflameFurnaceHandler.getRecipeType(furnace);
		AbstractCookingRecipe recipe = furnace.getLevel().getRecipeManager().getRecipeFor(rt, furnace, furnace.getLevel()).orElse(null);
		boolean canSmelt = ExoflameFurnaceHandler.canSmelt(furnace, recipe);
		return Pair.of(recipe, canSmelt);
	}

}
