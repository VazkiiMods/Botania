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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketItemAge;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;

public class SubTileDaffomill extends TileEntityFunctionalFlower {
	private static final String TAG_ORIENTATION = "orientation";
	private static final String TAG_WIND_TICKS = "windTicks";

	private int windTicks = 0;
	private Direction orientation = Direction.NORTH;

	public SubTileDaffomill() {
		super(ModSubtiles.DAFFOMILL);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().rand.nextInt(4) == 0) {
			WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.15F, 0.05F, 0.05F, 0.05F);
			world.addParticle(data, getEffectivePos().getX() + Math.random(), getEffectivePos().getY() + Math.random(), getEffectivePos().getZ() + Math.random(), orientation.getXOffset() * 0.1F, orientation.getYOffset() * 0.1F, orientation.getZOffset() * 0.1F);
		}

		if (windTicks == 0 && getMana() > 0) {
			windTicks = 20;
			addMana(-1);
		}

		if (windTicks > 0 && redstoneSignal == 0) {
			AxisAlignedBB axis = aabbForOrientation();

			if (axis != null) {
				List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, axis);
				int slowdown = getSlowdownFactor();
				for (ItemEntity item : items) {
					if (item.isAlive() && ((AccessorItemEntity) item).getAge() >= slowdown) {
						item.setMotion(
								item.getMotion().getX() + orientation.getXOffset() * 0.05,
								item.getMotion().getY() + orientation.getYOffset() * 0.05,
								item.getMotion().getZ() + orientation.getZOffset() * 0.05
						);
					}
				}
			}

			windTicks--;
		}
	}

	private AxisAlignedBB aabbForOrientation() {
		int x = getEffectivePos().getX();
		int y = getEffectivePos().getY();
		int z = getEffectivePos().getZ();
		int w = 2;
		int h = 3;
		int l = 16;

		AxisAlignedBB axis = null;
		switch (orientation) {
		case NORTH:
			axis = new AxisAlignedBB(x - w, y - h, z - l, x + w + 1, y + h, z);
			break;
		case SOUTH:
			axis = new AxisAlignedBB(x - w, y - h, z + 1, x + w + 1, y + h, z + l + 1);
			break;
		case WEST:
			axis = new AxisAlignedBB(x - l, y - h, z - w, x, y + h, z + w + 1);
			break;
		case EAST:
			axis = new AxisAlignedBB(x + 1, y - h, z - w, x + l + 1, y + h, z + w + 1);
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
			if (!player.world.isRemote) {
				orientation = orientation.rotateY();
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
		AxisAlignedBB aabb = aabbForOrientation();
		aabb = new AxisAlignedBB(aabb.minX, getEffectivePos().getY(), aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
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
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_ORIENTATION, orientation.getIndex());
		cmp.putInt(TAG_WIND_TICKS, windTicks);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);

		orientation = Direction.byIndex(cmp.getInt(TAG_ORIENTATION));
		windTicks = cmp.getInt(TAG_WIND_TICKS);
	}

	// Send item age to client to prevent client desync when an item is e.g. dropped by a powered open crate
	public static void onItemTrack(PlayerEvent.StartTracking evt) {
		if (evt.getTarget() instanceof ItemEntity) {
			int entityId = evt.getTarget().getEntityId();
			int age = ((AccessorItemEntity) evt.getTarget()).getAge();
			PacketHandler.sendTo((ServerPlayerEntity) evt.getPlayer(), new PacketItemAge(entityId, age));
		}
	}
}
