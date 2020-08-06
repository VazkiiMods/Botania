/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;

public class SubTileSpectranthemum extends TileEntityFunctionalFlower {
	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private static final int COST = 24;
	private static final int RANGE = 2;
	private static final int BIND_RANGE = 12;

	private static final String TAG_TELEPORTED = "botania:teleported";

	private BlockPos bindPos = new BlockPos(0, -1, 0);

	public SubTileSpectranthemum() {
		super(ModSubtiles.SPECTRANTHEMUM);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isRemote && redstoneSignal == 0 && getWorld().isBlockLoaded(bindPos)) {
			BlockPos pos = getEffectivePos();

			boolean did = false;

			List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.add(-RANGE, -RANGE, -RANGE), pos.add(RANGE + 1, RANGE + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();

			for (ItemEntity item : items) {
				int age = ((AccessorItemEntity) item).getAge();
				if (age < 60 + slowdown || !item.isAlive() || item.getPersistentData().getBoolean(TAG_TELEPORTED)) {
					continue;
				}

				ItemStack stack = item.getItem();
				if (!stack.isEmpty()) {
					Item sitem = stack.getItem();
					if (sitem instanceof IManaItem) {
						continue;
					}

					int cost = stack.getCount() * COST;
					if (getMana() >= cost) {
						spawnExplosionParticles(item, 10);
						item.setPosition(bindPos.getX() + 0.5, bindPos.getY() + 1.5, bindPos.getZ() + 0.5);
						item.getPersistentData().putBoolean(TAG_TELEPORTED, true);
						item.setMotion(Vector3d.ZERO);
						spawnExplosionParticles(item, 10);
						addMana(-cost);
						did = true;
					}
				}
			}

			if (did) {
				sync();
			}
		}
	}

	static void spawnExplosionParticles(Entity item, int p) {
		PacketHandler.sendToNearby(item.world, item.func_233580_cy_(), new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.ITEM_SMOKE, item.getPosX(), item.getPosY(), item.getPosZ(), item.getEntityId(), p));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_BIND_X, bindPos.getX());
		cmp.putInt(TAG_BIND_Y, bindPos.getY());
		cmp.putInt(TAG_BIND_Z, bindPos.getZ());
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		bindPos = new BlockPos(
				cmp.getInt(TAG_BIND_X),
				cmp.getInt(TAG_BIND_Y),
				cmp.getInt(TAG_BIND_Z)
		);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x98BCFF;
	}

	@Override
	public int getMaxMana() {
		return 16000;
	}

	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		boolean bound = super.bindTo(player, wand, pos, side);

		if (!bound && !pos.equals(bindPos) && pos.distanceSq(getEffectivePos()) <= BIND_RANGE * BIND_RANGE && !pos.equals(getEffectivePos())) {
			bindPos = pos;
			sync();

			return true;
		}

		return bound;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockPos getBinding() {
		return Minecraft.getInstance().player.isSneaking() && bindPos.getY() != -1 ? bindPos : super.getBinding();
	}

}
