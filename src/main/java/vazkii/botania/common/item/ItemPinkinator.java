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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemPinkinator extends Item {

	public ItemPinkinator(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int range = 16;
		List<WitherBoss> withers = world.getEntitiesOfClass(WitherBoss.class, new AABB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range));
		for (WitherBoss wither : withers) {
			if (!world.isClientSide && wither.isAlive() && !(wither instanceof EntityPinkWither)) {
				wither.discard();
				EntityPinkWither pink = ModEntities.PINK_WITHER.create(world);
				pink.moveTo(wither.getX(), wither.getY(), wither.getZ(), wither.getYRot(), wither.getXRot());
				pink.setNoAi(wither.isNoAi());
				if (wither.hasCustomName()) {
					pink.setCustomName(wither.getCustomName());
					pink.setCustomNameVisible(wither.isCustomNameVisible());
				}
				pink.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(pink.blockPosition()), MobSpawnType.CONVERSION, null, null);
				world.addFreshEntity(pink);
				pink.spawnAnim();
				pink.playSound(SoundEvents.GENERIC_EXPLODE, 4F, (1F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
				UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayer) player, stack, (ServerLevel) world, player.getX(), player.getY(), player.getZ());
				stack.shrink(1);
				return InteractionResultHolder.success(stack);
			}
		}

		return InteractionResultHolder.pass(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(new TranslatableComponent("botaniamisc.pinkinatorDesc").withStyle(ChatFormatting.GRAY));
	}

}
