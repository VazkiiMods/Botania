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
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.EntityHelper;

import java.util.List;

public class SubTilePollidisiac extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 6;
	private static final int MANA_COST = 12;

	public SubTilePollidisiac(BlockPos pos, BlockState state) {
		super(ModSubtiles.POLLIDISIAC, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide) {
			var bounds = new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1));
			List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, bounds);
			List<Animal> animals = getLevel().getEntitiesOfClass(Animal.class, bounds);

			for (Animal animal : animals) {
				if (getMana() < MANA_COST) {
					break;
				}

				if (animal.getAge() == 0 && !animal.isInLove()) {
					for (ItemEntity item : items) {
						if (!DelayHelper.canInteractWith(this, item)) {
							continue;
						}

						if (animal.isFood(item.getItem())) {
							EntityHelper.shrinkItem(item);

							addMana(-MANA_COST);
							animal.setInLoveTime(1200);
							getLevel().broadcastEntityEvent(animal, EntityEvent.IN_LOVE_HEARTS);
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 120;
	}

	@Override
	public int getColor() {
		return 0xCF4919;
	}

}
