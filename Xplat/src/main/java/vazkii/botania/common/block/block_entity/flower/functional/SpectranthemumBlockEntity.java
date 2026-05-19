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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.network.clientbound.ItemSmokeEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class SpectranthemumBlockEntity extends FunctionalFlowerBlockEntity {
	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private static final int BASE_COST = 2;
	private static final int RANGE = 2;

	@Nullable
	private BlockPos bindPos;

	public SpectranthemumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.SPECTRANTHEMUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || bindPos == null || isPowered() || !getLevel().hasChunkAt(bindPos)) {
			return;
		}

		List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class,
				MathHelper.inflateBoxAround(getEffectivePos(), RANGE),
				DelayHelper.asPredicateFor(DelayHelper::canMove, this));

		for (ItemEntity item : items) {
			ItemStack stack = item.getItem();
			if (XplatAbstractions.INSTANCE.findManaItem(stack) != null) {
				continue;
			}

			// TODO: maybe teleport fewer items if the cost is too much?
			double cost = BASE_COST * stack.getCount() * Math.sqrt(bindPos.distToCenterSqr(item.position()));
			if (getMana() >= cost) {
				spawnExplosionParticles(item, 10);
				BlockPos sourcePos = item.blockPosition();
				item.setPos(bindPos.getX() + 0.5, bindPos.getY() + 1.5, bindPos.getZ() + 0.5);
				EntityHelper.addTeleportTicketIfFarAway(item, sourcePos);
				item.setDeltaMovement(Vec3.ZERO);
				spawnExplosionParticles(item, 10);
				addMana(-(int) cost);
			}
		}
	}

	static void spawnExplosionParticles(Entity item, int p) {
		XplatAbstractions.INSTANCE.sendToTracking(item, new ItemSmokeEffectPacket(item.position(), item.getId(), p));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);
		if (bindPos != null) {
			cmp.putInt(TAG_BIND_X, bindPos.getX());
			cmp.putInt(TAG_BIND_Y, bindPos.getY());
			cmp.putInt(TAG_BIND_Z, bindPos.getZ());
		}
	}

	@Override
	public void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);
		if (cmp.contains(TAG_BIND_X)) {
			bindPos = new BlockPos(
					cmp.getInt(TAG_BIND_X),
					cmp.getInt(TAG_BIND_Y),
					cmp.getInt(TAG_BIND_Z)
			);
		} else {
			bindPos = null;
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		if (bindPos != null) {
			tag.putInt(TAG_BIND_X, bindPos.getX());
			tag.putInt(TAG_BIND_Y, bindPos.getY());
			tag.putInt(TAG_BIND_Z, bindPos.getZ());
		}
		return tag;
	}

	@Override
	public int getColor() {
		return 0x98BCFF;
	}

	@Override
	public int getMaxMana() {
		return 5000;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		boolean bound = super.bindTo(player, wand, pos, side);

		if (!bound && !pos.equals(bindPos) && !pos.equals(getEffectivePos())) {
			bindPos = pos;
			markForImmediateSync();
			setChanged();

			return true;
		}

		return bound;
	}

	@Nullable
	@Override
	public BlockPos getBinding() {
		return Proxy.INSTANCE.getClientPlayer().isShiftKeyDown() && bindPos != null ? bindPos : super.getBinding();
	}

}
