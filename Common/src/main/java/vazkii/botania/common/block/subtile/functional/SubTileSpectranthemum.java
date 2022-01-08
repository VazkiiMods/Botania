/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.components.ItemFlagsComponent;
import vazkii.botania.common.core.helper.DelayHelper;
import vazkii.botania.common.network.EffectType;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;

public class SubTileSpectranthemum extends TileEntityFunctionalFlower {
	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private static final int COST = 24;
	private static final int RANGE = 2;
	private static final int BIND_RANGE = 12;

	public static final String TAG_TELEPORTED = "botania:teleported";

	private BlockPos bindPos = new BlockPos(0, Integer.MIN_VALUE, 0);

	public SubTileSpectranthemum(BlockPos pos, BlockState state) {
		super(ModSubtiles.SPECTRANTHEMUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && redstoneSignal == 0 && getLevel().hasChunkAt(bindPos)) {
			BlockPos pos = getEffectivePos();

			boolean did = false;

			List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(pos.offset(-RANGE, -RANGE, -RANGE), pos.offset(RANGE + 1, RANGE + 1, RANGE + 1)));

			for (ItemEntity item : items) {
				ItemFlagsComponent flags = EntityComponents.INTERNAL_ITEM.get(item);
				if (!DelayHelper.canInteractWith(this, item) || flags.spectranthemumTeleported) {
					continue;
				}

				ItemStack stack = item.getItem();
				Item sitem = stack.getItem();
				if (sitem instanceof IManaItem) {
					continue;
				}

				int cost = stack.getCount() * COST;
				if (getMana() >= cost) {
					spawnExplosionParticles(item, 10);
					item.setPos(bindPos.getX() + 0.5, bindPos.getY() + 1.5, bindPos.getZ() + 0.5);
					flags.spectranthemumTeleported = true;
					item.setDeltaMovement(Vec3.ZERO);
					spawnExplosionParticles(item, 10);
					addMana(-cost);
					did = true;
				}
			}

			if (did) {
				sync();
			}
		}
	}

	static void spawnExplosionParticles(Entity item, int p) {
		IXplatAbstractions.INSTANCE.sendEffectPacketNear(item, EffectType.ITEM_SMOKE, item.getX(), item.getY(), item.getZ(), item.getId(), p);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), BIND_RANGE);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_BIND_X, bindPos.getX());
		cmp.putInt(TAG_BIND_Y, bindPos.getY());
		cmp.putInt(TAG_BIND_Z, bindPos.getZ());
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
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
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		boolean bound = super.bindTo(player, wand, pos, side);

		if (!bound && !pos.equals(bindPos) && pos.distSqr(getEffectivePos()) <= BIND_RANGE * BIND_RANGE && !pos.equals(getEffectivePos())) {
			bindPos = pos;
			sync();

			return true;
		}

		return bound;
	}

	@Override
	public BlockPos getBinding() {
		return Botania.proxy.getClientPlayer().isShiftKeyDown() && bindPos.getY() != Integer.MIN_VALUE ? bindPos : super.getBinding();
	}

}
