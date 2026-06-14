/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BifrostBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.material.SelfReturningItem;

import java.util.List;

public class BifrostRodItem extends SelfReturningItem {

	private static final ResourceLocation AVATAR_OVERLAY = ResourceLocation.parse(ResourcesLib.MODEL_AVATAR_RAINBOW);

	private static final int MANA_COST = 750;
	private static final int MANA_COST_AVATAR = 4;
	private static final double PROFICIENCY_FACTOR = 1.6;
	private static final int TIME = 600;
	private static final int TIME_WITH_PROFICIENCY = (int) (TIME * PROFICIENCY_FACTOR);
	private static final int MAX_LENGTH = 100;
	private static final int MAX_LENGTH_WITH_PROFICIENCY = (int) (MAX_LENGTH * PROFICIENCY_FACTOR);

	public BifrostRodItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, false)) {
			BlockState bifrost = BotaniaBlocks.bifrost.defaultBlockState();
			Vec3 vector = player.getLookAngle().normalize();

			double x = player.getX();
			double y = player.getY() - 1;
			double z = player.getZ();
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos((int) x, (int) y, (int) z);

			double lastX = 0;
			double lastY = -1;
			double lastZ = 0;
			BlockPos.MutableBlockPos previousPos = new BlockPos.MutableBlockPos();

			int count = 0;
			boolean placedAny = false;

			boolean prof = ManaItemHandler.instance().hasProficiency(player, stack);
			int maxlen = prof ? MAX_LENGTH_WITH_PROFICIENCY : MAX_LENGTH;
			int time = prof ? TIME_WITH_PROFICIENCY : TIME;

			BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();

			while (count < maxlen) {
				previousPos.set(lastX, lastY, lastZ);

				if (!previousPos.equals(pos)) { // Occasionally moving to the next segment stays on the same location, skip it
					if (!level.isEmptyBlock(pos) && level.getBlockState(pos) != bifrost && count >= 4) {
						break; // Stop placing if you hit a wall (bifrost blocks are fine), but only after 4 segments.
					}
					if (level.isOutsideBuildHeight(pos.getY())) {
						break;
					}
					if (placeBridgeSegment(level, pos, placePos, time)) {
						placedAny = true;
					}
				}

				count++;

				lastX = x;
				lastY = y;
				lastZ = z;

				x += vector.x;
				y += vector.y;
				z += vector.z;
				pos.set(x, y, z);
			}

			if (placedAny) {
				level.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.bifrostRod, SoundSource.PLAYERS, 1, 1);
				player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
				ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, true);
				player.getCooldowns().addCooldown(this, player.isCreative() ? 10 : TIME);
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	private static boolean placeBridgeSegment(Level level, BlockPos center, BlockPos.MutableBlockPos placePos, int time) {
		BlockState bifrost = BotaniaBlocks.bifrost.defaultBlockState();
		boolean placed = false;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				placePos.set(center.getX() + i, center.getY(), center.getZ() + j);
				if (level.isEmptyBlock(placePos) || level.getBlockState(placePos) == bifrost) {
					level.setBlock(placePos, bifrost, Block.UPDATE_CLIENTS);

					if (level.getBlockEntity(placePos) instanceof BifrostBlockEntity bifrostBlockEntity) {
						bifrostBlockEntity.ticks = time;
						placed = true;
					}
				}
			}
		}
		return placed;
	}

	public record AvatarBehavior(ItemStack rod, Avatar avatar) implements AvatarWieldable {

		public static final int BRIDGE_LENGTH = 20;

		@Override
		public void onAvatarUpdate(ServerLevel level, BlockPos pos, ManaReceiver receiver) {
			if (receiver.getCurrentMana() < MANA_COST_AVATAR * 25
					|| !avatar.isEnabled() || level.isOutsideBuildHeight(pos.getY() - 1)) {
				return;
			}

			Direction facing = avatar.getAvatarFacing();
			Direction sideways = facing.getClockWise();
			BoundingBox bridgeBox = BoundingBox.fromCorners(
					pos.offset(
							facing.getStepX() + sideways.getStepX(),
							0,
							facing.getStepZ() + sideways.getStepZ()
					),
					pos.offset(
							BRIDGE_LENGTH * facing.getStepX() - sideways.getStepX(),
							0,
							BRIDGE_LENGTH * facing.getStepZ() - sideways.getStepZ()
					)
			);
			AABB axis = AABB.of(bridgeBox);
			List<ServerPlayer> players = level.getPlayers(player -> isRelevantPlayer(player, axis));
			int y = pos.getY();
			BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();
			for (Player player : players) {
				int xMin = Mth.floor(player.getX(-1.5)) - 1;
				int xMax = Mth.floor(player.getX(1.5)) + 1;
				int zMin = Mth.floor(player.getZ(-1.5)) - 1;
				int zMax = Mth.floor(player.getZ(1.5)) + 1;
				for (int x = xMin; x <= xMax; x++) {
					for (int z = zMin; z <= zMax; z++) {
						if (!bridgeBox.isInside(x, y, z)) {
							continue;
						}

						placePos.set(x, y - 1, z);
						BlockState state = level.getBlockState(placePos);
						if (state.isAir()) {
							if (level.setBlockAndUpdate(placePos, BotaniaBlocks.bifrost.defaultBlockState())) {
								if (level.getBlockEntity(placePos) instanceof BifrostBlockEntity bifrostBlockEntity) {
									bifrostBlockEntity.ticks = 10;
								}
								receiver.receiveMana(-MANA_COST_AVATAR);
							}
						} else if (state.is(BotaniaBlocks.bifrost)
								&& level.getBlockEntity(placePos) instanceof BifrostBlockEntity bifrostBlockEntity
								&& bifrostBlockEntity.ticks < 2) {
							bifrostBlockEntity.ticks += 10;
							receiver.receiveMana(-MANA_COST_AVATAR);
						}
					}
				}
			}

		}

		private static boolean isRelevantPlayer(Player player, AABB aabb) {
			if (!player.canBeSeenByAnyone()) {
				return false;
			}
			// check if feet are in relevant bounding box
			AABB other = player.getBoundingBox();
			return aabb.minX <= other.maxX && aabb.maxX >= other.minX
					&& aabb.minY <= player.getY() && aabb.maxY >= player.getY()
					&& aabb.minZ <= other.maxZ && aabb.maxZ >= other.minZ;
		}

		@Override
		public ResourceLocation getOverlayResource() {
			return AVATAR_OVERLAY;
		}
	}

}
