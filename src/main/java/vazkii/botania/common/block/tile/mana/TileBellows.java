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

import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ExoflameFurnaceHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.mixin.AccessorAbstractFurnaceTileEntity;

public class TileBellows extends TileMod implements Tickable {
	private static final String TAG_ACTIVE = "active";

	public float movePos;
	public boolean active = false;
	public float moving = 0F;

	public TileBellows() {
		super(ModTiles.BELLOWS);
	}

	public void interact() {
		if (moving == 0F) {
			setActive(true);
		}
	}

	@Override
	public void tick() {
		boolean disable = true;
		BlockEntity tile = getLinkedTile();
		if (!active && tile instanceof TilePool) {
			TilePool pool = (TilePool) tile;
			boolean transfer = pool.isDoingTransfer;
			if (transfer) {
				if (pool.ticksDoingTransfer > 0) {
					setActive(true);
				}
				disable = false;
			}
		}

		float max = 0.9F;
		float min = 0F;

		float incr = max / 20F;

		if (movePos < max && active && moving >= 0F) {
			if (moving == 0F) {
				world.playSound(null, pos, ModSounds.bellows, SoundCategory.BLOCKS, 0.1F, 3F);
			}

			if (tile instanceof AbstractFurnaceBlockEntity) {
				AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity) tile;
				Pair<AbstractCookingRecipe, Boolean> p = canSmelt(furnace);
				if (p != null) {
					AbstractCookingRecipe recipe = p.getFirst();
					boolean canSmelt = p.getSecond();
					if (canSmelt) {
						AccessorAbstractFurnaceTileEntity mFurnace = (AccessorAbstractFurnaceTileEntity) furnace;
						mFurnace.setCookTime(Math.min(recipe.getCookTime() - 1, mFurnace.getCookTime() + 20));
						mFurnace.setBurnTime(Math.max(0, mFurnace.getBurnTime() - 10));
					}

					if (furnace instanceof FurnaceBlockEntity
							&& furnace.hasWorld() && furnace.getCachedState().get(FurnaceBlock.LIT)) {
						// [VanillaCopy] BlockFurnace
						double d0 = (double) pos.getX() + 0.5D;
						double d1 = (double) pos.getY();
						double d2 = (double) pos.getZ() + 0.5D;
						// Botania: no playSound

						Direction enumfacing = furnace.getCachedState().get(FurnaceBlock.FACING);
						Direction.Axis enumfacing$axis = enumfacing.getAxis();
						double d3 = 0.52D;
						double d4 = world.random.nextDouble() * 0.6D - 0.3D;
						double d5 = enumfacing$axis == Direction.Axis.X ? (double) enumfacing.getOffsetX() * 0.52D : d4;
						double d6 = world.random.nextDouble() * 6.0D / 16.0D;
						double d7 = enumfacing$axis == Direction.Axis.Z ? (double) enumfacing.getOffsetZ() * 0.52D : d4;
						world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
						world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
					}
				}
			}

			movePos += incr * 3;
			moving = incr * 3;
			if (movePos >= max) {
				movePos = Math.min(max, movePos);
				moving = 0F;
				if (disable) {
					setActive(false);
				}
			}
		} else if (movePos > min) {
			movePos -= incr;
			moving = -incr;
			if (movePos <= min) {
				movePos = Math.max(min, movePos);
				moving = 0F;
			}
		}

	}

	public BlockEntity getLinkedTile() {
		Direction side = getCachedState().get(Properties.HORIZONTAL_FACING);
		return world.getBlockEntity(getPos().offset(side));
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
		if (!world.isClient) {
			boolean diff = this.active != active;
			this.active = active;
			if (diff) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}
	}

	public static Pair<AbstractCookingRecipe, Boolean> canSmelt(AbstractFurnaceBlockEntity furnace) {
		RecipeType<? extends AbstractCookingRecipe> rt = ExoflameFurnaceHandler.getRecipeType(furnace);
		AbstractCookingRecipe recipe = furnace.getWorld().getRecipeManager().getFirstMatch(rt, furnace, furnace.getWorld()).orElse(null);
		boolean canSmelt = ExoflameFurnaceHandler.canSmelt(furnace, recipe);
		return Pair.of(recipe, canSmelt);
	}

}
