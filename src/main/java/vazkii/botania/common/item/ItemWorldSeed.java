/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nonnull;

public class ItemWorldSeed extends Item {

	public ItemWorldSeed(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote) {
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		BlockPos coords = ((ServerWorld) world).func_241135_u_();
		if (world.func_234923_W_() == World.field_234918_g_ && MathHelper.pointDistanceSpace(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5, player.getPosX(), player.getPosY(), player.getPosZ()) > 24) {
			player.rotationPitch = 0F;
			player.rotationYaw = 0F;
			player.setPositionAndUpdate(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5);

			while (!world.hasNoCollisions(player, player.getBoundingBox())) {
				player.setPositionAndUpdate(player.getPosX(), player.getPosY() + 1, player.getPosZ());
			}

			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
			SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.25F, 1F, 0.25F, 10);
			((ServerWorld) world).spawnParticle(data, player.getPosX(), player.getPosY() + player.getHeight() / 2, player.getPosZ(), 50, player.getWidth() / 8, player.getHeight() / 4, player.getWidth() / 8, 0);

			stack.shrink(1);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult<>(ActionResultType.PASS, stack);
	}

}
