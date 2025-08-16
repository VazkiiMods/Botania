/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.LensEffectItem;
import vazkii.botania.api.mana.TinyPlanetExcempt;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.component.LaputaState;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;
import java.util.Optional;

public class LaputaShardItem extends Item implements LensEffectItem, TinyPlanetExcempt, CustomCreativeTabContents {

	public static final int MAX_LEVEL = 19;
	private static final int BASE_RANGE = 14;
	private static final int BASE_OFFSET = 42;

	public LaputaShardItem(Properties props) {
		super(props);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		for (int i = 0; i <= 20; i += 5) {
			ItemStack s = new ItemStack(me);
			if (i != 0) {
				DataComponentHelper.setIntNonZero(s, BotaniaDataComponents.SHARD_LEVEL, i - 1);
				if (i == 20) {
					DataComponentHelper.bumpRarity(s);
				}
			}
			output.accept(s);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flags) {
		int level = getShardLevel(stack);
		Component levelLoc = Component.translatable("botania.roman" + (level + 1));
		list.add(Component.translatable("botaniamisc.shardLevel", levelLoc).withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("botaniamisc.shardRange", getRange(stack)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		if (pos.getY() < world.getMaxBuildHeight() - BASE_OFFSET && !world.dimensionType().hasCeiling()) {
			if (!world.isClientSide) {
				world.gameEvent(ctx.getPlayer(), GameEvent.ENTITY_PLACE, pos);
				world.playSound(null, pos, BotaniaSounds.laputaStart, SoundSource.BLOCKS, 1.0F + world.random.nextFloat(), world.random.nextFloat() * 0.7F + 1.3F);
				ItemStack stack = ctx.getItemInHand();
				spawnFirstBurst(world, pos, stack);
				if (ctx.getPlayer() != null) {
					UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayer) ctx.getPlayer(), stack, (ServerLevel) world, pos.getX(), pos.getY(), pos.getZ());
				}
				stack.shrink(1);
			}
			return InteractionResult.sidedSuccess(world.isClientSide());
		}

		return InteractionResult.PASS;
	}

	private int getRange(ItemStack shard) {
		return BASE_RANGE + getShardLevel(shard);
	}

	protected void spawnFirstBurst(Level world, BlockPos pos, ItemStack shard) {
		int range = getRange(shard);
		boolean pointy = shard.getOrDefault(BotaniaDataComponents.SHARD_POINTY, world.random.nextDouble() < 0.25);
		float heightScale = shard.getOrDefault(BotaniaDataComponents.SHARD_HEIGHT_SCALE, (world.random.nextFloat() + 0.5f) * ((float) BASE_RANGE / range));
		spawnNextBurst(world, pos, shard, pointy, heightScale);
	}

	protected void spawnNextBurst(Level world, BlockPos pos, ItemStack lens) {
		boolean pointy = lens.getOrDefault(BotaniaDataComponents.SHARD_POINTY, false);
		float heightScale = lens.getOrDefault(BotaniaDataComponents.SHARD_HEIGHT_SCALE, 1f);

		spawnNextBurst(world, pos, lens, pointy, heightScale);
	}

	private static boolean canMove(BlockState state, Level world, BlockPos pos) {
		FluidState fluidState = state.getFluidState();
		boolean isFlowingFluid = !fluidState.isEmpty() && !fluidState.isSource();
		Block block = state.getBlock();

		return !state.isAir()
				&& !isFlowingFluid
				&& !(block instanceof FallingBlock)
				&& (!state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)
						|| state.is(BotaniaTags.Blocks.LAPUTA_NO_DOUBLE_BLOCK)
						|| state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER)
				&& !state.is(BotaniaTags.Blocks.LAPUTA_IMMOBILE)
				&& state.getDestroySpeed(world, pos) != -1;
	}

	private void spawnNextBurst(Level world, BlockPos pos, ItemStack shard, boolean pointy, double heightScale) {
		int range = getRange(shard);

		LaputaState laputaState = shard.get(BotaniaDataComponents.LAPUTA_STATE);
		int i = laputaState != null ? laputaState.i() : 0;
		int j = laputaState != null ? laputaState.j() : BASE_OFFSET - BASE_RANGE / 2;
		int k = laputaState != null ? laputaState.k() : 0;

		if (j <= -BASE_RANGE * 2) {
			j = BASE_OFFSET - BASE_RANGE / 2;
		}
		if (k >= range * 2 + 1) {
			k = 0;
		}

		for (; i < range * 2 + 1; i++) {
			for (; j > -BASE_RANGE * 2; j--) {
				for (; k < range * 2 + 1; k++) {
					BlockPos pos_ = pos.offset(-range + i, -BASE_RANGE + j, -range + k);

					if (!inRange(pos_, pos, range, heightScale, pointy)) {
						continue;
					}

					BlockState state = world.getBlockState(pos_);
					Block block = state.getBlock();

					if (!canMove(state, world, pos_)) {
						continue;
					}

					BlockEntity tile = world.getBlockEntity(pos_);

					CompoundTag cmp;
					if (tile != null) {
						cmp = tile.saveWithFullMetadata(world.registryAccess());

						//Reset the block entity so e.g. chests don't spawn their drops
						BlockEntity newTile = ((EntityBlock) block).newBlockEntity(pos_, state);
						world.setBlockEntity(newTile);
					} else {
						cmp = null;
					}

					// This can fail from e.g. permissions plugins or event cancellations
					if (!world.removeBlock(pos_, false)) {
						// put the original block entity back
						if (tile != null) {
							world.setBlockEntity(tile);
						}
						continue;
					}

					world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos_, Block.getId(state));
					world.gameEvent(null, GameEvent.BLOCK_DESTROY, pos_);

					ItemStack copyLens = new ItemStack(this);
					copyLens.set(BotaniaDataComponents.SHARD_LEVEL, getShardLevel(shard));
					copyLens.set(BotaniaDataComponents.SHARD_POINTY, pointy);
					copyLens.set(BotaniaDataComponents.SHARD_HEIGHT_SCALE, (float) heightScale);
					copyLens.set(BotaniaDataComponents.LAPUTA_STATE, new LaputaState(
							state, Optional.ofNullable(cmp).map(CustomData::of), pos, pos_.getY(), i, j, k));

					ManaBurstEntity burst = getBurst(world, pos_, copyLens);
					world.addFreshEntity(burst);
					return;
				}
				k = 0;
			}
			j = BASE_OFFSET - BASE_RANGE / 2;
		}
	}

	public static int getShardLevel(ItemStack shard) {
		return shard.getOrDefault(BotaniaDataComponents.SHARD_LEVEL, 0);
	}

	private boolean inRange(BlockPos pos, BlockPos srcPos, int range, double heightScale, boolean pointy) {
		if (pos.getY() >= srcPos.getY()) {
			return MathHelper.pointDistanceSpace(pos.getX(), 0, pos.getZ(), srcPos.getX(), 0, srcPos.getZ()) < range;
		} else if (!pointy) {
			return MathHelper.pointDistanceSpace(pos.getX(), pos.getY() / heightScale, pos.getZ(), srcPos.getX(), srcPos.getY() / heightScale, srcPos.getZ()) < range;
		} else {
			return MathHelper.pointDistanceSpace(pos.getX(), 0, pos.getZ(), srcPos.getX(), 0, srcPos.getZ()) < range - (srcPos.getY() - pos.getY()) / heightScale;
		}
	}

	public ManaBurstEntity getBurst(Level world, BlockPos pos, ItemStack stack) {
		ManaBurstEntity burst = BotaniaEntities.MANA_BURST.create(world);
		burst.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

		burst.setColor(0x00EAFF);
		burst.setMana(1);
		burst.setStartingMana(1);
		burst.setMinManaLoss(0);
		burst.setManaLossPerTick(0F);
		burst.setGravity(0F);
		burst.setDeltaMovement(0, 0.5, 0);

		burst.setSourceLens(stack);
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props, Level level) {}

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		return false;
	}

	@Override
	public void updateBurst(ManaBurst burst, ItemStack stack) {
		double speed = 0.35;
		int targetDistance = BASE_OFFSET;
		Entity entity = burst.entity();
		if (!entity.level().isClientSide) {
			entity.setDeltaMovement(0, speed, 0);

			final int spawnTicks = 2;
			final int placeTicks = net.minecraft.util.Mth.floor(targetDistance / speed);

			ItemStack lens = burst.getSourceLens();
			LaputaState laputaState = lens.get(BotaniaDataComponents.LAPUTA_STATE);

			if (burst.getTicksExisted() == spawnTicks) {
				if (laputaState != null && laputaState.nextPos() != null) {
					spawnNextBurst(entity.level(), laputaState.nextPos(), lens);
				}
			} else if (burst.getTicksExisted() == placeTicks) {
				int x = net.minecraft.util.Mth.floor(entity.getX());
				int y = (laputaState != null ? laputaState.yStart() : -1) + targetDistance;
				int z = net.minecraft.util.Mth.floor(entity.getZ());
				BlockPos pos = new BlockPos(x, y, z);

				BlockState placeState = Blocks.AIR.defaultBlockState();
				if (laputaState != null) {
					placeState = laputaState.blockState();
				}

				if (entity.level().dimensionType().ultraWarm() && placeState.hasProperty(BlockStateProperties.WATERLOGGED)) {
					placeState = placeState.setValue(BlockStateProperties.WATERLOGGED, false);
				}

				if (entity.level().getBlockState(pos).canBeReplaced()) {
					BlockEntity tile = null;
					CompoundTag tilecmp = laputaState != null && laputaState.blockEntityData().isPresent() ? laputaState.blockEntityData().get().copyTag() : null;
					if (tilecmp != null && tilecmp.contains("id")) {
						tile = BlockEntity.loadStatic(pos, placeState, tilecmp, entity.level().registryAccess());
					}

					if (placeState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)
							&& !placeState.is(BotaniaTags.Blocks.LAPUTA_NO_DOUBLE_BLOCK)
							&& placeState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
						// place upper half, which was previously broken implicitly when the bottom was picked up
						entity.level().setBlock(pos.above(),
								placeState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER),
								Block.UPDATE_CLIENTS);
					}
					entity.level().setBlockAndUpdate(pos, placeState);
					entity.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(placeState));
					entity.level().gameEvent(null, GameEvent.BLOCK_PLACE, pos);
					if (tile != null) {
						entity.level().setBlockEntity(tile);
					}
				} else {
					BlockPos oPos;
					if (laputaState != null) {
						oPos = (laputaState.nextPos() != null ? laputaState.nextPos() : BlockPos.ZERO).atY(laputaState.yStart());
					} else {
						oPos = new BlockPos(0, -1, 0);
					}
					Block.dropResources(placeState, entity.level(), oPos);
				}

				entity.discard();
			}
		}
	}

	@Override
	public boolean doParticles(ManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		ItemStack lens = burst.getSourceLens();
		LaputaState laputaState = lens.get(BotaniaDataComponents.LAPUTA_STATE);

		if (laputaState != null) {
			BlockState state = laputaState.blockState();
			entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state),
					entity.getX(), entity.getY(), entity.getZ(),
					entity.getDeltaMovement().x(), entity.getDeltaMovement().y(), entity.getDeltaMovement().z());
		}
		return true;
	}

	@Override
	public boolean shouldPull(ItemStack stack) {
		return false;
	}

}
