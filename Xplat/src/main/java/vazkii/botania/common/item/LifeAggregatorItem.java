/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.block.LifeAggregatorCarryable;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.helper.PlayerHelper;

import java.util.List;

public class LifeAggregatorItem extends Item {

	public LifeAggregatorItem(Properties properties) {
		super(properties);
	}

	public static boolean hasData(ItemStack stack) {
		return stack.has(BotaniaDataComponents.BLOCK_TYPE);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (!hasData(stack)) {
			return;
		}

		ResourceLocation blockId = stack.get(BotaniaDataComponents.BLOCK_TYPE);
		if (blockId != null) {
			BuiltInRegistries.BLOCK.getOptional(blockId).ifPresent(
					block -> tooltipComponents.add(block.getName().withStyle(ChatFormatting.GRAY)));
		}

		ResourceLocation entityId = stack.get(BotaniaDataComponents.MOB_TYPE);
		if (entityId != null) {
			BuiltInRegistries.ENTITY_TYPE.getOptional(entityId).ifPresent(
					type -> tooltipComponents.add(type.getDescription().copy().withStyle(ChatFormatting.GRAY)));
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (hasData(context.getItemInHand())) {
			return placeSpawner(context);
		} else {
			return tryCaptureSpawner(context)
					? InteractionResult.sidedSuccess(context.getLevel().isClientSide())
					: InteractionResult.PASS;
		}
	}

	private InteractionResult placeSpawner(UseOnContext context) {
		ItemStack mover = context.getItemInHand();
		ResourceLocation blockId = mover.get(BotaniaDataComponents.BLOCK_TYPE);
		Block block = BuiltInRegistries.BLOCK.get(blockId);
		if (block == Blocks.AIR || !(block instanceof EntityBlock)) {
			return InteractionResult.FAIL;
		}

		ItemStack useStack = new ItemStack(block);
		// we can transfer block state data via the standard placement logic
		if (mover.has(DataComponents.BLOCK_STATE)) {
			useStack.set(DataComponents.BLOCK_STATE, mover.get(DataComponents.BLOCK_STATE));
		}

		// applying NBT via vanilla placement logic would be sketchy for various reasons (components, op-only, etc.)
		Pair<InteractionResult, BlockPos> res = PlayerHelper.substituteUseTrackPos(context, useStack);
		if (res.getFirst().consumesAction()) {
			Level level = context.getLevel();
			BlockPos pos = res.getSecond();

			if (!level.isClientSide()) {
				BlockEntity blockEntity = level.getBlockEntity(pos);
				if (blockEntity != null && mover.has(DataComponents.BLOCK_ENTITY_DATA)) {
					blockEntity.loadWithComponents(
							mover.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag(),
							level.registryAccess());
				}

				LifeAggregatorCarryable carryable = LifeAggregatorCarryable.LOOKUP.find(level, pos);
				if (carryable != null) {
					carryable.updateAfterPlacement(context);
				}

				// must empty stack last, as otherwise components become inaccessible
				Player player = context.getPlayer();
				if (mover.consumeAndReturn(1, player).isEmpty() && player != null) {
					player.onEquippedItemBroken(this, LivingEntity.getSlotForHand(context.getHand()));
				}
			} else {
				RandomSource random = level.getRandom();
				for (int i = 0; i < 100; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle(0.45f + 0.2f * random.nextFloat(),
							random.nextFloat(), random.nextFloat(), random.nextFloat(), 6);
					level.addParticle(data,
							pos.getX() + random.nextDouble(),
							pos.getY() + random.nextDouble(),
							pos.getZ() + random.nextDouble(),
							0, 0, 0);
				}
			}
		}

		return res.getFirst();
	}

	private boolean tryCaptureSpawner(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		LifeAggregatorCarryable carryable = LifeAggregatorCarryable.LOOKUP.find(level, pos);
		if (carryable == null) {
			return false;
		}

		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();

		if (!level.isClientSide) {
			stack.set(BotaniaDataComponents.BLOCK_TYPE, BuiltInRegistries.BLOCK.getKey(carryable.getBlockType()));
			EntityType<?> entityType = carryable.getEntityType();
			if (entityType != null) {
				stack.set(BotaniaDataComponents.MOB_TYPE, BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
			}
			DataComponentHelper.setOptional(stack, DataComponents.BLOCK_STATE, carryable.gatherBlockStateData());
			DataComponentHelper.setOptional(stack, DataComponents.BLOCK_ENTITY_DATA,
					carryable.gatherBlockEntityData(level.registryAccess()));
			level.destroyBlock(pos, false, player);
			if (player != null) {
				player.getCooldowns().addCooldown(this, 20);
				if (player instanceof ServerPlayer serverPlayer) {
					UseItemSuccessTrigger.INSTANCE.trigger(serverPlayer, stack, serverPlayer.serverLevel(),
							pos.getX(), pos.getY(), pos.getZ());
				}
				player.onEquippedItemBroken(this, LivingEntity.getSlotForHand(context.getHand()));
			}
		} else {
			RandomSource random = level.getRandom();
			for (int i = 0; i < 50; i++) {
				WispParticleData data = WispParticleData.wisp(random.nextFloat() * 0.1f + 0.05f,
						random.nextFloat(), random.nextFloat(), random.nextFloat());
				level.addParticle(data, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						(random.nextDouble() - 0.5) * 0.15,
						(random.nextDouble() - 0.5) * 0.15,
						(random.nextDouble() - 0.5) * 0.15);
			}
		}

		return true;
	}
}
