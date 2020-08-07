/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemEnderAir extends Item {
	public ItemEnderAir(Settings props) {
		super(props);
	}

	public static TypedActionResult<ItemStack> onPlayerInteract(PlayerEntity player, World world, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if (!stack.isEmpty() && stack.getItem() == Items.GLASS_BOTTLE && world.getDimension() == DimensionType.THE_END_REGISTRY_KEY) {
			if (!isClearFromDragonBreath(world, player.getBoundingBox().grow(3.5D))) {
				return TypedActionResult.pass(stack);
			}

			if (!world.isClient) {
				ItemStack enderAir = new ItemStack(ModItems.enderAirBottle);
				player.inventory.offerOrDrop(world, enderAir);
				stack.decrement(1);
				world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.5F, 1F);
			}

			return TypedActionResult.success(stack);
		}

		return TypedActionResult.pass(stack);
	}

	public static boolean isClearFromDragonBreath(World world, Box aabb) {
		List<AreaEffectCloudEntity> list = world.getEntitiesByClass(AreaEffectCloudEntity.class,
				aabb, entity -> entity != null && entity.isAlive()
						&& entity.getParticleType().getType() == ParticleTypes.DRAGON_BREATH);
		return list.isEmpty();
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!player.abilities.creativeMode) {
			stack.decrement(1);
		}

		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));

		if (!world.isClient) {
			EntityEnderAirBottle b = new EntityEnderAirBottle(player, world);
			b.setProperties(player, player.pitch, player.yaw, 0F, 1.5F, 1F);
			world.spawnEntity(b);
		}
		return TypedActionResult.method_29237(stack, world.isClient);
	}
}
