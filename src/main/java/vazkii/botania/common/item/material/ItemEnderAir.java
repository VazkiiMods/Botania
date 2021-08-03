/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemEnderAir extends Item {
	public ItemEnderAir(Properties props) {
		super(props);
	}

	public static InteractionResultHolder<ItemStack> onPlayerInteract(Player player, Level world, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!stack.isEmpty() && stack.getItem() == Items.GLASS_BOTTLE && world.dimension() == Level.END) {
			if (!isClearFromDragonBreath(world, player.getBoundingBox().inflate(3.5D))) {
				return InteractionResultHolder.pass(stack);
			}

			if (!world.isClientSide) {
				ItemStack enderAir = new ItemStack(ModItems.enderAirBottle);
				player.inventory.placeItemBackInInventory(world, enderAir);
				stack.shrink(1);
				world.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.5F, 1F);
			}

			return InteractionResultHolder.success(stack);
		}

		return InteractionResultHolder.pass(stack);
	}

	public static boolean isClearFromDragonBreath(Level world, AABB aabb) {
		List<AreaEffectCloud> list = world.getEntitiesOfClass(AreaEffectCloud.class,
				aabb, entity -> entity != null && entity.isAlive()
						&& entity.getParticle().getType() == ParticleTypes.DRAGON_BREATH);
		return list.isEmpty();
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!player.abilities.instabuild) {
			stack.shrink(1);
		}

		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

		if (!world.isClientSide) {
			EntityEnderAirBottle b = new EntityEnderAirBottle(player, world);
			b.shootFromRotation(player, player.xRot, player.yRot, 0F, 1.5F, 1F);
			world.addFreshEntity(b);
		}
		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
	}
}
