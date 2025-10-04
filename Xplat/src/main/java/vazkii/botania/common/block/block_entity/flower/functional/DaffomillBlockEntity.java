/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.network.clientbound.ItemAgePacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class DaffomillBlockEntity extends FunctionalFlowerBlockEntity implements Wandable {
	private static final String TAG_ORIENTATION = "orientation";
	private static final String TAG_WIND_TICKS = "windTicks";

	private int windTicks = 0;
	private Direction orientation = Direction.NORTH;

	public DaffomillBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.DAFFOMILL, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (isPowered()) {
			return;
		}

		if (level.isClientSide() && level.random.nextInt(4) == 0) {
			WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.15F, 0.05F, 0.05F, 0.05F);
			emitParticle(data, Math.random(), Math.random(), Math.random(), orientation.getStepX() * 0.1F, orientation.getStepY() * 0.1F, orientation.getStepZ() * 0.1F);
		}

		if (windTicks == 0 && getMana() > 0) {
			windTicks = 20;
			addMana(-1);
		}

		if (windTicks > 0) {
			AABB axis = aabbForOrientation();

			if (axis != null) {
				List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, axis,
						itemEntity -> DelayHelper.canInteractWithImmediate(this, itemEntity));
				for (ItemEntity item : items) {
					item.setDeltaMovement(
							item.getDeltaMovement().x() + orientation.getStepX() * 0.05,
							item.getDeltaMovement().y() + orientation.getStepY() * 0.05,
							item.getDeltaMovement().z() + orientation.getStepZ() * 0.05
					);
				}
			}

			windTicks--;
		}
	}

	@Nullable
	private AABB aabbForOrientation() {
		int x = getEffectivePos().getX();
		int y = getEffectivePos().getY();
		int z = getEffectivePos().getZ();
		int w = 2;
		int h = 3;
		int l = 16;

		return switch (orientation) {
			case NORTH -> new AABB(x - w, y - h, z - l, x + w + 1, y + h, z);
			case SOUTH -> new AABB(x - w, y - h, z + 1, x + w + 1, y + h, z + l + 1);
			case WEST -> new AABB(x - l, y - h, z - w, x, y + h, z + w + 1);
			case EAST -> new AABB(x + 1, y - h, z - w, x + l + 1, y + h, z + w + 1);
			default -> null;
		};
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
		if (player == null || !player.isShiftKeyDown()) {
			return false;
		}

		if (!player.level().isClientSide) {
			orientation = orientation.getClockWise();
			sync();
		}

		return true;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (placer != null) {
			orientation = placer.getDirection();
		}
		super.setPlacedBy(level, pos, state, placer, stack);
	}

	@Override
	public RadiusDescriptor getRadius() {
		AABB aabb = aabbForOrientation();
		aabb = new AABB(aabb.minX, getEffectivePos().getY(), aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
		return new RadiusDescriptor.Rectangle(getEffectivePos(), aabb);
	}

	@Override
	public int getColor() {
		return 0xD8BA00;
	}

	@Override
	public int getMaxMana() {
		return 100;
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);

		cmp.putInt(TAG_ORIENTATION, orientation.get3DDataValue());
		cmp.putInt(TAG_WIND_TICKS, windTicks);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);

		orientation = Direction.from3DDataValue(cmp.getInt(TAG_ORIENTATION));
		windTicks = cmp.getInt(TAG_WIND_TICKS);
	}

	// Send timeCounter to client to prevent client desync when an item is e.g. dropped by a powered open crate
	public static void onItemTrack(Entity entity, ServerPlayer player) {
		if (entity instanceof ItemEntity item) {
			int entityId = entity.getId();
			int age = XplatAbstractions.INSTANCE.itemFlagsComponent(item).timeCounter;
			XplatAbstractions.INSTANCE.sendToPlayer(player, new ItemAgePacket(entityId, age));
		}
	}
}
