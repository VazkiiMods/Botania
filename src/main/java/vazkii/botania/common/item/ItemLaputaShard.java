/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILaputaImmobile;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemLaputaShard extends Item implements ILensEffect, ITinyPlanetExcempt {

	private static final String TAG_STATE = "_state";
	private static final String TAG_TILE = "_tile";
	private static final String TAG_X = "_x";
	private static final String TAG_Y = "_y";
	private static final String TAG_Y_START = "_yStart";
	private static final String TAG_Z = "_z";
	private static final String TAG_POINTY = "_pointy";
	private static final String TAG_HEIGHTSCALE = "_heightscale";
	private static final String TAG_ITERATION_I = "iterationI";
	private static final String TAG_ITERATION_J = "iterationJ";
	private static final String TAG_ITERATION_K = "iterationK";
	public static final String TAG_LEVEL = "level";

	private static final int BASE_RANGE = 14;
	private static final int BASE_OFFSET = 42;

	public ItemLaputaShard(Settings props) {
		super(props);
	}

	@Override
	public void appendStacks(@Nonnull ItemGroup tab, @Nonnull DefaultedList<ItemStack> list) {
		if (isIn(tab)) {
			for (int i = 0; i <= 20; i += 5) {
				ItemStack s = new ItemStack(this);
				if (i != 0) {
					s.getOrCreateTag().putInt(TAG_LEVEL, i - 1);
				}
				list.add(s);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		int level = getShardLevel(stack);
		Text levelLoc = new TranslatableText("botania.roman" + (level + 1));
		list.add(new TranslatableText("botaniamisc.shardLevel", levelLoc).formatted(Formatting.GRAY));
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		if (!world.isClient && pos.getY() < 160 && !world.getDimension().isUltrawarm()) {
			world.playSound(null, pos, ModSounds.laputaStart, SoundCategory.BLOCKS, 1.0F + world.random.nextFloat(), world.random.nextFloat() * 0.7F + 1.3F);
			ItemStack stack = ctx.getStack();
			spawnBurstFirst(world, pos, stack);
			if (ctx.getPlayer() != null) {
				UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayerEntity) ctx.getPlayer(), stack, (ServerWorld) world, pos.getX(), pos.getY(), pos.getZ());
			}
			stack.decrement(1);
		}

		return ActionResult.SUCCESS;
	}

	public void spawnBurstFirst(World world, BlockPos pos, ItemStack shard) {
		int range = BASE_RANGE + getShardLevel(shard);
		boolean pointy = world.random.nextDouble() < 0.25;
		double heightscale = (world.random.nextDouble() + 0.5) * ((double) BASE_RANGE / (double) range);
		spawnBurst(world, pos, shard, pointy, heightscale);
	}

	public void spawnBurst(World world, BlockPos pos, ItemStack lens) {
		boolean pointy = ItemNBTHelper.getBoolean(lens, TAG_POINTY, false);
		double heightscale = ItemNBTHelper.getDouble(lens, TAG_HEIGHTSCALE, 1);

		spawnBurst(world, pos, lens, pointy, heightscale);
	}

	private static boolean canMove(BlockState state, World world, BlockPos pos) {
		FluidState fluidState = state.getFluidState();
		boolean isFlowingFluid = !fluidState.isEmpty() && !fluidState.isStill();
		Block block = state.getBlock();

		return !state.isAir()
				&& !isFlowingFluid
				&& !(block instanceof FallingBlock)
				&& (!(block instanceof ILaputaImmobile) || ((ILaputaImmobile) block).canMove(world, pos))
				&& state.getHardness(world, pos) != -1;
	}

	public void spawnBurst(World world, BlockPos pos, ItemStack shard, boolean pointy, double heightscale) {
		int range = BASE_RANGE + getShardLevel(shard);

		int i = ItemNBTHelper.getInt(shard, TAG_ITERATION_I, 0);
		int j = ItemNBTHelper.getInt(shard, TAG_ITERATION_J, BASE_OFFSET - BASE_RANGE / 2);
		int k = ItemNBTHelper.getInt(shard, TAG_ITERATION_K, 0);

		if (j <= -BASE_RANGE * 2) {
			j = BASE_OFFSET - BASE_RANGE / 2;
		}
		if (k >= range * 2 + 1) {
			k = 0;
		}

		if (!world.isClient) {
			for (; i < range * 2 + 1; i++) {
				for (; j > -BASE_RANGE * 2; j--) {
					for (; k < range * 2 + 1; k++) {
						BlockPos pos_ = pos.add(-range + i, -BASE_RANGE + j, -range + k);

						if (inRange(pos_, pos, range, heightscale, pointy)) {
							BlockState state = world.getBlockState(pos_);
							Block block = state.getBlock();
							if (canMove(state, world, pos_)) {
								BlockEntity tile = world.getBlockEntity(pos_);

								if (tile != null && block instanceof BlockEntityProvider) {
									// Reset the TE so e.g. chests don't spawn their drops
									BlockEntity newTile = ((BlockEntityProvider) block).createBlockEntity(world);
									world.setBlockEntity(pos_, newTile);
								}
								world.syncWorldEvent(2001, pos_, Block.getRawIdFromState(state));
								world.setBlockState(pos_, Blocks.AIR.getDefaultState());

								ItemStack copyLens = new ItemStack(this);
								copyLens.getOrCreateTag().putInt(TAG_LEVEL, getShardLevel(shard));
								copyLens.getTag().put(TAG_STATE, NbtHelper.fromBlockState(state));
								CompoundTag cmp = new CompoundTag();
								if (tile != null) {
									cmp = tile.toTag(cmp);
								}
								ItemNBTHelper.setCompound(copyLens, TAG_TILE, cmp);
								ItemNBTHelper.setInt(copyLens, TAG_X, pos.getX());
								ItemNBTHelper.setInt(copyLens, TAG_Y, pos.getY());
								ItemNBTHelper.setInt(copyLens, TAG_Y_START, pos_.getY());
								ItemNBTHelper.setInt(copyLens, TAG_Z, pos.getZ());
								ItemNBTHelper.setBoolean(copyLens, TAG_POINTY, pointy);
								ItemNBTHelper.setDouble(copyLens, TAG_HEIGHTSCALE, heightscale);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_I, i);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_J, j);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_K, k);

								EntityManaBurst burst = getBurst(world, pos_, copyLens);
								world.spawnEntity(burst);
								return;
							}
						}
					}
					k = 0;
				}
				j = BASE_OFFSET - BASE_RANGE / 2;
			}
		}
	}

	public static int getShardLevel(ItemStack shard) {
		if (!shard.hasTag()) {
			return 0;
		}
		return shard.getOrCreateTag().getInt(TAG_LEVEL);
	}

	private boolean inRange(BlockPos pos, BlockPos srcPos, int range, double heightscale, boolean pointy) {
		if (pos.getY() >= srcPos.getY()) {
			return MathHelper.pointDistanceSpace(pos.getX(), 0, pos.getZ(), srcPos.getX(), 0, srcPos.getZ()) < range;
		} else if (!pointy) {
			return MathHelper.pointDistanceSpace(pos.getX(), pos.getY() / heightscale, pos.getZ(), srcPos.getX(), srcPos.getY() / heightscale, srcPos.getZ()) < range;
		} else {
			return MathHelper.pointDistanceSpace(pos.getX(), 0, pos.getZ(), srcPos.getX(), 0, srcPos.getZ()) < range - (srcPos.getY() - pos.getY()) / heightscale;
		}
	}

	public EntityManaBurst getBurst(World world, BlockPos pos, ItemStack stack) {
		EntityManaBurst burst = ModEntities.MANA_BURST.create(world);
		burst.updatePosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

		burst.setColor(0x00EAFF);
		burst.setMana(1);
		burst.setStartingMana(1);
		burst.setMinManaLoss(0);
		burst.setManaLossPerTick(0F);
		burst.setGravity(0F);
		burst.setBurstMotion(0, 0.5, 0);

		burst.setSourceLens(stack);
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {}

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return false;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		double speed = 0.35;
		int targetDistance = BASE_OFFSET;
		Entity entity = burst.entity();
		if (!entity.world.isClient) {
			entity.setVelocity(0, speed, 0);

			final int spawnTicks = 2;
			final int placeTicks = net.minecraft.util.math.MathHelper.floor(targetDistance / speed);

			ItemStack lens = burst.getSourceLens();

			if (burst.getTicksExisted() == spawnTicks) {
				int x = ItemNBTHelper.getInt(lens, TAG_X, 0);
				int y = ItemNBTHelper.getInt(lens, TAG_Y, -1);
				int z = ItemNBTHelper.getInt(lens, TAG_Z, 0);

				if (y != -1) {
					spawnBurst(entity.world, new BlockPos(x, y, z), lens);
				}
			} else if (burst.getTicksExisted() == placeTicks) {
				int x = net.minecraft.util.math.MathHelper.floor(entity.getX());
				int y = ItemNBTHelper.getInt(lens, TAG_Y_START, -1) + targetDistance;
				int z = net.minecraft.util.math.MathHelper.floor(entity.getZ());
				BlockPos pos = new BlockPos(x, y, z);

				BlockState placeState = Blocks.AIR.getDefaultState();
				if (lens.hasTag() && lens.getTag().contains(TAG_STATE)) {
					placeState = NbtHelper.toBlockState(lens.getTag().getCompound(TAG_STATE));
				}

				if (entity.world.getDimension().isUltrawarm() && placeState.contains(Properties.WATERLOGGED)) {
					placeState = placeState.with(Properties.WATERLOGGED, false);
				}

				if (entity.world.getBlockState(pos).getMaterial().isReplaceable()) {
					BlockEntity tile = null;
					CompoundTag tilecmp = ItemNBTHelper.getCompound(lens, TAG_TILE, false);
					if (tilecmp.contains("id")) {
						tile = BlockEntity.createFromTag(placeState, tilecmp);
					}

					entity.world.setBlockState(pos, placeState);
					entity.world.syncWorldEvent(2001, pos, Block.getRawIdFromState(placeState));
					if (tile != null) {
						tile.setPos(pos);
						entity.world.setBlockEntity(pos, tile);
					}
				} else {
					int ox = ItemNBTHelper.getInt(lens, TAG_X, 0);
					int oy = ItemNBTHelper.getInt(lens, TAG_Y_START, -1);
					int oz = ItemNBTHelper.getInt(lens, TAG_Z, 0);
					Block.dropStacks(placeState, entity.world, new BlockPos(ox, oy, oz));
				}

				entity.remove();
			}
		}
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		ItemStack lens = burst.getSourceLens();
		BlockState state = NbtHelper.toBlockState(lens.getOrCreateTag().getCompound(TAG_STATE));
		entity.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), entity.getX(), entity.getY(), entity.getZ(),
				entity.getVelocity().getX(), entity.getVelocity().getY(), entity.getVelocity().getZ());

		return true;
	}

	@Override
	public boolean shouldPull(ItemStack stack) {
		return false;
	}

}
