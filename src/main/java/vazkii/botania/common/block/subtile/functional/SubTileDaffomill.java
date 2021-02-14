/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.network.PacketItemAge;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;

public class SubTileDaffomill extends TileEntityFunctionalFlower {
	private static final String TAG_ORIENTATION = "orientation";
	private static final String TAG_WIND_TICKS = "windTicks";
	private static final String TAG_POWERED = "powered";

	private int windTicks = 0;
	private Direction orientation = Direction.NORTH;

	// On some occasions the client's redstone state is not the same as the server (eg. comparators,
	// which can return 0 power on the client as their TE state is often not synced at all)
	private boolean redstonePowered;

	public SubTileDaffomill() {
		super(ModSubtiles.DAFFOMILL);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().random.nextInt(4) == 0) {
			WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.15F, 0.05F, 0.05F, 0.05F);
			world.addParticle(data, getEffectivePos().getX() + Math.random(), getEffectivePos().getY() + Math.random(), getEffectivePos().getZ() + Math.random(), orientation.getOffsetX() * 0.1F, orientation.getOffsetY() * 0.1F, orientation.getOffsetZ() * 0.1F);
		}

		if (windTicks == 0 && getMana() > 0) {
			windTicks = 20;
			addMana(-1);
		}

		if (windTicks > 0 && !isRedstonePowered()) {
			Box axis = aabbForOrientation();

			if (axis != null) {
				List<ItemEntity> items = getWorld().getNonSpectatingEntities(ItemEntity.class, axis);
				int slowdown = getSlowdownFactor();
				for (ItemEntity item : items) {
					if (item.isAlive() && ((AccessorItemEntity) item).getAge() >= slowdown) {
						item.setVelocity(
								item.getVelocity().getX() + orientation.getOffsetX() * 0.05,
								item.getVelocity().getY() + orientation.getOffsetY() * 0.05,
								item.getVelocity().getZ() + orientation.getOffsetZ() * 0.05
						);
					}
				}
			}

			windTicks--;
		}
	}

	private Box aabbForOrientation() {
		int x = getEffectivePos().getX();
		int y = getEffectivePos().getY();
		int z = getEffectivePos().getZ();
		int w = 2;
		int h = 3;
		int l = 16;

		Box axis = null;
		switch (orientation) {
		case NORTH:
			axis = new Box(x - w, y - h, z - l, x + w + 1, y + h, z);
			break;
		case SOUTH:
			axis = new Box(x - w, y - h, z + 1, x + w + 1, y + h, z + l + 1);
			break;
		case WEST:
			axis = new Box(x - l, y - h, z - w, x, y + h, z + w + 1);
			break;
		case EAST:
			axis = new Box(x + 1, y - h, z - w, x + l + 1, y + h, z + w + 1);
			break;
		default:
		}
		return axis;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public boolean onWanded(PlayerEntity player, ItemStack wand) {
		if (player == null) {
			return false;
		}

		if (player.isSneaking()) {
			if (!player.world.isClient) {
				orientation = orientation.rotateYClockwise();
				sync();
			}

			return true;
		} else {
			return super.onWanded(player, wand);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (entity != null) {
			orientation = entity.getHorizontalFacing();
		}
		super.onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public RadiusDescriptor getRadius() {
		Box aabb = aabbForOrientation();
		aabb = new Box(aabb.minX, getEffectivePos().getY(), aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
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
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_ORIENTATION, orientation.getId());
		cmp.putInt(TAG_WIND_TICKS, windTicks);
		cmp.putBoolean(TAG_POWERED, redstonePowered);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);

		orientation = Direction.byId(cmp.getInt(TAG_ORIENTATION));
		windTicks = cmp.getInt(TAG_WIND_TICKS);
		redstonePowered = cmp.getBoolean(TAG_POWERED);
	}

	private boolean isRedstonePowered() {
		if (!world.isClient) {
			boolean powered = redstoneSignal != 0;
			if (powered != redstonePowered) {
				redstonePowered = powered;
				sync();
			}
		}
		return redstonePowered;
	}

	// Send item age to client to prevent client desync when an item is e.g. dropped by a powered open crate
	public static void onItemTrack(ServerPlayerEntity player, Entity entity) {
		if (entity instanceof ItemEntity) {
			int entityId = entity.getEntityId();
			int age = ((AccessorItemEntity) entity).getAge();
			PacketItemAge.send(player, entityId, age);
		}
	}
}
