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
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemPinkinator extends Item {

	public ItemPinkinator(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		int range = 16;
		List<WitherEntity> withers = world.getNonSpectatingEntities(WitherEntity.class, new Box(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range));
		for (WitherEntity wither : withers) {
			if (!world.isClient && wither.isAlive() && !(wither instanceof EntityPinkWither)) {
				wither.remove();
				EntityPinkWither pink = ModEntities.PINK_WITHER.create(world);
				pink.refreshPositionAndAngles(wither.getX(), wither.getY(), wither.getZ(), wither.yaw, wither.pitch);
				pink.setAiDisabled(wither.isAiDisabled());
				if (wither.hasCustomName()) {
					pink.setCustomName(wither.getCustomName());
					pink.setCustomNameVisible(wither.isCustomNameVisible());
				}
				pink.initialize(world, world.getLocalDifficulty(pink.getBlockPos()), SpawnReason.CONVERSION, null, null);
				world.spawnEntity(pink);
				pink.playSpawnEffects();
				pink.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4F, (1F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
				UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayerEntity) player, stack, (ServerWorld) world, player.getX(), player.getY(), player.getZ());
				stack.decrement(1);
				return TypedActionResult.success(stack);
			}
		}

		return TypedActionResult.pass(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext flags) {
		tooltip.add(new TranslatableText("botaniamisc.pinkinatorDesc").formatted(Formatting.GRAY));
	}

}
