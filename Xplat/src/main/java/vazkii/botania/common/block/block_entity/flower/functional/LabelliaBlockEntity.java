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
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.helper.MathHelper;

import java.util.List;

public class LabelliaBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int PICKUP_RANGE = 0;
	private static final int RENAME_RANGE = 2;
	private static final int COST = 500;

	public LabelliaBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.LABELLIA, pos, state);
	}

	@Override
	public int getColor() {
		return 0xFFEE83;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (level.isClientSide || isPowered() || getMana() < COST) {
			return;
		}
		BlockPos effPos = getEffectivePos();
		BlockPos realPos = getBlockPos();

		for (ItemEntity nameTagEnt : level.getEntitiesOfClass(ItemEntity.class,
				MathHelper.inflateBoxAround(realPos, PICKUP_RANGE, 0),
				item -> item.getItem().is(Items.NAME_TAG)
						&& item.getItem().has(DataComponents.CUSTOM_NAME)
						&& DelayHelper.canInteractWith(this, item))) {
			ItemStack nameTag = nameTagEnt.getItem();
			AABB renameArea = MathHelper.inflateBoxAround(effPos, RENAME_RANGE, 0);
			Component name = nameTag.getHoverName();
			List<LivingEntity> nameableEntities = level.getEntitiesOfClass(LivingEntity.class, renameArea,
					EntitySelector.ENTITY_STILL_ALIVE.and(e -> !name.equals(e.getCustomName()) && !(e instanceof Player)));

			List<ItemEntity> nameableItems = level.getEntitiesOfClass(ItemEntity.class, renameArea,
					i -> DelayHelper.canInteractWith(this, i)
							&& i != nameTagEnt
							&& !name.equals(i.getItem().getHoverName()));

			if (nameableItems.isEmpty() && nameableEntities.isEmpty()) {
				continue;
			}
			for (LivingEntity e : nameableEntities) {
				// [VanillaCopy] from NameTagItem
				e.setCustomName(name);
				if (e instanceof Mob mob) {
					mob.setPersistenceRequired();
				}
			}
			for (ItemEntity i : nameableItems) {
				i.getItem().set(DataComponents.CUSTOM_NAME, name);
				EntityHelper.syncItem(i);
				((ServerLevel) level).sendParticles(ParticleTypes.INSTANT_EFFECT,
						i.getX(), i.getY(), i.getZ(),
						3, 0, 0, 0, 0);

			}
			addMana(-COST);
			EntityHelper.shrinkItem(nameTagEnt);
			level.playSound(null, effPos, BotaniaSounds.labellia, SoundSource.BLOCKS, 1F, 1F);
			break;
		}
	}

	@Override
	public boolean isOvergrowthAffected() {
		return false;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RENAME_RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return RadiusDescriptor.Rectangle.square(getBlockPos(), PICKUP_RANGE);
	}

	@Override
	public int getMaxMana() {
		return 6000;
	}
}
