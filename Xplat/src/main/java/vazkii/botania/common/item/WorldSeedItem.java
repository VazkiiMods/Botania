/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.handler.BotaniaSounds;

public class WorldSeedItem extends Item {

	public WorldSeedItem(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		BlockPos coords = world.getSharedSpawnPos();
		boolean inRange = coords.distToCenterSqr(player.getX(), player.getY(), player.getZ()) <= 24 * 24;
		if (world.dimension() == Level.OVERWORLD && !inRange) {
			if (!world.isClientSide) {
				Vec3 sourcePos = player.position();
				player.setXRot(0F);
				player.setYRot(0F);
				player.teleportTo(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5);

				while (!world.noCollision(player, player.getBoundingBox())) {
					player.teleportTo(player.getX(), player.getY() + 1, player.getZ());
				}

				world.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.worldSeedTeleport, SoundSource.PLAYERS, 1F, 1F);
				SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.25F, 1F, 0.25F, 10);
				((ServerLevel) world).sendParticles(data, player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ(), 50, player.getBbWidth() / 8, player.getBbHeight() / 4, player.getBbWidth() / 8, 0);
				stack.shrink(1);

				world.gameEvent(player, GameEvent.TELEPORT, sourcePos);
			}

			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

}
