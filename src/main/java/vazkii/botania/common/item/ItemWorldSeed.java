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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nonnull;

public class ItemWorldSeed extends Item {

	public ItemWorldSeed(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (world.isClient) {
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}

		BlockPos coords = ((ServerWorld) world).getSpawnPos();
		if (world.getRegistryKey() == World.OVERWORLD && MathHelper.pointDistanceSpace(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5, player.getX(), player.getY(), player.getZ()) > 24) {
			player.pitch = 0F;
			player.yaw = 0F;
			player.requestTeleport(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5);

			while (!world.isSpaceEmpty(player, player.getBoundingBox())) {
				player.requestTeleport(player.getX(), player.getY() + 1, player.getZ());
			}

			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
			SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.25F, 1F, 0.25F, 10);
			((ServerWorld) world).spawnParticles(data, player.getX(), player.getY() + player.getHeight() / 2, player.getZ(), 50, player.getWidth() / 8, player.getHeight() / 4, player.getWidth() / 8, 0);

			stack.decrement(1);
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}

		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

}
