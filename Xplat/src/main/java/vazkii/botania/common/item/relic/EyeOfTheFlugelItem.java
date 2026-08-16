/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.network.clientbound.FlugelEyeEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class EyeOfTheFlugelItem extends RelicItem {

	public EyeOfTheFlugelItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();

		if (player != null && player.isSecondaryUseActive()) {
			if (world.isClientSide) {
				for (int i = 0; i < 10; i++) {
					float x1 = (float) (pos.getX() + Math.random());
					float y1 = pos.getY() + 1;
					float z1 = (float) (pos.getZ() + Math.random());
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.5F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
					world.addParticle(data, x1, y1, z1, 0, 0.05F - (float) Math.random() * 0.05F, 0);
				}
			} else {
				ItemStack stack = context.getItemInHand();
				Map<ResourceLocation, BlockPos> boundPositions = new HashMap<>(stack.getOrDefault(
						BotaniaDataComponents.BOUND_POSITIONS, Collections.emptyMap()));
				boundPositions.put(world.dimension().location(), pos);
				stack.set(BotaniaDataComponents.BOUND_POSITIONS, boundPositions);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.EYE_OF_THE_FLUGEL_BIND, SoundSource.PLAYERS, 1F, 1F);
			}

			return InteractionResult.sidedSuccess(world.isClientSide());
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		if (level.isClientSide) {
			float x = (float) (livingEntity.getX() - Math.random() * livingEntity.getBbWidth());
			float y = (float) (livingEntity.getY() + Math.random());
			float z = (float) (livingEntity.getZ() - Math.random() * livingEntity.getBbWidth());
			WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.7F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
			level.addParticle(data, x, y, z, 0, 0.05F + (float) Math.random() * 0.05F, 0);
		}
	}

	@Nullable
	public static BlockPos getBoundPosInDimension(ItemStack stack, Level level) {
		return stack.getOrDefault(BotaniaDataComponents.BOUND_POSITIONS, Map.<ResourceLocation, BlockPos>of())
				.get(level.dimension().location());
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		// we can't access the level while building the tooltip, so the best we can do is figuring it out ahead of time
		ResourceLocation dimension = level.dimension().location();
		ResourceLocation knownDimension = stack.get(BotaniaDataComponents.LOCAL_DIMENSION);
		if (!Objects.equals(dimension, knownDimension)) {
			stack.set(BotaniaDataComponents.LOCAL_DIMENSION, dimension);
		}
		BlockPos boundPos = getBoundPosInDimension(stack, level);
		BlockPos knownBoundPos = stack.get(BotaniaDataComponents.LOCAL_BOUND_POSITION);
		if (!Objects.equals(boundPos, knownBoundPos)) {
			DataComponentHelper.setOptional(stack, BotaniaDataComponents.LOCAL_BOUND_POSITION, boundPos);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		return ItemUtils.startUsingInstantly(level, player, usedHand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		if (level.isClientSide()) {
			return stack;
		}
		BlockPos loc = getBoundPosInDimension(stack, level);
		if (loc == null) {
			return stack;
		}

		int x = loc.getX();
		int y = loc.getY();
		int z = loc.getZ();

		int cost = (int) (MathHelper.pointDistanceSpace(x + 0.5, y + 0.5, z + 0.5,
				livingEntity.getX(), livingEntity.getY(), livingEntity.getZ()) * 10);

		if (!(livingEntity instanceof Player player) || ManaItemHandler.instance().requestManaExact(stack, player, cost, true)) {
			moveParticlesAndSound(livingEntity);
			Vec3 sourcePos = livingEntity.position();
			livingEntity.teleportTo(x + 0.5, y + 1.5, z + 0.5);
			level.gameEvent(livingEntity, GameEvent.TELEPORT, sourcePos);
			moveParticlesAndSound(livingEntity);
		}

		return stack;
	}

	private static void moveParticlesAndSound(Entity entity) {
		XplatAbstractions.INSTANCE.sendToTracking(entity, new FlugelEyeEffectPacket(entity.getId()));
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
				BotaniaSounds.EYE_OF_THE_FLUGEL_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 40;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	public static class CoordBoundItemImpl implements CoordBoundItem {
		private final ItemStack stack;

		public CoordBoundItemImpl(ItemStack stack) {
			this.stack = stack;
		}

		@Nullable
		@Override
		public BlockPos getBinding(Level world) {
			return getBoundPosInDimension(stack, world);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, context, tooltip, flags);

		if (context.registries() == null) {
			return;
		}

		ResourceLocation dimension = stack.get(BotaniaDataComponents.LOCAL_DIMENSION);
		if (dimension == null) {
			return;
		}
		BlockPos binding = stack.get(BotaniaDataComponents.LOCAL_BOUND_POSITION);
		Component worldText = Component.literal(dimension.toString()).withStyle(ChatFormatting.GREEN);

		if (binding == null) {
			tooltip.add(Component.translatable("botaniamisc.flugelUnbound", worldText).withStyle(ChatFormatting.GRAY));
		} else {
			Component bindingText = Component.literal("[").withStyle(ChatFormatting.WHITE)
					.append(Component.literal(Integer.toString(binding.getX())).withStyle(ChatFormatting.GOLD))
					.append(", ")
					.append(Component.literal(Integer.toString(binding.getY())).withStyle(ChatFormatting.GOLD))
					.append(", ")
					.append(Component.literal(Integer.toString(binding.getZ())).withStyle(ChatFormatting.GOLD))
					.append("]");

			tooltip.add(Component.translatable("botaniamisc.flugelBound", bindingText, worldText).withStyle(ChatFormatting.GRAY));
		}
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, botaniaRL("challenge/eye_of_the_flugel"));
	}

}
